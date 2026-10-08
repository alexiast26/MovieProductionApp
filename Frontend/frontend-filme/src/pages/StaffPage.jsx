import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { staffService } from '../services/staffService';

const TABS = [
    { key: 'actors', label: '🎭 Actors', icon: '🎭', movieEndpoint: (id) => `/movies/actor/${id}` },
    { key: 'directors', label: '🎬 Directors', icon: '🎬', movieEndpoint: (id) => `/movies/director/${id}` },
    { key: 'screenwriters', label: '✍️ Screenwriters', icon: '✍️', movieEndpoint: (id) => `/movies/screenwriter/${id}` },
];

export default function StaffPage({ userRole }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [category, setCategory] = useState('actors');
    const [staffList, setStaffList] = useState([]);
    const [selected, setSelected] = useState(null);
    const [selectedMovies, setSelectedMovies] = useState([]);
    const [loadingMovies, setLoadingMovies] = useState(false);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    
    // Stări pentru Editare
    const [editMode, setEditMode] = useState(false);
    const [editData, setEditData] = useState({});

    const currentTab = TABS.find(t => t.key === category);

    const fetchStaff = async (searchQuery = '') => {
        try {
            const data = searchQuery.trim() !== '' 
                ? await staffService.searchByName(category, searchQuery) 
                : await staffService.getAll(category);
                
            setStaffList(data);
            setError('');
        } catch {
            setError(`Failed to load ${category}.`);
            setStaffList([]);
        }
    };

    useEffect(() => {
        setSelected(null);
        setSelectedMovies([]);
        setSearchTerm(''); 
        fetchStaff('');
    }, [category]);

    const handleSearchChange = (e) => {
        const value = e.target.value;
        setSearchTerm(value);
        fetchStaff(value);
    };

    const handleSelect = async (person) => {
        setSelected(person);
        setLoadingMovies(true);
        try {
            const endpoint = currentTab.movieEndpoint(person.id);
            const movies = await staffService.getAssociatedMovies(endpoint);
            
            console.log(`Movies recived for ${person.name}:`, movies);
            setSelectedMovies(movies);
        } catch (err) {
            console.error("Error at fetching movies for", person.name, err);
            setSelectedMovies([]);
        } finally {
            setLoadingMovies(false);
        }
    };

    const handleDelete = async (person) => {
        if (!window.confirm(`Delete ${person.name}?`)) return;
        try {
            await staffService.delete(category, person.id);
            setStaffList(prev => prev.filter(p => p.id !== person.id));
            if (selected?.id === person.id) setSelected(null);
        } catch {
            alert('Failed to delete staff member.');
        }
    };

    const handleEdit = (person) => {
        setEditData({ ...person, image_url: person.image_url || person.photoUrl || '' });
        setEditMode(true);
    };

    const handleModalImageUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setEditData(prev => ({ ...prev, image_url: reader.result }));
            };
            reader.readAsDataURL(file);
        }
    };

    const handleEditSave = async () => {
        try {
            const updated = await staffService.update(category, editData.id, {
                name: editData.name, 
                age: parseInt(editData.age), 
                gender: editData.gender,
                image_url: editData.image_url
            });
            setStaffList(prev => prev.map(p => p.id === updated.id ? updated : p));
            setEditMode(false);
            if (selected?.id === updated.id) setSelected(updated);
        } catch {
            alert('Failed to update staff member.');
        }
    };

    const canDoCRUD = ['WORKER', 'MANAGER', 'ADMIN'].includes(userRole);

    return (
        <div style={{ padding: '32px', fontFamily: "'Georgia', serif" }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                <h1 style={{ margin: 0, color: 'var(--color-plum)', borderBottom: '3px solid var(--color-rose)', paddingBottom: '8px' }}>
                    👥 {t('staff_management', 'Staff Management')}
                </h1>
                {canDoCRUD && (
                    <button onClick={() => navigate('/add-staff')} style={{ backgroundColor: '#27ae60', color: 'white', padding: '12px 24px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontWeight: 'bold', fontSize: '14px' }}>
                        ➕ Add New Staff
                    </button>
                )}
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px', flexWrap: 'wrap', gap: '16px' }}>
                <div style={{ display: 'flex', gap: '10px' }}>
                    {TABS.map(tab => (
                        <button key={tab.key} onClick={() => setCategory(tab.key)} style={{ padding: '10px 20px', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold', backgroundColor: category === tab.key ? 'var(--color-plum)' : '#e8d5f5', color: category === tab.key ? 'white' : 'var(--color-plum)', transition: '0.3s' }}>
                            {tab.label}
                        </button>
                    ))}
                </div>

                <input 
                    type="text"
                    placeholder={`🔍 Caută în ${currentTab.label.split(' ')[1]}...`}
                    value={searchTerm}
                    onChange={handleSearchChange}
                    style={{ padding: '10px 16px', width: '300px', borderRadius: '20px', border: '1px solid #c9b0e8', outline: 'none', fontSize: '14px', backgroundColor: '#fdfbfe' }}
                />
            </div>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <div style={{ display: 'grid', gridTemplateColumns: selected ? '2fr 1fr' : '1fr', gap: '24px', alignItems: 'start' }}>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: '16px', alignContent: 'start' }}>
                    {staffList.length > 0 ? staffList.map(person => {
                        const photo = person.image_url || person.photoUrl;
                        return (
                            <div key={person.id} onClick={() => handleSelect(person)} style={{ border: selected?.id === person.id ? '2px solid var(--color-plum)' : '1px solid #ddd', borderRadius: '10px', padding: '16px', textAlign: 'center', cursor: 'pointer', backgroundColor: selected?.id === person.id ? '#fbf8ff' : 'white', transition: '0.2s', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
                                
                                <div style={{ width: '80px', height: '80px', borderRadius: '50%', backgroundColor: '#2d1b4e', display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden', margin: '0 auto 12px', border: '2px solid #e0d0f0' }}>
                                    {photo ? (
                                        <img src={photo} alt={person.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                                    ) : (
                                        <span style={{ fontSize: '32px' }}>{currentTab.icon}</span>
                                    )}
                                </div>

                                <p style={{ margin: '0 0 6px', fontWeight: 'bold', fontSize: '15px', color: 'var(--color-plum)' }}>{person.name}</p>
                                {(person.age || person.gender) && (
                                    <p style={{ margin: 0, fontSize: '12px', color: '#888' }}>
                                        {person.age ? `${person.age} ${t('years', 'ani')}` : ''} 
                                        {person.age && person.gender ? ' · ' : ''}
                                        {person.gender ? person.gender : ''}
                                    </p>
                                )}
                                
                                {canDoCRUD && (
                                    <div style={{ display: 'flex', gap: '6px', marginTop: '16px', borderTop: '1px solid #eee', paddingTop: '12px' }} onClick={e => e.stopPropagation()}>
                                        <button onClick={() => handleEdit(person)} style={btnEdit}>✏️ {t('edit', 'Edit')}</button>
                                        <button onClick={() => handleDelete(person)} style={btnDelete}>🗑️</button>
                                    </div>
                                )}
                            </div>
                        );
                    }) : (
                        <p style={{ color: '#888', gridColumn: '1 / -1', textAlign: 'center', marginTop: '20px' }}>Nu a fost găsit niciun rezultat.</p>
                    )}
                </div>

                {selected && (
                    <div style={{ backgroundColor: '#f8f4ff', borderRadius: '10px', padding: '24px', border: '1px solid #c9b0e8', position: 'sticky', top: '24px' }}>
                        <div style={{ display: 'flex', gap: '16px', alignItems: 'center', marginBottom: '20px', borderBottom: '1px solid #e0d0f0', paddingBottom: '16px' }}>
                            <div style={{ width: '70px', height: '70px', borderRadius: '50%', overflow: 'hidden', border: '2px solid var(--color-plum)', backgroundColor: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                                {(selected.image_url || selected.photoUrl) ? <img src={selected.image_url || selected.photoUrl} alt={selected.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} /> : <span style={{ fontSize: '30px' }}>👤</span>}
                            </div>
                            <div>
                                <h2 style={{ margin: 0, color: 'var(--color-plum)', fontSize: '20px' }}>{selected.name}</h2>
                                <p style={{ margin: '4px 0 0', color: '#666', fontSize: '13px' }}>
                                    {selected.age ? `${selected.age} ${t('years', 'ani')}` : ''} 
                                    {selected.age && selected.gender ? ' · ' : ''}
                                    {selected.gender ? selected.gender : ''}
                                </p>
                            </div>
                        </div>

                        <h3 style={{ color: 'var(--color-plum)', marginBottom: '16px', fontSize: '16px' }}>🎬 {t('films_list', 'Films List')}</h3>
                        {loadingMovies ? <p style={{ color: '#888', fontSize: '13px' }}>{t('loading', 'Loading...')}</p> : selectedMovies.length > 0 ? (
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                                {selectedMovies.map(movie => {
                                    const movieYear = movie.releaseYear || movie.releseYear || movie.relese_year;
                                    return (
                                        <div key={movie.id} style={{ backgroundColor: 'white', borderRadius: '8px', padding: '12px 16px', border: '1px solid #e0d0f0', display: 'flex', justifyContent: 'space-between', alignItems: 'center', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                                            <div style={{ display: 'flex', flexDirection: 'column' }}>
                                                <span style={{ fontWeight: 'bold', fontSize: '14px', color: '#333' }}>{movie.name}</span>
                                                <span style={{ fontSize: '12px', color: '#888', marginTop: '2px' }}>{movie.genre}</span>
                                            </div>
                                            <span style={{ fontSize: '12px', fontWeight: 'bold', color: 'white', backgroundColor: 'var(--color-plum)', padding: '4px 8px', borderRadius: '12px' }}>
                                                {movieYear}
                                            </span>
                                        </div>
                                    );
                                })}
                            </div>
                        ) : <p style={{ color: '#999', fontSize: '14px' }}>Nu joacă în niciun film înregistrat.</p>}
                    </div>
                )}
            </div>

            {editMode && (
                <div style={modalOverlay}>
                    <div style={modalBox}>
                        <h3 style={{ margin: '0 0 10px', color: 'var(--color-plum)' }}>✏️ {t('edit_staff_member', 'Edit Staff Member')}</h3>
                        <input value={editData.name || ''} onChange={e => setEditData({ ...editData, name: e.target.value })} style={inputStyle} />
                        <input type="number" value={editData.age || ''} onChange={e => setEditData({ ...editData, age: e.target.value })} style={inputStyle} />
                        <select value={editData.gender || 'MALE'} onChange={e => setEditData({ ...editData, gender: e.target.value })} style={inputStyle}>
                            <option value="MALE">{t('male', 'MALE')}</option>
                            <option value="FEMALE">{t('female', 'FEMALE')}</option>
                        </select>

                        <label style={{ fontSize: '12px', color: 'gray', fontWeight: 'bold', marginTop: '5px' }}>📷 {t('change_photo', 'Change Photo')}:</label>
                        <input type="file" accept="image/*" onChange={handleModalImageUpload} />

                        <div style={{ display: 'flex', gap: '10px', marginTop: '8px' }}>
                            <button onClick={handleEditSave} style={{ backgroundColor: 'var(--color-plum)', color: 'white', border: 'none', borderRadius: '6px', padding: '10px 20px', cursor: 'pointer', fontWeight: 'bold', flex: 1 }}>{t('save', 'Save')}</button>
                            <button onClick={() => setEditMode(false)} style={{ backgroundColor: '#888', color: 'white', border: 'none', borderRadius: '6px', padding: '10px 20px', cursor: 'pointer' }}>{t('cancel', 'Cancel')}</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

const btnEdit = { backgroundColor: '#5a2d82', color: 'white', border: 'none', borderRadius: '4px', padding: '6px 10px', fontSize: '12px', cursor: 'pointer', flex: 1, fontWeight: 'bold' };
const btnDelete = { backgroundColor: '#c0392b', color: 'white', border: 'none', borderRadius: '4px', padding: '6px 10px', fontSize: '12px', cursor: 'pointer', fontWeight: 'bold' };
const modalOverlay = { position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 };
const modalBox = { backgroundColor: 'white', padding: '24px', borderRadius: '8px', display: 'flex', flexDirection: 'column', gap: '10px', width: '300px' };
const inputStyle = { padding: '10px', border: '1px solid #ccc', borderRadius: '4px', fontSize: '14px' };