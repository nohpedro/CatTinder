import { useEffect, useState } from "react";
import keycloak from "./keycloak";
import { SwipeForm } from "./components/SwipeForm";
import { ResultPanel } from "./components/ResultPanel";
import type { SwipeResponse } from "./types";

function App() {
    const [userA, setUserA] = useState("userA");
    const [userB, setUserB] = useState("userC");
    const [result, setResult] = useState<SwipeResponse | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // Si quieres hacer algo al montar, lo dejas aquí
    }, []);

    const handleLogout = () => {
        keycloak.logout({ redirectUri: window.location.origin });
    };

    const withAuth = async (): Promise<string | null> => {
        // Pequeña función para no repetir código de token
        await keycloak.updateToken(30);
        const freshToken = keycloak.token;

        if (!freshToken) {
            alert("No hay token disponible");
            return null;
        }
        return freshToken;
    };

    const sendSwipe = async () => {
        try {
            setLoading(true);
            setResult(null);

            const freshToken = await withAuth();
            if (!freshToken) return;

            const response = await fetch(
                `http://localhost:8083/swipe-service/api/v1/swipes/${userA}/${userB}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${freshToken}`,
                    },
                    body: JSON.stringify({ dir: "like" }),
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
        } catch (e) {
            console.error(e);
            setResult({ message: "Error llamando al backend" });
        } finally {
            setLoading(false);
        }
    };

    const checkMatch = async () => {
        try {
            setLoading(true);
            setResult(null);

            const freshToken = await withAuth();
            if (!freshToken) return;

            const response = await fetch(
                `http://localhost:8083/swipe-service/api/v1/swipes/matches/${userA}/${userB}`,
                {
                    method: "GET",
                    headers: {
                        Accept: "application/json",
                        Authorization: `Bearer ${freshToken}`,
                    },
                }
            );

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                setResult({
                    message: `Error ${response.status}: ${response.statusText}`,
                    ...data,
                });
            } else {
                // Aquí normalmente vendrá algo como { match: true/false, ... }
                setResult(data);
            }
        } catch (e) {
            console.error(e);
            setResult({ message: "Error llamando al backend (match)" });
        } finally {
            setLoading(false);
        }
    };

    const username = keycloak.tokenParsed?.preferred_username;

    return (
        <div
            style={{
                fontFamily: "'Press Start 2P', monospace",
                minHeight: "100vh",
                display: "flex",
                justifyContent: "center", // CENTRA horizontal
                alignItems: "center", // CENTRA vertical
                background: "linear-gradient(135deg, #0d0d0d, #1a1a2e 40%, #2a004f)",
                padding: "2rem",
                color: "#f8f8f2",
            }}
        >
            <div
                style={{
                    width: "95%",
                    maxWidth: "550px",
                    padding: "2rem",
                    borderRadius: "12px",
                    background: "rgba(20, 20, 40, 0.7)",
                    border: "2px solid #ff00ff44",
                    boxShadow: "0 0 20px #ff00ff55, inset 0 0 10px #55005544",
                    backdropFilter: "blur(8px)",
                }}
            >
                <h2
                    style={{
                        textAlign: "center",
                        marginBottom: "1.5rem",
                        color: "#ff77ff",
                        textShadow: "0 0 8px #ff00ff",
                        fontSize: "1.3rem",
                    }}
                >
                    UCB Dating App - Swipes
                </h2>

                <p style={{ textAlign: "center", marginBottom: "1rem" }}>
                    Sesión iniciada como{" "}
                    <strong style={{ color: "#00eaff" }}>{username}</strong>
                </p>

                <button
                    onClick={handleLogout}
                    style={{
                        width: "100%",
                        padding: "0.6rem",
                        marginBottom: "1.5rem",
                        borderRadius: 6,
                        border: "none",
                        background: "#ff0066",
                        color: "#fff",
                        fontWeight: "bold",
                        cursor: "pointer",
                        textShadow: "0 0 4px #000",
                        boxShadow: "0 0 10px #ff006688",
                    }}
                >
                    Cerrar sesión
                </button>

                <hr style={{ borderColor: "#444", margin: "1.5rem 0" }} />

                <SwipeForm
                    userA={userA}
                    userB={userB}
                    loading={loading}
                    onChangeUserA={setUserA}
                    onChangeUserB={setUserB}
                    onSend={sendSwipe}
                    onCheckMatch={checkMatch}
                />

                <hr style={{ borderColor: "#444", margin: "1.5rem 0" }} />

                <ResultPanel result={result} />
            </div>
        </div>
    );


}

export default App;
