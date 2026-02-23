"""
Streamlit frontend for Churn Prediction.
Connects to the FastAPI backend at localhost:8000.
Includes a prediction form and an EDA dashboard.
"""

import streamlit as st
import requests
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
from plotly.subplots import make_subplots

API_URL = "http://127.0.0.1:8000"

# ── Page config ───────────────────────────────────────────────────────────────
st.set_page_config(
    page_title="Churn Prediction",
    page_icon="📊",
    layout="wide",
)

# ── Load dataset (cached) ────────────────────────────────────────────────────
@st.cache_data
def load_data():
    return pd.read_csv("../Churn_Modelling.csv")

df = load_data()

# ── Sidebar navigation ───────────────────────────────────────────────────────
st.sidebar.title("Navigation")
page = st.sidebar.radio("Page", ["Prediction", "Dashboard"], label_visibility="collapsed")

# ══════════════════════════════════════════════════════════════════════════════
#  PREDICTION PAGE
# ══════════════════════════════════════════════════════════════════════════════
if page == "Prediction":
    st.title("Prediction du Churn Client")
    st.markdown("Remplissez les informations du client puis cliquez sur **Predire**.")

    col1, col2, col3 = st.columns(3)

    with col1:
        st.subheader("Informations personnelles")
        gender = st.selectbox("Genre", ["Female", "Male"])
        age = st.slider("Age", 18, 92, 37)
        geography = st.selectbox("Pays", ["France", "Germany", "Spain"])

    with col2:
        st.subheader("Compte bancaire")
        credit_score = st.number_input("Credit Score", 300, 900, 652)
        balance = st.number_input("Solde du compte", 0.0, 300000.0, 97198.54, step=1000.0)
        estimated_salary = st.number_input("Salaire estime", 0.0, 200000.0, 100193.91, step=1000.0)

    with col3:
        st.subheader("Relation client")
        tenure = st.slider("Anciennete (annees)", 0, 10, 5)
        num_products = st.selectbox("Nombre de produits", [1, 2, 3, 4], index=0)
        has_cr_card = st.selectbox("Carte de credit", [("Oui", 1), ("Non", 0)], format_func=lambda x: x[0])
        is_active = st.selectbox("Membre actif", [("Oui", 1), ("Non", 0)], format_func=lambda x: x[0])

    st.markdown("---")

    if st.button("Predire", type="primary", use_container_width=True):
        payload = {
            "CreditScore": credit_score,
            "Geography": geography,
            "Gender": gender,
            "Age": age,
            "Tenure": tenure,
            "Balance": balance,
            "NumOfProducts": num_products,
            "HasCrCard": has_cr_card[1],
            "IsActiveMember": is_active[1],
            "EstimatedSalary": estimated_salary,
        }

        try:
            resp = requests.post(f"{API_URL}/predict", json=payload, timeout=10)
            resp.raise_for_status()
            result = resp.json()

            prob = result["churn_probability"]
            label = result["label"]

            # Result cards
            r1, r2, r3 = st.columns(3)
            r1.metric("Prediction", label)
            r2.metric("Probabilite de Churn", f"{prob:.1%}")
            r3.metric("Confiance", f"{max(prob, 1 - prob):.1%}")

            # Gauge chart
            fig_gauge = go.Figure(go.Indicator(
                mode="gauge+number",
                value=prob * 100,
                title={"text": "Risque de Churn (%)"},
                gauge={
                    "axis": {"range": [0, 100]},
                    "bar": {"color": "#e74c3c" if prob > 0.5 else "#2ecc71"},
                    "steps": [
                        {"range": [0, 30], "color": "#d5f5e3"},
                        {"range": [30, 60], "color": "#fdebd0"},
                        {"range": [60, 100], "color": "#fadbd8"},
                    ],
                    "threshold": {"line": {"color": "black", "width": 3}, "thickness": 0.8, "value": 50},
                },
            ))
            fig_gauge.update_layout(height=300)
            st.plotly_chart(fig_gauge, use_container_width=True)

        except requests.exceptions.ConnectionError:
            st.error("Impossible de se connecter au backend. Verifiez que le serveur tourne sur localhost:8000.")
        except Exception as e:
            st.error(f"Erreur: {e}")

