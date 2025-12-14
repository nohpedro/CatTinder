import { useNavigate } from "react-router-dom";

export default function UsersPage() {
    const navigate = useNavigate();
    return (
        <div style={{ padding: 24 }}>
            <h2>Usuarios</h2>
            <button onClick={() => navigate("/menu")}>Volver al menú</button>
        </div>
    );
}
