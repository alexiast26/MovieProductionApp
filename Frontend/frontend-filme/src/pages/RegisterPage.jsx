import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { userService } from '../services/userService.js';

export default function RegisterPage({ onSwitchToLogin }) {
    const { t } = useTranslation();
    const [formData, setFormData] = useState({ username: '', email: '', phone: '', password: '', userRole: 'WORKER' });
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await userService.register(formData); // Delegare către serviciu
            alert(t('account_created'));
            onSwitchToLogin();
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', backgroundColor: 'var(--color-dark)' }}>
            <div style={{ backgroundColor: 'var(--color-light)', padding: '40px', borderRadius: '8px', width: '300px' }}>
                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                    <input type="text" placeholder={t('username')} required style={{ padding: '10px' }} onChange={e => setFormData({...formData, username: e.target.value})} />
                    <input type="email" placeholder="Email" required style={{ padding: '10px' }} onChange={e => setFormData({...formData, email: e.target.value})} />
                    <input type="text" placeholder="Phone" required style={{ padding: '10px' }} onChange={e => setFormData({...formData, phone: e.target.value})} />
                    <input type="password" placeholder={t('password')} required style={{ padding: '10px' }} onChange={e => setFormData({...formData, password: e.target.value})} />
                    <select style={{ padding: '10px' }} value={formData.userRole} onChange={e => setFormData({...formData, userRole: e.target.value})}>
                        <option value="WORKER">{t('worker')}</option>
                        <option value="MANAGER">{t('manager')}</option>
                        <option value="ADMIN">{t('admin')}</option>
                    </select>
                    <button type="submit" className="btn btn-primary">{t('register')}</button>
                </form>
                {error && <p style={{ color: 'red' }}>{error}</p>}
            </div>
        </div>
    );
}