import React from "react";

type SwipeFormProps = {
    userA: string;
    userB: string;
    loading: boolean;
    onChangeUserA: (value: string) => void;
    onChangeUserB: (value: string) => void;
    onSend: () => void;
    onCheckMatch: () => void; // 👈 nueva prop
};

export const SwipeForm: React.FC<SwipeFormProps> = ({
                                                        userA,
                                                        userB,
                                                        loading,
                                                        onChangeUserA,
                                                        onChangeUserB,
                                                        onSend,
                                                        onCheckMatch,
                                                    }) => {
    return (
        <>
            <h3>Enviar swipe</h3>

            <div style={{ marginBottom: "0.5rem" }}>
                <label>
                    Usuario A:{" "}
                    <input
                        value={userA}
                        onChange={(e) => onChangeUserA(e.target.value)}
                        style={{ width: "100%" }}
                    />
                </label>
            </div>

            <div style={{ marginBottom: "0.5rem" }}>
                <label>
                    Usuario B:{" "}
                    <input
                        value={userB}
                        onChange={(e) => onChangeUserB(e.target.value)}
                        style={{ width: "100%" }}
                    />
                </label>
            </div>

            <div style={{ display: "flex", gap: "0.5rem", marginTop: "0.5rem" }}>
                <button onClick={onSend} disabled={loading}>
                    {loading ? "Enviando..." : "Enviar swipe"}
                </button>

                <button onClick={onCheckMatch} disabled={loading}>
                    {loading ? "Consultando..." : "Ver si hay match"}
                </button>
            </div>
        </>
    );
};
