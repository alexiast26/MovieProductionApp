import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { movieService } from '../services/movieService';
import { staffService } from '../services/staffService';

export default function AddMoviePage() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        releseYear: '',
        duration: '',
        genre: 'ACTION',
        category: 'FEATURE_FILM',
        imageUrls: [],
        actorIds: [],
        directorIds: [],
        screenwriterIds: []
    });

    const [allActors, setAllActors] = useState([]);
    const [allDirectors, setAllDirectors] = useState([]);
    const [allScreenwriters, setAllScreenwriters] = useState([]);

    useEffect(() => {
        const fetchStaff = async () => {
            try {
                const [actors, directors, screenwriters] = await Promise.all([
                    staffService.getAll('actors'),
                    staffService.getAll('directors'),
                    staffService.getAll('screenwriters')
                ]);
                setAllActors(actors);
                setAllDirectors(directors);
                setAllScreenwriters(screenwriters);
            } catch (err) {
                console.error("Failed to load staff", err);
            }
        };
        fetchStaff();
    }, []);

    const handleImageUpload = (e) => {
        const files = Array.from(e.target.files);
        if (formData.imageUrls.length + files.length > 3) {
            alert(t('max_images_error', 'Maximum 3 images allowed.'));
            return;
        }
        files.forEach(file => {
            const reader = new FileReader();
            reader.onloadend = () => {
                setFormData(prev => ({ ...prev, imageUrls: [...prev.imageUrls, reader.result] }));
            };
            reader.readAsDataURL(file);
        });
    };

    const handleRemoveImage = (urlToRemove) => {
        setFormData(prev => ({ ...prev, imageUrls: prev.imageUrls.filter(url => url !== urlToRemove) }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await movieService.create({
                ...formData,
                releseYear: parseInt(formData.releseYear),
                duration: parseInt(formData.duration)
            });
            alert(t('movie_added_success', 'Movie added successfully!'));
            navigate('/movies');
        } catch (err) {
            alert(t('failed_add_movie', 'Failed to add movie: ') + err.message);
        }
    };

    const toggleStaff = (id, field) => {
        setFormData(prev => ({
            ...prev,
            [field]: prev[field].includes(id) ? prev[field].filter(i => i !== id) : [...prev[field], id]
        }));
    };

    return (
        <div style={{ padding: '40px', fontFamily: "'Georgia', serif", maxWidth: '600px', margin: '0 auto' }}>
            <h1 style={{ color: 'var(--color-plum)', marginBottom: '24px' }}>🎬 {t('add_new_movie', 'Add New Movie')}</h1>
            
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                <input placeholder={t('movie_title', 'Title')} required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} style={inputStyle} />
                <input type="number" placeholder={t('year', 'Year')} required value={formData.releseYear} onChange={e => setFormData({...formData, releseYear: e.target.value})} style={inputStyle} />
                <input type="number" placeholder={t('duration_min', 'Duration (min)')} required value={formData.duration} onChange={e => setFormData({...formData, duration: e.target.value})} style={inputStyle} />
                
                <select value={formData.genre} onChange={e => setFormData({...formData, genre: e.target.value})} style={inputStyle}>
                    {['ACTION', 'COMEDY', 'DRAMA', 'HORROR', 'SCI_FI'].map(g => <option key={g} value={g}>{g}</option>)}
                </select>

                <select value={formData.category} onChange={e => setFormData({...formData, category: e.target.value})} style={inputStyle}>
                    {['FEATURE_FILM', 'DOCUMENTARY', 'SHORT_FILM', 'TV_SERIES'].map(c => <option key={c} value={c}>{c.replace('_', ' ')}</option>)}
                </select>

                {/* GALERIE IMAGINI */}
                <div style={sectionCard}>
                    <h4 style={{ margin: '0 0 10px' }}>📷 {t('manage_images', 'Manage Images (max 3)')}</h4>
                    <input type="file" multiple accept="image/*" onChange={handleImageUpload} disabled={formData.imageUrls.length >= 3} />
                    <div style={{ display: 'flex', gap: '8px', marginTop: '10px' }}>
                        {formData.imageUrls.map((url, i) => (
                            <div key={i} style={{ position: 'relative', width: '80px', height: '55px', border: '1px solid #ccc', borderRadius: '4px', overflow: 'hidden' }}>
                                <img src={url} alt="preview" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                                <button type="button" onClick={() => handleRemoveImage(url)} style={{ position: 'absolute', top: '0', right: '0', backgroundColor: 'red', color: 'white', border: 'none', cursor: 'pointer' }}>✕</button>
                            </div>
                        ))}
                    </div>
                </div>

                <div style={sectionCard}>
                    <h4 style={{ margin: '0 0 10px' }}>🎭 {t('select_actors', 'Actors')}</h4>
                    <div style={staffListStyle}>
                        {allActors.map(a => <label key={a.id}><input type="checkbox" onChange={() => toggleStaff(a.id, 'actorIds')} /> {a.name}</label>)}
                    </div>
                </div>

                <div style={sectionCard}>
                    <h4 style={{ margin: '0 0 10px' }}>🎬 {t('select_directors', 'Directors')}</h4>
                    <div style={staffListStyle}>
                        {allDirectors.map(d => <label key={d.id}><input type="checkbox" onChange={() => toggleStaff(d.id, 'directorIds')} /> {d.name}</label>)}
                    </div>
                </div>

                <div style={sectionCard}>
                    <h4 style={{ margin: '0 0 10px' }}>✍️ {t('select_screenwriters', 'Screenwriters')}</h4>
                    <div style={staffListStyle}>
                        {allScreenwriters.map(s => <label key={s.id}><input type="checkbox" onChange={() => toggleStaff(s.id, 'screenwriterIds')} /> {s.name}</label>)}
                    </div>
                </div>

                <button type="submit" style={btnStyle}>{t('save_movie', 'Save Movie')}</button>
            </form>
        </div>
    );
}

const inputStyle = { padding: '12px', borderRadius: '6px', border: '1px solid #ccc' };
const sectionCard = { padding: '15px', backgroundColor: '#f8f4ff', borderRadius: '8px', border: '1px solid #c9b0e8' };
const staffListStyle = { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '5px', maxHeight: '100px', overflowY: 'auto' };
const btnStyle = { backgroundColor: '#27ae60', color: 'white', padding: '15px', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold' };