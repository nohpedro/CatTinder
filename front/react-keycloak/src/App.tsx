import { useState } from "react";
import keycloak from "./keycloak";

type ConfigPayload = {
    userid: string;
    darkmode: boolean;
    showInfo: boolean;
    showOnlineStatus: boolean;
    showNotifications: boolean;
};

type ConfigResponse = ConfigPayload & {
    message?: string;
};

function App() {
    const username = keycloak.tokenParsed?.preferred_username ?? "u123";

    const [userId, setUserId] = useState(username);
    const [darkmode, setDarkmode] = useState(true);
    const [showInfo, setShowInfo] = useState(true);
    const [showOnlineStatus, setShowOnlineStatus] = useState(false);
    const [showNotifications, setShowNotifications] = useState(true);

    const [result, setResult] = useState<ConfigResponse | { message: string } | null>(null);
    const [loading, setLoading] = useState(false);

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

    const sendConfig = async () => {
        try {
            setLoading(true);
            setResult(null);

            const freshToken = await withAuth();
            if (!freshToken) return;

            const payload: ConfigPayload = {
                userid: userId,
                darkmode,
                showInfo,
                showOnlineStatus,
                showNotifications,
            };

            const response = await fetch(
                `http://localhost:8082/confg-ms/api/v1/config/${userId}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${freshToken}`,
                    },
                    body: JSON.stringify(payload),
                }
            );

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                setResult({
                    message: `Error ${response.status}: ${response.statusText}`,
                    ...(data as any),
                });
            } else {
                setResult(data as ConfigResponse);
            }
        } catch (e) {
            console.error(e);
            setResult({ message: "Error llamando al backend (config-ms)" });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div
            style={{
                fontFamily: "'Press Start 2P', monospace",
                minHeight: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                background:
                    "linear-gradient(135deg, #0d0d0d, #1a1a2e 40%, #2a004f)",
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
                    boxShadow:
                        "0 0 20px #ff00ff55, inset 0 0 10px #55005544",
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
                    UCB Config App - Preferencias
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

                {/* FORM CONFIG */}
                <div style={{ marginBottom: "1.5rem" }}>
                    <label
                        style={{
                            display: "block",
                            marginBottom: "0.5rem",
                            fontSize: "0.7rem",
                            color: "#ccc",
                        }}
                    >
                        User ID
                    </label>
                    <input
                        type="text"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        style={{
                            width: "100%",
                            padding: "0.5rem",
                            borderRadius: 6,
                            border: "1px solid #555",
                            background: "#111",
                            color: "#f8f8f2",
                            marginBottom: "1rem",
                            fontFamily: "inherit",
                            fontSize: "0.7rem",
                        }}
                    />

                    <div
                        style={{
                            display: "grid",
                            gridTemplateColumns: "1fr",
                            gap: "0.5rem",
                            fontSize: "0.7rem",
                        }}
                    >
                        <label style={{ display: "flex", gap: "0.5rem" }}>
                            <input
                                type="checkbox"
                                checked={darkmode}
                                onChange={(e) => setDarkmode(e.target.checked)}
                            />
                            Dark mode
                        </label>

                        <label style={{ display: "flex", gap: "0.5rem" }}>
                            <input
                                type="checkbox"
                                checked={showInfo}
                                onChange={(e) => setShowInfo(e.target.checked)}
                            />
                            Mostrar información
                        </label>

                        <label style={{ display: "flex", gap: "0.5rem" }}>
                            <input
                                type="checkbox"
                                checked={showOnlineStatus}
                                onChange={(e) =>
                                    setShowOnlineStatus(e.target.checked)
                                }
                            />
                            Mostrar estado en línea
                        </label>

                        <label style={{ display: "flex", gap: "0.5rem" }}>
                            <input
                                type="checkbox"
                                checked={showNotifications}
                                onChange={(e) =>
                                    setShowNotifications(e.target.checked)
                                }
                            />
                            Mostrar notificaciones
                        </label>
                    </div>

                    <button
                        onClick={sendConfig}
                        disabled={loading}
                        style={{
                            width: "100%",
                            padding: "0.8rem",
                            marginTop: "1rem",
                            borderRadius: 6,
                            border: "none",
                            background: loading ? "#555" : "#00eaff",
                            color: "#000",
                            fontWeight: "bold",
                            cursor: loading ? "default" : "pointer",
                            textShadow: "0 0 4px #000",
                            boxShadow: "0 0 10px #00eaff88",
                        }}
                    >
                        {loading ? "Enviando..." : "Guardar configuración"}
                    </button>
                </div>

                <hr style={{ borderColor: "#444", margin: "1.5rem 0" }} />

                {/* RESULTADO */}
                <div style={{ fontSize: "0.7rem" }}>
                    <h3
                        style={{
                            marginBottom: "0.5rem",
                            color: "#ff77ff",
                            fontSize: "0.9rem",
                        }}
                    >
                        Resultado
                    </h3>
                    {result ? (
                        <pre
                            style={{
                                background: "#050510",
                                padding: "0.8rem",
                                borderRadius: 8,
                                border: "1px solid #333",
                                maxHeight: "200px",
                                overflow: "auto",
                            }}
                        >
                            {JSON.stringify(result, null, 2)}
                        </pre>
                    ) : (
                        <p style={{ color: "#888" }}>
                            Envía la configuración para ver la respuesta aquí.
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default App;
