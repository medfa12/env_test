# Prédiction des Prix Immobiliers — Ames, Iowa

Projet de régression supervisée pour prédire le prix de vente de maisons résidentielles à partir de 80 variables décrivant leurs caractéristiques physiques, structurelles et contextuelles.

---

## Jeu de données

Les données proviennent du dataset **Ames Housing** (Kaggle). Chaque ligne représente une vente immobilière réalisée entre 2006 et 2010 dans la ville d'Ames, Iowa.

| Fichier | Contenu |
|---|---|
| `train.csv` | 1 460 ventes avec `SalePrice` (variable cible) |
| `test.csv` | 1 459 ventes sans `SalePrice` (jeu de soumission) |
| `data_description.txt` | Description détaillée de chaque variable |

### Structure des variables

- **80 variables explicatives** : 36 numériques, 43 catégorielles (+ `Id`)
- **Variable cible** : `SalePrice` (prix de vente en dollars)

Les variables couvrent 7 grandes thématiques :

| Thématique | Exemples |
|---|---|
| Localisation | `Neighborhood`, `MSZoning`, `Condition1` |
| Terrain | `LotArea`, `LotShape`, `LotConfig` |
| Structure de la maison | `HouseStyle`, `YearBuilt`, `OverallQual`, `OverallCond` |
| Sous-sol | `TotalBsmtSF`, `BsmtQual`, `BsmtFinType1` |
| Surfaces | `GrLivArea`, `1stFlrSF`, `2ndFlrSF`, `GarageArea` |
| Équipements | `Fireplaces`, `PoolArea`, `CentralAir`, `KitchenQual` |
| Vente | `YrSold`, `MoSold`, `SaleType`, `SaleCondition` |

### Valeurs manquantes notables

Cinq variables ont un taux de valeurs manquantes élevé, mais celles-ci correspondent en réalité à l'**absence** de la caractéristique (ex. pas de piscine, pas d'allée) :

| Variable | Manquant | Interprétation |
|---|---|---|
| `PoolQC` | 99.5 % | Pas de piscine |
| `MiscFeature` | 96.3 % | Pas d'équipement divers |
| `Alley` | 93.8 % | Pas d'accès par ruelle |
| `Fence` | 80.8 % | Pas de clôture |
| `MasVnrType` | 59.7 % | Pas de parement en maçonnerie |

---

## Analyse exploratoire (`eda.ipynb`)

### Variable cible

| Statistique | Valeur |
|---|---|
| Prix moyen | 180 921 $ |
| Prix médian | 163 000 $ |
| Écart-type | 79 443 $ |
| Min / Max | 34 900 $ / 755 000 $ |
| Asymétrie (skewness) | 1.88 → distribution droitière |

La distribution est fortement asymétrique. Une **transformation logarithmique** est appliquée avant la modélisation (skewness ramenée à 0.12).

### Variables les plus corrélées avec le prix

| Variable | Corrélation | Description |
|---|---|---|
| `OverallQual` | 0.791 | Qualité générale des matériaux et finitions |
| `GrLivArea` | 0.709 | Surface habitable hors-sol (sq ft) |
| `GarageCars` | 0.640 | Capacité du garage (nombre de voitures) |
| `GarageArea` | 0.623 | Surface du garage (sq ft) |
| `TotalBsmtSF` | 0.614 | Surface totale du sous-sol (sq ft) |

### Quartiers

- Quartier le plus cher : **NoRidge** (335 295 $ en moyenne)
- Quartier le moins cher : **MeadowV** (98 576 $ en moyenne)
- L'écart maximal entre quartiers dépasse **3×** le prix minimum

### Tendances temporelles

- Période couverte : 2006–2010
- Légère baisse des prix sur la période : **−2.8 %** (182 549 $ en 2006 → 177 394 $ en 2010)
- Saisonnalité : mois de septembre le plus cher (195 683 $), avril le moins cher (171 503 $)

---

## Modélisation (`prices_regression.ipynb`)

### Pipeline de préparation

1. **Imputation** : médiane pour les variables numériques, mode pour les catégorielles
2. **Encodage** : `pd.get_dummies` sur les 43 variables catégorielles → 244 variables après encodage
3. **Suppression outliers** : maisons avec `GrLivArea > 4 000 sq ft` et `SalePrice ≤ 300 000 $` retirées (2 observations)
4. **Transformation cible** : `log1p(SalePrice)`
5. **Normalisation** : `StandardScaler`
6. **Split** : 80 % entraînement (1 166 obs.) / 20 % test (292 obs.)

### Sélection de variables

Deux méthodes testées en plus du jeu complet :

| Méthode | Variables retenues | Réduction |
|---|---|---|
| Toutes les variables | 244 | — |
| RFE (Recursive Feature Elimination) | 50 | −79.5 % |
| Backward Elimination (p-value < 0.05) | 83 | −66.0 % |

### Résultats — 8 modèles × 3 jeux de variables (R² test)

| Modèle | Toutes | RFE | BE | Meilleur |
|---|---|---|---|---|
| **ElasticNet** | 0.9028 | **0.9067** | 0.9058 | 0.9067 |
| Lasso | 0.9014 | 0.9051 | 0.9034 | 0.9051 |
| Ridge | 0.8976 | 0.9034 | 0.9008 | 0.9034 |
| XGBoost | 0.8923 | 0.8938 | **0.8942** | 0.8942 |
| Linear | 0.8923 | 0.8920 | 0.8909 | 0.8923 |
| Random Forest | 0.8742 | 0.8735 | 0.8697 | 0.8742 |
| SVR | 0.7525 | 0.8475 | 0.8166 | 0.8475 |
| Decision Tree | 0.7690 | 0.7806 | 0.7491 | 0.7806 |

### Modèle champion

**ElasticNet + RFE** (50 variables)
- R² test : **0.9067**
- RMSE test : **0.1254** (sur log-prix)
- MAE test : **0.0881**
- Hyperparamètres : `alpha=0.001`, `l1_ratio=0.5`

### Observations clés

- La sélection par **RFE** améliore systématiquement les modèles linéaires régularisés (Lasso, Ridge, ElasticNet).
- **XGBoost** et **Random Forest** sont robustes avec toutes les variables et ne bénéficient pas significativement de la sélection.
- Le **SVR** est très sensible au jeu de variables (R² de 0.75 → 0.85 avec RFE).
- La **Decision Tree** seule souffre de variance élevée sans pruning agressif.
