import { useNavigate } from "react-router-dom";

export default function MenuPage() {
    const navigate = useNavigate();

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
                    UCB Dating App - Menu
                </h2>

                <button
                    onClick={() => navigate("/users")}
                    style={btnStyle("#00eaff")}
                >
                    Usuarios
                </button>

                <button
                    onClick={() => navigate("/swipes")}
                    style={btnStyle("#ff77ff")}
                >
                    Swipes
                </button>

                <button
                    onClick={() => navigate("/configs")}
                    style={btnStyle("#00ff88")}
                >
                    Configuraciones
                </button>
            </div>
        </div>
    );
}

function btnStyle(glow: string) {
    return {
        width: "100%",
        padding: "0.9rem",
        marginBottom: "1rem",
        borderRadius: 8,
        border: "none",
        background: "#111",
        color: "#fff",
        fontWeight: "bold",
        cursor: "pointer",
        textShadow: "0 0 4px #000",
        boxShadow: `0 0 12px ${glow}`,
    } as const;
}