# ══════════════════════════════════════════════════════════════════════════════
#  DASHBOARD PAGE
# ══════════════════════════════════════════════════════════════════════════════
else:
    st.title("Dashboard - Analyse du Churn")

    # ── KPIs ──────────────────────────────────────────────────────────────────
    total = len(df)
    churned = df["Exited"].sum()
    stayed = total - churned
    churn_rate = churned / total

    k1, k2, k3, k4 = st.columns(4)
    k1.metric("Total Clients", f"{total:,}")
    k2.metric("Clients Restes", f"{stayed:,}")
    k3.metric("Clients Partis", f"{churned:,}")
    k4.metric("Taux de Churn", f"{churn_rate:.1%}")

    st.markdown("---")

    # ── Row 1: Churn distribution + Geography ─────────────────────────────────
    c1, c2 = st.columns(2)

    with c1:
        fig_pie = px.pie(
            df, names=df["Exited"].map({0: "Reste", 1: "Parti"}),
            color_discrete_sequence=["#2ecc71", "#e74c3c"],
            title="Repartition du Churn",
            hole=0.4,
        )
        st.plotly_chart(fig_pie, use_container_width=True)

    with c2:
        geo_churn = df.groupby("Geography")["Exited"].mean().reset_index()
        geo_churn.columns = ["Pays", "Taux de Churn"]
        fig_geo = px.bar(
            geo_churn, x="Pays", y="Taux de Churn",
            color="Pays",
            color_discrete_sequence=["#3498db", "#e67e22", "#9b59b6"],
            title="Taux de Churn par Pays",
            text_auto=".1%",
        )
        fig_geo.update_layout(showlegend=False)
        st.plotly_chart(fig_geo, use_container_width=True)

    # ── Row 2: Age distribution + Gender ──────────────────────────────────────
    c3, c4 = st.columns(2)

    with c3:
        fig_age = px.histogram(
            df, x="Age", color=df["Exited"].map({0: "Reste", 1: "Parti"}),
            barmode="overlay", nbins=30,
            color_discrete_sequence=["#2ecc71", "#e74c3c"],
            title="Distribution de l'Age par Statut",
            labels={"color": "Statut"},
        )
        fig_age.update_layout(bargap=0.1)
        st.plotly_chart(fig_age, use_container_width=True)

    with c4:
        gender_churn = df.groupby("Gender")["Exited"].mean().reset_index()
        gender_churn.columns = ["Genre", "Taux de Churn"]
        fig_gender = px.bar(
            gender_churn, x="Genre", y="Taux de Churn",
            color="Genre",
            color_discrete_sequence=["#e74c3c", "#3498db"],
            title="Taux de Churn par Genre",
            text_auto=".1%",
        )
        fig_gender.update_layout(showlegend=False)
        st.plotly_chart(fig_gender, use_container_width=True)

    # ── Row 3: Balance + NumProducts ──────────────────────────────────────────
    c5, c6 = st.columns(2)

    with c5:
        fig_bal = px.box(
            df, x=df["Exited"].map({0: "Reste", 1: "Parti"}), y="Balance",
            color=df["Exited"].map({0: "Reste", 1: "Parti"}),
            color_discrete_sequence=["#2ecc71", "#e74c3c"],
            title="Solde par Statut de Churn",
            labels={"x": "Statut"},
        )
        fig_bal.update_layout(showlegend=False)
        st.plotly_chart(fig_bal, use_container_width=True)

    with c6:
        prod_churn = df.groupby("NumOfProducts")["Exited"].mean().reset_index()
        prod_churn.columns = ["Nb Produits", "Taux de Churn"]
        fig_prod = px.bar(
            prod_churn, x="Nb Produits", y="Taux de Churn",
            color="Taux de Churn",
            color_continuous_scale="RdYlGn_r",
            title="Taux de Churn par Nombre de Produits",
            text_auto=".1%",
        )
        st.plotly_chart(fig_prod, use_container_width=True)

    # ── Row 4: Correlation heatmap + Active members ───────────────────────────
    c7, c8 = st.columns(2)

    with c7:
        numeric_cols = ["CreditScore", "Age", "Tenure", "Balance", "NumOfProducts",
                        "HasCrCard", "IsActiveMember", "EstimatedSalary", "Exited"]
        corr = df[numeric_cols].corr()
        fig_corr = px.imshow(
            corr, text_auto=".2f", color_continuous_scale="RdBu_r",
            title="Matrice de Correlation",
            aspect="auto",
        )
        st.plotly_chart(fig_corr, use_container_width=True)

    with c8:
        active_churn = df.groupby("IsActiveMember")["Exited"].mean().reset_index()
        active_churn["IsActiveMember"] = active_churn["IsActiveMember"].map({0: "Inactif", 1: "Actif"})
        active_churn.columns = ["Statut Membre", "Taux de Churn"]
        fig_active = px.bar(
            active_churn, x="Statut Membre", y="Taux de Churn",
            color="Statut Membre",
            color_discrete_sequence=["#e74c3c", "#2ecc71"],
            title="Taux de Churn: Membres Actifs vs Inactifs",
            text_auto=".1%",
        )
        fig_active.update_layout(showlegend=False)
        st.plotly_chart(fig_active, use_container_width=True)

    # ── Row 5: Tenure + Credit Score ──────────────────────────────────────────
    c9, c10 = st.columns(2)

    with c9:
        tenure_churn = df.groupby("Tenure")["Exited"].mean().reset_index()
        tenure_churn.columns = ["Anciennete", "Taux de Churn"]
        fig_tenure = px.line(
            tenure_churn, x="Anciennete", y="Taux de Churn",
            markers=True,
            title="Taux de Churn par Anciennete",
            color_discrete_sequence=["#e74c3c"],
        )
        st.plotly_chart(fig_tenure, use_container_width=True)

    with c10:
        fig_cs = px.histogram(
            df, x="CreditScore", color=df["Exited"].map({0: "Reste", 1: "Parti"}),
            barmode="overlay", nbins=30,
            color_discrete_sequence=["#2ecc71", "#e74c3c"],
            title="Distribution du Credit Score par Statut",
            labels={"color": "Statut"},
        )
        st.plotly_chart(fig_cs, use_container_width=True)
