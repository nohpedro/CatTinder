import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import MenuPage from "./menu/MenuPage";
import { SwipesPage } from "./swipes";
import UsersPage from "./users/UsersPage.tsx";
import { ConfigsPage } from "./configs";

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/menu" replace />} />

                <Route path="/menu" element={<MenuPage />} />
                <Route path="/users" element={<UsersPage />} />
                <Route path="/swipes" element={<SwipesPage />} />
                <Route path="/configs" element={<ConfigsPage />} />

                <Route path="*" element={<Navigate to="/menu" replace />} />
            </Routes>
        </BrowserRouter>
    );
}
