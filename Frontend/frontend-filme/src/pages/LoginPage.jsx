import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { userService } from '../services/userService.js';

export default function LoginPage({ onLogin, onSwitchToRegister }) {
    const { t } = useTranslation();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = await userService.login(username, password);
            
            localStorage.setItem('jwt_token', data.token);
            const payload = JSON.parse(atob(data.token.split('.')[1]));
            const userRole = payload.role;
            
            localStorage.setItem('user_role', userRole);
            onLogin(userRole);
        } catch (err) {
            setError('Autentificare eșuată. Verifică datele introduse.');
        }
    };

    return (
        <div style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', backgroundColor: '#363040', fontFamily: "'Georgia', serif" }}>
            <div style={{ backgroundColor: 'white', padding: '40px', borderRadius: '12px', width: '350px', boxShadow: '0 8px 24px rgba(0,0,0,0.2)' }}>
                <h2 style={{ color: '#5a2d82', textAlign: 'center', marginBottom: '24px' }}>🎬 CineMatics Login</h2>
                
                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                    <input 
                        type="text" 
                        placeholder={t('username', 'Nume utilizator')} 
                        value={username} 
                        onChange={e => setUsername(e.target.value)} 
                        required 
                        style={{ padding: '12px', borderRadius: '6px', border: '1px solid #ccc', fontSize: '14px' }} 
                    />
                    <input 
                        type="password" 
                        placeholder={t('password', 'Password')} 
                        value={password} 
                        onChange={e => setPassword(e.target.value)} 
                        required 
                        style={{ padding: '12px', borderRadius: '6px', border: '1px solid #ccc', fontSize: '14px' }} 
                    />
                    
                    {error && <p style={{ color: '#c0392b', fontSize: '13px', margin: '0' }}>{error}</p>}
                    
                    <button type="submit" style={{ padding: '12px', backgroundColor: '#462861', color: 'white', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer', fontSize: '16px', marginTop: '10px' }}>
                        {t('login_btn', 'Autentificare')}
                    </button>
                </form>

                <div style={{ marginTop: '24px', textAlign: 'center', fontSize: '14px' }}>
                    <span style={{ color: '#666' }}>{t('no_account', 'Nu ai cont?')} </span>
                    <button onClick={onSwitchToRegister} style={{ background: 'none', border: 'none', color: '#e84393', fontWeight: 'bold', cursor: 'pointer', fontSize: '14px' }}>
                        {t('register', 'Înregistrează-te')}
                    </button>
                </div>
            </div>
        </div>
    );
}