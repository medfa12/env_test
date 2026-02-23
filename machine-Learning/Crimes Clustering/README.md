# Crime Clustering Analysis - USA

## Description du projet

Ce projet applique des techniques de **clustering non supervisé** pour segmenter les 50 États américains selon leurs statistiques de criminalité. L'objectif est d'identifier des profils d'États partageant des tendances criminelles similaires, afin de mieux comprendre les disparités géographiques en matière de sécurité publique.

## Données

**Fichier :** `crimes.csv` (séparateur `;`)

- **50 observations** (les 50 États américains)
- **8 colonnes :**

| Colonne      | Description                          |
|-------------|--------------------------------------|
| `Etat`       | Nom de l'État                       |
| `Meutre`     | Taux de meurtres (pour 100 000 hab.) |
| `Rapt`       | Taux d'enlèvements                  |
| `Vol`        | Taux de vols (robbery)              |
| `Attaque`    | Taux d'agressions                   |
| `Viol`       | Taux de cambriolages (burglary)     |
| `Larcin`     | Taux de larcins                     |
| `Auto_Theft` | Taux de vols de véhicules           |

Aucune valeur manquante dans le jeu de données.

## Contenu du notebook

Le notebook `crimes_clustering_analysis.ipynb` est structuré en 9 sections :

### 1. Chargement et préparation des données
Lecture du CSV, vérification de la structure, des types et des valeurs manquantes.

### 2. Analyse exploratoire (EDA)
- Statistiques descriptives
- Distributions de chaque variable (histogrammes avec moyenne/médiane)
- Matrice de corrélation (heatmap)
- Box plots pour la détection d'outliers
- Pairplot des relations entre variables

### 3. Réduction de dimensionnalité
Projection en 2D pour visualisation, via trois méthodes :
- **PCA** (Principal Component Analysis) + scree plot de la variance expliquée
- **UMAP** (Uniform Manifold Approximation and Projection)
- **t-SNE** (t-Distributed Stochastic Neighbor Embedding)

### 4. K-Means Clustering
- **Méthode du coude** (Elbow) avec détection automatique via `KneeLocator` (k optimal = 5)
- **Analyse des scores silhouette** pour k = 2 à 10
- Modèle final avec **k = 4** clusters (silhouette = 0.2836)
- Visualisation des clusters sur PCA, UMAP et t-SNE

### 5. Profilage des clusters
- Centroïdes des clusters (échelle originale) + heatmap
- Profil détaillé de chaque cluster avec la liste des États et statistiques
- Export des résultats en CSV (`cluster_assignments.csv`, `cluster_centroids.csv`, `cluster_statistics.csv`, `cluster_summary.csv`)
- **Rapport de profiling** détaillé avec interprétation métier des 4 profils identifiés

### 6. Extraction de règles par arbre de décision
- Entraînement d'un `DecisionTreeClassifier` sur les labels K-Means
- Visualisation de l'arbre de décision
- Importance des variables (top features : Larcin, Attaque, Meutre)

### 7. Clustering hiérarchique
- Dendrogramme (linkage de Ward)
- `AgglomerativeClustering` avec k = 4 (silhouette = 0.2330)
- Visualisation sur PCA, UMAP, t-SNE

### 8. Gaussian Mixture Model (GMM)
- Sélection du nombre de composantes via BIC/AIC
- Modèle GMM avec 4 composantes (silhouette = 0.2114)
- Heatmap des probabilités d'appartenance
- Visualisation sur PCA, UMAP, t-SNE

### 9. Comparaison des méthodes
- Tableau comparatif des assignations (K-Means vs Hiérarchique vs GMM)
- Comparaison des scores silhouette : **K-Means** obtient le meilleur score (0.2836)

## Résultats clés

| Cluster | Profil | États typiques | Caractéristique principale |
|---------|--------|---------------|---------------------------|
| 0 | Forte criminalité urbaine | Californie, Floride, New-York | Taux élevés sur tous les indicateurs |
| 1 | Zones rurales sûres | Iowa, Vermont, Dakota du Nord | Tous les indicateurs au minimum |
| 2 | Sud violent | Louisiane, Mississippi, Alabama | Meurtres les plus élevés (~10.5) |
| 3 | Nord-Est modéré | Connecticut, Massachusetts | Faible violence, vols modérés |

## Technologies utilisées

- Python 3, Jupyter Notebook
- **Librairies :** NumPy, Pandas, Matplotlib, Seaborn, Scikit-learn, UMAP-learn, SciPy, Kneed
