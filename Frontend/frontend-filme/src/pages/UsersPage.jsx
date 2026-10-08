import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { userService } from '../services/userService.js';

export default function UsersPage() {
    const { t } = useTranslation();
    const [users, setUsers] = useState([]);
    const [roleFilter, setRoleFilter] = useState('');
    const [error, setError] = useState('');
    
    // Stări modal editare
    const [editUser, setEditUser] = useState(null);
    const [editData, setEditData] = useState({ email: '', phone: '', userRole: 'WORKER' });

    const fetchUsers = async () => {
        try {
            const data = await userService.getAll();
            setUsers(data);
        } catch {
            setError(t('failed_load_users', 'Failed to load users.'));
        }
    };

    useEffect(() => { 
        fetchUsers(); 
    }, []);

    const handleDelete = async (id) => {
        if (!window.confirm(t('confirm_delete_user', 'Are you sure you want to delete this user?'))) return;
        try {
            await userService.delete(id);
            setUsers(prev => prev.filter(u => u.id !== id));
            alert(t('user_deleted_success', 'User deleted successfully!'));
        } catch {
            alert(t('error_delete', 'Error occurred while deleting user.'));
        }
    };

    const handleEditSave = async () => {
        try {
            const updated = await userService.update(editUser.id, editData);
            
            setUsers(prev => prev.map(u => u.id === editUser.id ? { ...u, ...editData } : u));
            
            try {
                await userService.triggerNotification({
                    userId: editUser.id,
                    email: editData.email,
                    phone: editData.phone
                });
            } catch (notifErr) {
                console.error("Notification failed (but user was saved):", notifErr);
            }

            setEditUser(null);
            alert(t('user_updated_success', 'User updated successfully!'));

        } catch (err) {
            console.error("Update error:", err);
            alert(t('error_update', 'Failed to update user.'));
        }
    };

    const filteredUsers = roleFilter ? users.filter(u => (u.userRole === roleFilter || u.role === roleFilter)) : users;

    return (
        <div style={{ padding: '32px', fontFamily: "'Georgia', serif" }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                <h1 style={{ margin: 0, color: '#5a2d82', borderBottom: '3px solid #e84393', paddingBottom: '8px' }}>
                    🛡️ {t('users_management', 'Users Management')}
                </h1>
                
                <select 
                    value={roleFilter} 
                    onChange={e => setRoleFilter(e.target.value)} 
                    style={{ padding: '10px', borderRadius: '6px', border: '1px solid #c9b0e8' }}
                >
                    <option value="">{t('all_roles', 'All Roles')}</option>
                    <option value="WORKER">WORKER</option>
                    <option value="MANAGER">MANAGER</option>
                    <option value="ADMIN">ADMIN</option>
                </select>
            </div>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <table style={{ width: '100%', borderCollapse: 'collapse', backgroundColor: 'white', boxShadow: '0 4px 6px rgba(0,0,0,0.05)', borderRadius: '8px', overflow: 'hidden' }}>
                <thead>
                    <tr style={{ backgroundColor: '#5a2d82', color: 'white', textAlign: 'left' }}>
                        <th style={{ padding: '12px' }}>{t('username', 'Username')}</th>
                        <th style={{ padding: '12px' }}>{t('email', 'Email')}</th>
                        <th style={{ padding: '12px' }}>{t('phone', 'Phone')}</th>
                        <th style={{ padding: '12px' }}>{t('role', 'Role')}</th>
                        <th style={{ padding: '12px', textAlign: 'center' }}>{t('actions', 'Actions')}</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredUsers.map((u, index) => (
                        <tr key={u.id} style={{ borderBottom: '1px solid #eee', backgroundColor: index % 2 === 0 ? 'white' : '#fcfaff' }}>
                            <td style={{ padding: '12px', fontWeight: 'bold', color: '#333' }}>{u.username}</td>
                            <td style={{ padding: '12px', color: '#666' }}>{u.email}</td>
                            <td style={{ padding: '12px', color: '#666' }}>{u.phone || '-'}</td>
                            <td style={{ padding: '12px' }}>
                                <span style={{ backgroundColor: '#e8d5f5', color: '#5a2d82', padding: '4px 8px', borderRadius: '12px', fontSize: '12px', fontWeight: 'bold' }}>
                                    {u.userRole || u.role}
                                </span>
                            </td>
                            <td style={{ padding: '12px', textAlign: 'center' }}>
                                <button onClick={() => { setEditUser(u); setEditData({ email: u.email, phone: u.phone || '', userRole: u.userRole || u.role }); }} style={actionBtn}>✏️</button>
                                <button onClick={() => handleDelete(u.id)} style={{...actionBtn, marginLeft: '8px'}}>🗑️</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {editUser && (
                <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
                    <div style={{ backgroundColor: 'white', padding: '24px', borderRadius: '12px', display: 'flex', flexDirection: 'column', gap: '15px', width: '320px' }}>
                        <h3 style={{ margin: '0 0 10px', color: '#5a2d82' }}>✏️ {t('edit_user', 'Edit User')}</h3>
                        
                        <label style={{ fontSize: '12px', fontWeight: 'bold', color: '#666', marginBottom: '-10px' }}>{t('email', 'Email')}</label>
                        <input value={editData.email} onChange={e => setEditData({...editData, email: e.target.value})} style={inputStyle} />
                        
                        <label style={{ fontSize: '12px', fontWeight: 'bold', color: '#666', marginBottom: '-10px' }}>{t('phone', 'Phone')}</label>
                        <input value={editData.phone} onChange={e => setEditData({...editData, phone: e.target.value})} style={inputStyle} />
                        
                        <label style={{ fontSize: '12px', fontWeight: 'bold', color: '#666', marginBottom: '-10px' }}>{t('role', 'Role')}</label>
                        <select value={editData.userRole} onChange={e => setEditData({...editData, userRole: e.target.value})} style={inputStyle}>
                            <option value="WORKER">WORKER</option>
                            <option value="MANAGER">MANAGER</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>
                        
                        <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                            <button onClick={handleEditSave} style={{ flex: 1, backgroundColor: '#5a2d82', color: 'white', border: 'none', padding: '10px', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer' }}>{t('save', 'Save')}</button>
                            <button onClick={() => setEditUser(null)} style={{ flex: 1, backgroundColor: '#ccc', color: '#333', border: 'none', padding: '10px', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer' }}>{t('cancel', 'Cancel')}</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

const inputStyle = { padding: '10px', border: '1px solid #ccc', borderRadius: '6px' };
const actionBtn = { border: 'none', background: 'none', cursor: 'pointer', fontSize: '16px' };