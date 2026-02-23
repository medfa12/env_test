"""
Prepare pickle files for the Churn Prediction backend.
Loads the dataset, preprocesses it, trains the best model (XGBoost),
and exports the scaler and model as pickle files.
"""

import pandas as pd
import numpy as np
import pickle
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import StandardScaler, LabelEncoder
from xgboost import XGBClassifier
from sklearn.metrics import f1_score, roc_auc_score, accuracy_score, classification_report

# ── 1. Load dataset ──────────────────────────────────────────────────────────
df = pd.read_csv("../Churn_Modelling.csv")
print(f"Dataset loaded: {df.shape}")

# ── 2. Preprocessing ─────────────────────────────────────────────────────────
# Drop irrelevant columns
df_clean = df.drop(columns=["RowNumber", "CustomerId", "Surname"])

# Label encode Gender
le_gender = LabelEncoder()
df_clean["Gender"] = le_gender.fit_transform(df_clean["Gender"])

# One-Hot encode Geography (drop_first to avoid multicollinearity)
df_clean = pd.get_dummies(df_clean, columns=["Geography"], prefix="Geography", drop_first=True)

# Ensure boolean columns are int
for col in df_clean.columns:
    if df_clean[col].dtype == bool:
        df_clean[col] = df_clean[col].astype(int)

# Separate features and target
X = df_clean.drop("Exited", axis=1)
y = df_clean["Exited"]

feature_names = X.columns.tolist()
print(f"Features ({len(feature_names)}): {feature_names}")

# Split
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42, stratify=y
)

# Scale
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# ── 3. Train XGBoost with GridSearchCV ────────────────────────────────────────
print("\nRunning GridSearchCV for XGBoost...")
param_grid = {
    "n_estimators": [100, 200, 300],
    "max_depth": [3, 5, 7, 9],
    "learning_rate": [0.01, 0.1, 0.3],
    "subsample": [0.8, 0.9, 1.0],
    "colsample_bytree": [0.8, 0.9, 1.0],
}

grid_search = GridSearchCV(
    estimator=XGBClassifier(random_state=42, n_jobs=-1, eval_metric="logloss"),
    param_grid=param_grid,
    cv=5,
    scoring="f1",
    n_jobs=-1,
    verbose=1,
)
grid_search.fit(X_train_scaled, y_train)

best_model = grid_search.best_estimator_
print(f"\nBest params: {grid_search.best_params_}")
print(f"Best CV F1: {grid_search.best_score_:.4f}")

# ── 4. Evaluate ──────────────────────────────────────────────────────────────
y_pred = best_model.predict(X_test_scaled)
y_proba = best_model.predict_proba(X_test_scaled)[:, 1]

print(f"\nTest Accuracy: {accuracy_score(y_test, y_pred):.4f}")
print(f"Test F1-Score: {f1_score(y_test, y_pred):.4f}")
print(f"Test ROC-AUC:  {roc_auc_score(y_test, y_proba):.4f}")
print("\nClassification Report:")
print(classification_report(y_test, y_pred, target_names=["Stayed", "Churned"]))

# ── 5. Save artifacts ────────────────────────────────────────────────────────
with open("scaler.pkl", "wb") as f:
    pickle.dump(scaler, f)
print("Saved: scaler.pkl")

with open("model.pkl", "wb") as f:
    pickle.dump(best_model, f)
print("Saved: model.pkl")

with open("label_encoder_gender.pkl", "wb") as f:
    pickle.dump(le_gender, f)
print("Saved: label_encoder_gender.pkl")

with open("feature_names.pkl", "wb") as f:
    pickle.dump(feature_names, f)
print("Saved: feature_names.pkl")

print("\nAll artifacts saved successfully!")
