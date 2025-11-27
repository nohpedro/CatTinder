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

    // 👉 NUEVO: GET /config/{userId}
    const getConfig = async () => {
        try {
            setLoading(true);
            setResult(null);

            const freshToken = await withAuth();
            if (!freshToken) return;

            const response = await fetch(
                `http://localhost:8082/confg-ms/api/v1/config/${userId}`,
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
                    ...(data as any),
                });
            } else {
                const cfg = data as ConfigResponse;

                // Opcional: si el backend devuelve estos campos, sincronizamos el formulario
                if (cfg.userid) setUserId(cfg.userid);
                if (typeof cfg.darkmode === "boolean") setDarkmode(cfg.darkmode);
                if (typeof cfg.showInfo === "boolean") setShowInfo(cfg.showInfo);
                if (typeof cfg.showOnlineStatus === "boolean") {
                    setShowOnlineStatus(cfg.showOnlineStatus);
                }
                if (typeof cfg.showNotifications === "boolean") {
                    setShowNotifications(cfg.showNotifications);
                }

                setResult(cfg);
            }
        } catch (e) {
            console.error(e);
            setResult({ message: "Error llamando al backend (GET config)" });
        } finally {
            setLoading(false);
        }
    };

    // === Estilos retro / Crash-like ===
    const outerStyle = {
        fontFamily: "'Press Start 2P', system-ui, monospace",
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        background:
            "linear-gradient(145deg, #0b1b1b 0%, #16402c 25%, #3f6d2b 55%, #f68c1f 100%)",
        backgroundSize: "200% 200%",
        padding: "2rem",
        color: "#ffeec5",
        imageRendering: "pixelated" as const,
    };

    const frameStyle = {
        width: "98%",
        maxWidth: "620px",
        padding: "1.6rem",
        borderRadius: 0,
        border: "4px solid #5b3214",
        background:
            "repeating-linear-gradient(45deg, #3b2413 0, #3b2413 10px, #4c2f17 10px, #4c2f17 20px)",
        boxShadow:
            "0 0 0 4px #201308, 0 12px 0 #201308, 0 12px 0 4px #000000aa",
        position: "relative" as const,
    };

    const titleBadge = {
        position: "absolute" as const,
        top: "-18px",
        left: "50%",
        transform: "translateX(-50%)",
        padding: "0.4rem 1rem",
        background:
            "linear-gradient(180deg, #ffcf40 0%, #ff9c1a 60%, #c7670c 100%)",
        border: "3px solid #3e1e00",
        boxShadow: "0 4px 0 #3e1e00",
        textTransform: "uppercase" as const,
        fontSize: "0.7rem",
        letterSpacing: "2px",
        color: "#2b1300",
        textShadow: "1px 1px 0 #ffe9a6",
    };

    const sectionTitle = {
        marginBottom: "0.5rem",
        color: "#ffe27a",
        fontSize: "0.8rem",
        textShadow: "2px 2px 0 #7b3700",
    };

    const labelStyle = {
        display: "block",
        marginBottom: "0.5rem",
        fontSize: "0.65rem",
        color: "#ffeec5",
        textShadow: "1px 1px 0 #3a2009",
    };

    const inputStyle = {
        width: "100%",
        padding: "0.6rem 0.7rem",
        borderRadius: 0,
        border: "3px solid #241109",
        background:
            "repeating-linear-gradient(90deg, #1a0f09 0, #1a0f09 6px, #26140b 6px, #26140b 12px)",
        color: "#ffeec5",
        marginBottom: "1rem",
        fontFamily: "inherit",
        fontSize: "0.7rem",
        boxShadow: "0 3px 0 #000",
        outline: "none",
    };

    const checkboxRow = {
        display: "flex",
        gap: "0.5rem",
        alignItems: "center",
        padding: "0.3rem 0.4rem",
        borderRadius: 0,
        border: "2px solid #2b180b",
        background:
            "linear-gradient(180deg, #2b180b 0%, #372014 50%, #241109 100%)",
        boxShadow: "0 3px 0 #000",
    };

    const buttonBase = {
        width: "100%",
        padding: "0.85rem",
        borderRadius: 0,
        border: "3px solid #1d0f05",
        fontWeight: "bold",
        cursor: "pointer",
        fontSize: "0.75rem",
        textShadow: "2px 2px 0 #000000",
        boxShadow: "0 5px 0 #1d0f05",
        transition: "transform 0.05s ease, box-shadow 0.05s ease",
    };

    const logoutButton = {
        ...buttonBase,
        marginBottom: "1.5rem",
        background:
            "linear-gradient(180deg, #ff5b2b 0%, #d32710 60%, #7b1406 100%)",
        color: "#ffeec5",
    };

    const primaryButton = {
        ...buttonBase,
        marginTop: "0.7rem",
        background:
            "linear-gradient(180deg, #ffcf40 0%, #ff9c1a 60%, #c7670c 100%)",
        color: "#2b1300",
    };

    const secondaryButton = {
        ...buttonBase,
        marginTop: "1rem",
        background:
            "linear-gradient(180deg, #7dff6a 0%, #36c44a 60%, #1f7b2b 100%)",
        color: "#062010",
    };

    const hrStyle = {
        borderColor: "#5b3214",
        margin: "1.4rem 0",
        borderWidth: "2px",
    };

    const resultBox = {
        background:
            "repeating-linear-gradient(135deg, #120b06 0, #120b06 6px, #1b1009 6px, #1b1009 12px)",
        padding: "0.8rem",
        borderRadius: 0,
        border: "3px solid #2b180b",
        maxHeight: "220px",
        overflow: "auto" as const,
        boxShadow: "0 4px 0 #000",
        fontSize: "0.65rem",
        color: "#ffeec5",
    };

    return (
        <div style={outerStyle}>
            <div style={frameStyle}>
                <div style={titleBadge}>UCB CONFIG APP</div>

                <div style={{ marginTop: "1.4rem" }}>
                    <p
                        style={{
                            textAlign: "center",
                            marginBottom: "0.8rem",
                            fontSize: "0.7rem",
                            textShadow: "2px 2px 0 #3a2009",
                        }}
                    >
                        Sesión:{" "}
                        <span style={{ color: "#ffe27a" }}>{username}</span>
                    </p>

                    <button
                        onClick={handleLogout}
                        style={logoutButton}
                    >
                        SALIR DEL TEMPLO
                    </button>

                    <hr style={hrStyle} />

                    {/* FORM CONFIG */}
                    <div style={{ marginBottom: "1.5rem" }}>
                        <h3 style={sectionTitle}>PANTALLA DE OPCIONES</h3>

                        <label style={labelStyle}>User ID</label>
                        <input
                            type="text"
                            value={userId}
                            onChange={(e) => setUserId(e.target.value)}
                            style={inputStyle}
                        />

                        <div
                            style={{
                                display: "grid",
                                gridTemplateColumns: "1fr",
                                gap: "0.6rem",
                                fontSize: "0.65rem",
                            }}
                        >
                            <label style={checkboxRow}>
                                <input
                                    type="checkbox"
                                    checked={darkmode}
                                    onChange={(e) =>
                                        setDarkmode(e.target.checked)
                                    }
                                    style={{ transform: "scale(1.2)" }}
                                />
                                <span>MODO OSCURO</span>
                            </label>

                            <label style={checkboxRow}>
                                <input
                                    type="checkbox"
                                    checked={showInfo}
                                    onChange={(e) =>
                                        setShowInfo(e.target.checked)
                                    }
                                    style={{ transform: "scale(1.2)" }}
                                />
                                <span>MOSTRAR INFO</span>
                            </label>

                            <label style={checkboxRow}>
                                <input
                                    type="checkbox"
                                    checked={showOnlineStatus}
                                    onChange={(e) =>
                                        setShowOnlineStatus(e.target.checked)
                                    }
                                    style={{ transform: "scale(1.2)" }}
                                />
                                <span>ESTADO EN LÍNEA</span>
                            </label>

                            <label style={checkboxRow}>
                                <input
                                    type="checkbox"
                                    checked={showNotifications}
                                    onChange={(e) =>
                                        setShowNotifications(e.target.checked)
                                    }
                                    style={{ transform: "scale(1.2)" }}
                                />
                                <span>NOTIFICACIONES</span>
                            </label>
                        </div>

                        {/* Botón GET */}
                        <button
                            onClick={getConfig}
                            disabled={loading}
                            style={{
                                ...secondaryButton,
                                ...(loading ? { opacity: 0.6, cursor: "default" } : {}),
                            }}
                        >
                            {loading ? "CARGANDO..." : "CARGAR CONFIG"}
                        </button>

                        {/* Botón POST */}
                        <button
                            onClick={sendConfig}
                            disabled={loading}
                            style={{
                                ...primaryButton,
                                ...(loading ? { opacity: 0.6, cursor: "default" } : {}),
                            }}
                        >
                            {loading ? "GUARDANDO..." : "GUARDAR CONFIG"}
                        </button>
                    </div>

                    <hr style={hrStyle} />

                    {/* RESULTADO */}
                    <div style={{ fontSize: "0.7rem" }}>
                        <h3 style={sectionTitle}>RESULTADO DEL TOTEM</h3>
                        {result ? (
                            <pre style={resultBox}>
                                {JSON.stringify(result, null, 2)}
                            </pre>
                        ) : (
                            <p
                                style={{
                                    color: "#ffeec5",
                                    textShadow: "1px 1px 0 #3a2009",
                                    fontSize: "0.65rem",
                                }}
                            >
                                Usa “CARGAR CONFIG” o “GUARDAR CONFIG” para ver la
                                respuesta del servidor.
                            </p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default App;
