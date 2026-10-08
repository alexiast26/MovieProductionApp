import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { staffService } from '../services/staffService';

export default function AddStaffPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [staffType, setStaffType] = useState('actors'); 
    const [formData, setFormData] = useState({ name: '', age: '', gender: 'MALE', image_url: '' });

    const handleImageUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => setFormData(prev => ({ ...prev, image_url: reader.result }));
            reader.readAsDataURL(file);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = { ...formData, age: parseInt(formData.age) };
            
            // Asigură-te că în staffService.js ai definit metoda ca: create: async (category, data) => ...
            await staffService.create(staffType, payload);
            
            alert(t('staff_added', 'Saved successfully!'));
            navigate('/staff');
        } catch (err) {
            console.error("Error creating staff:", err);
            alert(t('failed_add_staff', 'Failed to create staff member.'));
        }
    };

    return (
        <div style={{ padding: '40px', fontFamily: "'Georgia', serif" }}>
            <h1 style={{ color: 'var(--color-plum)', marginBottom: '24px', borderBottom: '3px solid var(--color-rose)', paddingBottom: '8px', maxWidth: '400px' }}>
                ➕ {t('add_new_staff', 'Add New Staff')}
            </h1>
            
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px', maxWidth: '400px' }}>
                
                <label htmlFor="staffType" style={labelStyle}>{t('staff_category', 'Category')}</label>
                <select id="staffType" name="staffType" value={staffType} onChange={e => setStaffType(e.target.value)} style={inputStyle}>
                    <option value="actors">{t('actor', 'Actor')}</option>
                    <option value="directors">{t('director', 'Director')}</option>
                    <option value="screenwriters">{t('screenwriter', 'Screenwriter')}</option>
                </select>

                <label htmlFor="name" style={labelStyle}>{t('full_name', 'Full Name')} *</label>
                <input id="name" name="name" placeholder={t('eg_name', 'e.g. Leonardo DiCaprio')} required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} style={inputStyle} />
                
                <label htmlFor="age" style={labelStyle}>{t('age', 'Age')} *</label>
                <input id="age" name="age" type="number" placeholder={t('eg_age', 'e.g. 48')} required value={formData.age} onChange={e => setFormData({...formData, age: e.target.value})} style={inputStyle} />
                
                <label htmlFor="gender" style={labelStyle}>{t('gender', 'Gender')}</label>
                <select id="gender" name="gender" value={formData.gender} onChange={e => setFormData({...formData, gender: e.target.value})} style={inputStyle}>
                    {/* AICI ESTE REPARATĂ PROBLEMA DE SALVARE (MALE / FEMALE CU LITERE MARI) */}
                    <option value="MALE">{t('male', 'Male')}</option>
                    <option value="FEMALE">{t('female', 'Female')}</option>
                </select>

                <label htmlFor="profilePic" style={labelStyle}>{t('profile_picture', 'Profile Picture')}</label>
                <input id="profilePic" name="profilePic" type="file" accept="image/*" onChange={handleImageUpload} style={{ padding: '8px 0', cursor: 'pointer' }} />
                
                <button type="submit" style={{ backgroundColor: 'var(--color-plum)', color: 'white', padding: '14px', borderRadius: '6px', border: 'none', fontWeight: 'bold', fontSize: '15px', cursor: 'pointer', marginTop: '10px' }}>
                    💾 {t('save_person', 'Save')}
                </button>
            </form>
        </div>
    );
}

const labelStyle = { 
    fontSize: '13px', 
    fontWeight: 'bold', 
    color: '#5a2d82', 
    marginBottom: '-10px' 
};

const inputStyle = { 
    padding: '12px', 
    borderRadius: '6px', 
    border: '1px solid #c9b0e8', 
    outline: 'none', 
    fontSize: '14px', 
    backgroundColor: '#fcfafc' 
};