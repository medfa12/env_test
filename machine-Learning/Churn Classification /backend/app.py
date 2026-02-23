"""
FastAPI backend for Churn Prediction.
Loads the pre-trained XGBoost model and scaler, exposes a /predict endpoint.
"""

import pickle
import os
import numpy as np
import pandas as pd
from fastapi import FastAPI
from pydantic import BaseModel, Field

# ── Load artifacts (resolve paths relative to this file) ──────────────────────
BASE_DIR = os.path.dirname(os.path.abspath(__file__))

with open(os.path.join(BASE_DIR, "model.pkl"), "rb") as f:
    model = pickle.load(f)

with open(os.path.join(BASE_DIR, "scaler.pkl"), "rb") as f:
    scaler = pickle.load(f)

with open(os.path.join(BASE_DIR, "label_encoder_gender.pkl"), "rb") as f:
    le_gender = pickle.load(f)

with open(os.path.join(BASE_DIR, "feature_names.pkl"), "rb") as f:
    feature_names = pickle.load(f)

# ── Pydantic schema with defaults (median/mode from the dataset) ─────────────
class CustomerInput(BaseModel):
    CreditScore: int = Field(default=652, ge=300, le=900, description="Credit score of the customer")
    Geography: str = Field(default="France", description="Country: France, Germany, or Spain")
    Gender: str = Field(default="Female", description="Gender: Male or Female")
    Age: int = Field(default=37, ge=18, le=100, description="Age of the customer")
    Tenure: int = Field(default=5, ge=0, le=10, description="Number of years as a client")
    Balance: float = Field(default=97198.54, ge=0, description="Account balance")
    NumOfProducts: int = Field(default=1, ge=1, le=4, description="Number of bank products used")
    HasCrCard: int = Field(default=1, ge=0, le=1, description="Has a credit card (0 or 1)")
    IsActiveMember: int = Field(default=1, ge=0, le=1, description="Is an active member (0 or 1)")
    EstimatedSalary: float = Field(default=100193.91, ge=0, description="Estimated salary")


# ── FastAPI app ───────────────────────────────────────────────────────────────
app = FastAPI(
    title="Churn Prediction API",
    description="Predict whether a bank customer will churn using a tuned XGBoost model.",
    version="1.0.0",
)


@app.get("/")
def root():
    return {"message": "Churn Prediction API is running. Go to /docs for Swagger UI."}


@app.post("/predict")
def predict(customer: CustomerInput):
    """Predict churn probability for a single customer."""

    # Encode Gender
    gender_encoded = le_gender.transform([customer.Gender])[0]

    # Build feature vector in the same order as training
    data = {
        "CreditScore": customer.CreditScore,
        "Gender": int(gender_encoded),
        "Age": customer.Age,
        "Tenure": customer.Tenure,
        "Balance": customer.Balance,
        "NumOfProducts": customer.NumOfProducts,
        "HasCrCard": customer.HasCrCard,
        "IsActiveMember": customer.IsActiveMember,
        "EstimatedSalary": customer.EstimatedSalary,
        "Geography_Germany": 1 if customer.Geography == "Germany" else 0,
        "Geography_Spain": 1 if customer.Geography == "Spain" else 0,
    }

    # Create DataFrame with correct column order
    df_input = pd.DataFrame([data])[feature_names]

    # Scale
    X_scaled = scaler.transform(df_input)

    # Predict
    prediction = int(model.predict(X_scaled)[0])
    probability = float(model.predict_proba(X_scaled)[0][1])

    return {
        "prediction": prediction,
        "churn_probability": round(probability, 4),
        "label": "Churned" if prediction == 1 else "Stayed",
        "input_features": data,
    }
