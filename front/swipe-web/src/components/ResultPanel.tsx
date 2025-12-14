import React from "react";
import type { SwipeResponse } from "../types";

type ResultPanelProps = {
    result: SwipeResponse | null;
};

export const ResultPanel: React.FC<ResultPanelProps> = ({ result }) => {
    return (
        <div style={{ marginTop: "1rem" }}>
            <h4 style={{ marginBottom: "0.75rem", textAlign: "left" }}>Resultado</h4>

            <div
                style={{
                    display: "flex",
                    justifyContent: "center",
                }}
            >
                {result ? (
                    <div
                        style={{
                            background: "#1f2933",        // caja oscura
                            color: "#f9fafb",             // texto claro
                            padding: "1rem 1.25rem",
                            borderRadius: 8,
                            minWidth: "100%",
                            boxShadow: "0 4px 10px rgba(0,0,0,0.35)",
                            fontFamily: "monospace",
                            fontSize: "0.9rem",
                            whiteSpace: "pre-wrap",
                            wordBreak: "break-word",
                        }}
                    >
                        <div
                            style={{
                                fontWeight: "bold",
                                marginBottom: "0.5rem",
                                opacity: 0.8,
                            }}
                        >
                            Respuesta del backend
                        </div>

                        <pre
                            style={{
                                margin: 0,
                                background: "transparent",
                                color: "inherit", // usa el color claro del contenedor
                            }}
                        >
              {JSON.stringify(result, null, 2)}
            </pre>
                    </div>
                ) : (
                    <p
                        style={{
                            fontStyle: "italic",
                            color: "#e5e7eb",
                        }}
                    >
                        No se ha enviado ninguna petición aún.
                    </p>
                )}
            </div>
        </div>
    );
};
