import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import keycloak from "./keycloak";

import "./index.css";

keycloak
    .init({
        onLoad: "login-required",  // fuerza login
        pkceMethod: "S256",        // Authorization Code + PKCE
    })
    .then((authenticated) => {
        if (!authenticated) {
            console.warn("No autenticado!");
            keycloak.login();
        } else {
            console.log("Autenticado con Keycloak ✅");
            ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
                <React.StrictMode>
                    <App />
                </React.StrictMode>
            );
        }
    })
    .catch((err) => {
        console.error("Error inicializando Keycloak", err);
    });
