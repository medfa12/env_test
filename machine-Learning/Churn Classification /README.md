# Churn Classification - Prediction du Churn Client

## Description du projet

Ce projet realise une analyse complete de la prediction du churn (depart) de clients bancaires, depuis l'exploration des donnees jusqu'au deploiement d'une application web. Il combine un notebook d'analyse ML, une API de prediction (FastAPI) et une interface utilisateur interactive (Streamlit).

## Structure du projet

```
Churn Classification/
├── README.md
├── requirements.txt            # Dependances backend + frontend
├── Churn_Modelling.csv         # Dataset source
├── churn_analysis.ipynb        # Notebook d'analyse complet
├── backend/
│   ├── prepare_model.py        # Script d'entrainement et export des artefacts
│   ├── app.py                  # API FastAPI
│   ├── model.pkl               # Modele XGBoost entraine
│   ├── scaler.pkl              # StandardScaler
│   ├── label_encoder_gender.pkl# LabelEncoder pour Gender
│   └── feature_names.pkl       # Ordre des features
└── frontend/
    └── frontend.py             # Interface Streamlit
```

## Dataset

**Fichier :** `Churn_Modelling.csv`

- **10 000 observations** de clients bancaires
- **14 colonnes :**

| Colonne         | Type    | Description                              |
|-----------------|---------|------------------------------------------|
| RowNumber       | int     | Index de la ligne                        |
| CustomerId      | int     | Identifiant unique du client             |
| Surname         | str     | Nom de famille                           |
| CreditScore     | int     | Score de credit (350 - 850)              |
| Geography       | str     | Pays (France, Germany, Spain)            |
| Gender          | str     | Genre (Male, Female)                     |
| Age             | int     | Age du client (18 - 92)                  |
| Tenure          | int     | Anciennete en annees (0 - 10)            |
| Balance         | float   | Solde du compte                          |
| NumOfProducts   | int     | Nombre de produits bancaires (1 - 4)     |
| HasCrCard       | int     | Possede une carte de credit (0/1)        |
| IsActiveMember  | int     | Membre actif (0/1)                       |
| EstimatedSalary | float   | Salaire estime                           |
| **Exited**      | **int** | **Variable cible : churn (0/1)**         |

- Taux de churn : **20.37%** (2 037 clients partis sur 10 000)
- Aucune valeur manquante

## Notebook d'analyse (`churn_analysis.ipynb`)

Le notebook est structure en 6 parties :

### 1. Analyse exploratoire (EDA)
- Statistiques descriptives et verification des donnees
- Distribution de la variable cible (desequilibre 80/20)
- Analyse du churn par pays (Allemagne a le taux le plus eleve) et par genre
- Histogrammes, box plots et pairplots des variables numeriques
- Matrice de correlation (Age et IsActiveMember = variables les plus correlees au churn)

### 2. Preprocessing
- Suppression des colonnes non pertinentes (RowNumber, CustomerId, Surname)
- LabelEncoder sur Gender (Female=0, Male=1)
- One-Hot Encoding sur Geography (drop_first → Geography_Germany, Geography_Spain)
- StandardScaler pour normalisation
- Split train/test 80/20 avec stratification

### 3. Modelisation baseline (5 modeles)
- K-Nearest Neighbors (KNN)
- Logistic Regression
- Decision Tree (avec visualisation de l'arbre)
- Random Forest (avec feature importance)
- XGBoost

### 4. Fine-tuning avec GridSearchCV
Optimisation par validation croisee (5 folds, scoring F1) sur les 3 meilleurs modeles :
- Logistic Regression
- Random Forest
- XGBoost

### 5. Selection de features (SelectKBest)
Tests avec K = 5, 7, 10 features utilisant f_classif. Les 5 features les plus discriminantes :
1. Age (score: 682)
2. Geography_Germany (score: 275)
3. IsActiveMember (score: 190)
4. Balance (score: 120)
5. Gender (score: 91)

### 6. Resultats et conclusion

| Modele                      | Accuracy | F1-Score | ROC-AUC |
|-----------------------------|----------|----------|---------|
| K-Nearest Neighbors         | 0.824    | 0.443    | 0.753   |
| Logistic Regression         | 0.808    | 0.284    | 0.775   |
| Decision Tree               | 0.856    | 0.531    | 0.842   |
| Random Forest               | 0.862    | 0.575    | 0.855   |
| **XGBoost (Tuned)**         | **0.862**| **0.587**| **0.859** |

**Modele selectionne : XGBoost (Tuned)** avec les parametres :
- `max_depth=3`, `learning_rate=0.3`, `n_estimators=100`
- `subsample=0.8`, `colsample_bytree=0.9`

## Backend (FastAPI)

### `prepare_model.py`
Script de preparation qui :
1. Charge et pretraite le dataset
2. Entraine le modele XGBoost avec GridSearchCV
3. Exporte 4 fichiers pickle : `model.pkl`, `scaler.pkl`, `label_encoder_gender.pkl`, `feature_names.pkl`

### `app.py`
API REST avec deux endpoints :

| Methode | Endpoint   | Description                                      |
|---------|-----------|--------------------------------------------------|
| GET     | `/`       | Verification que l'API est active                |
| POST    | `/predict`| Prediction du churn pour un client               |

Le body du POST accepte un JSON avec les features du client. Toutes les features ont des **valeurs par defaut** (medianes du dataset), donc un body `{}` vide est valide.

Exemple de requete :
```bash
curl -X POST http://localhost:8000/predict \
  -H "Content-Type: application/json" \
  -d '{"Age": 55, "Geography": "Germany", "IsActiveMember": 0}'
```

Exemple de reponse :
```json
{
  "prediction": 1,
  "churn_probability": 0.8234,
  "label": "Churned",
  "input_features": { ... }
}
```

Documentation interactive : **http://localhost:8000/docs** (Swagger UI)

## Frontend (Streamlit)

### `frontend.py`
Interface web avec deux pages :

**Page Prediction :**
- Formulaire interactif avec sliders, selectbox et number inputs
- Appel a l'API backend et affichage du resultat
- Jauge de risque de churn (Plotly)

**Page Dashboard :**
- 4 KPIs (total clients, restes, partis, taux de churn)
- 10 graphiques interactifs Plotly :
  - Repartition du churn (donut chart)
  - Taux de churn par pays, genre, nombre de produits, statut membre
  - Distributions age et credit score par statut
  - Box plot du solde par statut
  - Evolution du churn par anciennete
  - Matrice de correlation

## Installation et lancement

### Prerequis
- Python 3.9+

### Installation des dependances
```bash
cd "Churn Classification"
pip install -r requirements.txt
```

### Regenerer les fichiers pickle (optionnel)
```bash
cd backend
python prepare_model.py
```

### Lancer l'application

**Terminal 1 - Backend :**
```bash
cd backend
uvicorn app:app --host 127.0.0.1 --port 8000
```

**Terminal 2 - Frontend :**
```bash
cd frontend
streamlit run frontend.py
```

### Acces
| Service  | URL                          |
|----------|------------------------------|
| API      | http://localhost:8000         |
| Swagger  | http://localhost:8000/docs    |
| Frontend | http://localhost:8501         |

## Technologies utilisees

- **Analyse :** Pandas, NumPy, Matplotlib, Seaborn, Scikit-learn, XGBoost
- **Backend :** FastAPI, Uvicorn, Pydantic
- **Frontend :** Streamlit, Plotly
- **Serialisation :** Pickle
