import { useEffect, useState } from "react";
import keycloak from "./keycloak";

type SwipeResponse = {
    message?: string;
    match?: boolean;
    [key: string]: any;
};

function App() {
    const [userA, setUserA] = useState("userA");
    const [userB, setUserB] = useState("userC");
    const [token, setToken] = useState<string | null>(null);
    const [result, setResult] = useState<SwipeResponse | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // Guardamos el token actual al montar
        setToken(keycloak.token ?? null);
    }, []);

    const handleLogout = () => {
        keycloak.logout({ redirectUri: window.location.origin });
    };

    const sendSwipe = async () => {
        try {
            setLoading(true);
            setResult(null);

            // Refrescar token si está por expirar (30s)
            await keycloak.updateToken(30);
            const freshToken = keycloak.token;
            setToken(freshToken ?? null);

            if (!freshToken) {
                alert("No hay token disponible");
                return;
            }

            // Ajusta el método/URL si tu endpoint es distinto
            const response = await fetch(
                `http://localhost:8082/swipe-service/api/v1/swipes/${userA}/${userB}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${freshToken}`,
                    },
                    // body: JSON.stringify({ ... })  // si tu endpoint espera body
                }
            );

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                setResult({
                    message: `Error ${response.status}: ${response.statusText}`,
                    ...data,
                });
            } else {
                setResult(data);
            }
        } catch (e: any) {
            console.error(e);
            setResult({ message: "Error llamando al backend" });
        } finally {
            setLoading(false);
        }
    };

    const username = keycloak.tokenParsed?.preferred_username;

    return (
        <div
            style={{
                fontFamily: "sans-serif",
                maxWidth: 500,
                margin: "2rem auto",
                padding: "1.5rem",
                border: "1px solid #ddd",
                borderRadius: 8,
            }}
        >
            <h2>UCB Dating App - Swipes</h2>

            <p>
                Sesión iniciada como <strong>{username}</strong>
            </p>

            <button onClick={handleLogout} style={{ marginBottom: "1rem" }}>
                Cerrar sesión
            </button>

            <hr />

            <h3>Enviar swipe</h3>

            <div style={{ marginBottom: "0.5rem" }}>
                <label>
                    Usuario A:{" "}
                    <input
                        value={userA}
                        onChange={(e) => setUserA(e.target.value)}
                        style={{ width: "100%" }}
                    />
                </label>
            </div>

            <div style={{ marginBottom: "0.5rem" }}>
                <label>
                    Usuario B:{" "}
                    <input
                        value={userB}
                        onChange={(e) => setUserB(e.target.value)}
                        style={{ width: "100%" }}
                    />
                </label>
            </div>

            <button onClick={sendSwipe} disabled={loading}>
                {loading ? "Enviando..." : "Enviar swipe"}
            </button>

            <hr />

            <h4>Resultado</h4>
            {result ? (
                <pre
                    style={{
                        background: "#f5f5f5",
                        padding: "0.75rem",
                        borderRadius: 4,
                        fontSize: "0.85rem",
                    }}
                >
          {JSON.stringify(result, null, 2)}
        </pre>
            ) : (
                <p>No se ha enviado ninguna petición aún.</p>
            )}

            <hr />
            <h4>Token (recortado)</h4>
            <p style={{ fontSize: "0.75rem", wordBreak: "break-all" }}>
                {token
                    ? token.substring(0, 40) + "..."
                    : "No hay token cargado (algo salió mal en el login)."}
            </p>
        </div>
    );
}

export default App;
