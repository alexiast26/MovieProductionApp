import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MoviesPage from './pages/MoviesPage';
import AddMoviePage from './pages/AddMoviePage';
import StaffPage from './pages/StaffPage';
import UsersPage from './pages/UsersPage';
import AddUsersPage from './pages/AddUsersPage';
import StatisticsPage from './pages/StatisticsPage';
import AddStaffPage from './pages/AddStaffPage';
import './App.css';

export default function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [userRole, setUserRole] = useState('');
    const [showRegister, setShowRegister] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('jwt_token');
        if (token) {
            setIsAuthenticated(true);
            setUserRole(localStorage.getItem('user_role') || 'WORKER');
        }
    }, []);

    const handleLogin = (role) => {
        setUserRole(role);
        localStorage.setItem('user_role', role);
        setIsAuthenticated(true);
    };

    const handleLogout = () => {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_role');
        setIsAuthenticated(false);
        setUserRole('');
        setShowRegister(false);
    };

    if (!isAuthenticated) {
        if (showRegister) return <RegisterPage onSwitchToLogin={() => setShowRegister(false)} />;
        return <LoginPage onLogin={handleLogin} onSwitchToRegister={() => setShowRegister(true)} />;
    }

    return (
        <Router>
            <div style={{ display: 'flex', height: '100vh', width: '100vw', overflow: 'hidden' }}>
                <Sidebar userRole={userRole} onLogout={handleLogout} />
                <div style={{ flex: 1, overflowY: 'auto', backgroundColor: 'white' }}>
                    <Routes>
                        <Route path="/welcome" element={<div style={{ width: '100%', height: '100%', backgroundColor: 'white' }} />} />
                        
                        <Route path="/movies" element={<MoviesPage userRole={userRole} />} />
                        <Route path="/staff" element={<StaffPage userRole={userRole} />} />
                        
                        {['WORKER', 'MANAGER', 'ADMIN'].includes(userRole) && (
                            <>
                                <Route path="/add-movie" element={<AddMoviePage />} />
                                <Route path="/add-staff" element={<AddStaffPage />} />
                            </>
                        )}

                        {['MANAGER', 'ADMIN'].includes(userRole) && (
                            <Route path="/statistics" element={<StatisticsPage />} />
                        )}

                        {userRole === 'ADMIN' && (
                            <>
                                <Route path="/users" element={<UsersPage />} />
                                <Route path="/add-user" element={<AddUsersPage />} />
                            </>
                        )}

                        <Route path="*" element={<Navigate to="/movies" />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}