import { useEffect, useState } from "react";
import keycloak from "../keycloak";
import { SwipeForm } from "./components/SwipeForm";
import { ResultPanel } from "./components/ResultPanel";
import type { SwipeResponse } from "./types";
import { sendSwipeRequest, checkMatchRequest } from "./api";
import { useNavigate } from "react-router-dom";

export default function SwipesPage() {
    const [userA, setUserA] = useState("userA");
    const [userB, setUserB] = useState("userC");
    const [result, setResult] = useState<SwipeResponse | null>(null);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        // Si quieres hacer algo al montar, lo dejas aquí
    }, []);

    const handleLogout = () => {
        keycloak.logout({ redirectUri: window.location.origin });
    };

    const withAuth = async (): Promise<string | null> => {
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

            const data = await sendSwipeRequest(userA, userB, freshToken);
            setResult(data);
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

            const data = await checkMatchRequest(userA, userB, freshToken);
            setResult(data);
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
                justifyContent: "center",
                alignItems: "center",
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

                {/* ✅ BOTÓN VOLVER AL MENÚ (va dentro del return) */}
                <button
                    onClick={() => navigate("/menu")}
                    style={{
                        width: "100%",
                        padding: "0.6rem",
                        marginBottom: "0.9rem",
                        borderRadius: 6,
                        border: "none",
                        background: "#111",
                        color: "#fff",
                        fontWeight: "bold",
                        cursor: "pointer",
                        textShadow: "0 0 4px #000",
                        boxShadow: "0 0 10px #00eaff88",
                    }}
                >
                    Volver al menú
                </button>

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
