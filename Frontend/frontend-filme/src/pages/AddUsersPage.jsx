import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { userService } from '../services/userService.js';

export default function AddUsersPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({ username: '', email: '', phone: '', password: '', userRole: 'WORKER' });
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await userService.register(formData);
            alert(t('user_added_success'));
            navigate('/users');
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div style={{ padding: '40px', fontFamily: "'Georgia', serif" }}>
            <h1 style={{ color: 'var(--color-plum)', borderBottom: '3px solid var(--color-rose)', paddingBottom: '10px' }}>➕ {t('add_new_user')}</h1>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxWidth: '400px' }}>
                <input type="text" placeholder="Username" required style={{ padding: '10px' }} onChange={e => setFormData({...formData, username: e.target.value})} />
                <input type="email" placeholder="Email" required style={{ padding: '10px' }} onChange={e => setFormData({...formData, email: e.target.value})} />
                <input type="text" placeholder="Phone" required style={{ padding: '10px' }} onChange={e => setFormData({...formData, phone: e.target.value})} />
                <input type="password" placeholder="Password" required style={{ padding: '10px' }} onChange={e => setFormData({...formData, password: e.target.value})} />
                <select style={{ padding: '10px' }} value={formData.userRole} onChange={e => setFormData({...formData, userRole: e.target.value})}>
                    <option value="WORKER">WORKER</option>
                    <option value="MANAGER">MANAGER</option>
                    <option value="ADMIN">ADMIN</option>
                </select>
                <button type="submit" style={{ backgroundColor: 'var(--color-plum)', color: 'white', padding: '12px', fontWeight: 'bold', cursor: 'pointer' }}>{t('save_user')}</button>
            </form>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
}