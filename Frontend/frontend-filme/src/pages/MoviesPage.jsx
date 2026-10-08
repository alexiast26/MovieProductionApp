import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { movieService } from '../services/movieService';
import { staffService } from '../services/staffService';

const GENRES = ['ALL', 'ACTION', 'COMEDY', 'DRAMA', 'HORROR', 'SCI_FI'];
const CATEGORIES = ['ALL', 'FEATURE_FILM', 'DOCUMENTARY', 'SHORT_FILM', 'TV_SERIES'];

const getActorIds = (movie) => {
    if (!movie) return [];
    let arr = [];
    if (Array.isArray(movie.actorIds) && movie.actorIds.length > 0) arr = movie.actorIds;
    else if (Array.isArray(movie.actor_ids) && movie.actor_ids.length > 0) arr = movie.actor_ids;
    else if (Array.isArray(movie.actors)) arr = movie.actors.map(a => typeof a === 'object' ? a.id : a);
    return arr.map(id => Number(id));
};

const getDirectorIds = (movie) => {
    if (!movie) return [];
    let arr = [];
    if (Array.isArray(movie.directorIds) && movie.directorIds.length > 0) arr = movie.directorIds;
    else if (Array.isArray(movie.director_ids) && movie.director_ids.length > 0) arr = movie.director_ids;
    else if (Array.isArray(movie.directors)) arr = movie.directors.map(d => typeof d === 'object' ? d.id : d);
    return arr.map(id => Number(id));
};

const getScreenwriterIds = (movie) => {
    if (!movie) return [];
    let arr = [];
    if (Array.isArray(movie.screenwriterIds) && movie.screenwriterIds.length > 0) arr = movie.screenwriterIds;
    else if (Array.isArray(movie.screenwriter_ids) && movie.screenwriter_ids.length > 0) arr = movie.screenwriter_ids;
    else if (Array.isArray(movie.screenwriters)) arr = movie.screenwriters.map(s => typeof s === 'object' ? s.id : s);
    return arr.map(id => Number(id));
};

const getMovieImages = (movie) => {
    if (!movie) return [];
    if (Array.isArray(movie.imageUrls) && movie.imageUrls.length > 0) return movie.imageUrls;
    if (Array.isArray(movie.image_urls) && movie.image_urls.length > 0) return movie.image_urls;
    if (Array.isArray(movie.images)) return movie.images.map(img => typeof img === 'object' ? (img.imageUrl || img.url) : img);
    return [];
};

export default function MoviesPage({ userRole }) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [movies, setMovies] = useState([]);
    const [error, setError] = useState('');
    const [search, setSearch] = useState('');
    const [genre, setGenre] = useState('ALL');
    const [category, setCategory] = useState('ALL');
    const [sortBy, setSortBy] = useState('');
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [loading, setLoading] = useState(true);

    const [showEditModal, setShowEditModal] = useState(false);
    const [editData, setEditData] = useState({});
    const [allActors, setAllActors] = useState([]);
    const [allDirectors, setAllDirectors] = useState([]);
    const [allScreenwriters, setAllScreenwriters] = useState([]);

    const [selectedActors, setSelectedActors] = useState([]);
    const [selectedDirectors, setSelectedDirectors] = useState([]);
    const [selectedScreenwriters, setSelectedScreenwriters] = useState([]);

    const filtered = movies.filter(m => m.name?.toLowerCase().includes(search.toLowerCase()));
    const isWorker = String(userRole).trim().toUpperCase() === 'WORKER';
    const canDoCRUD = ['WORKER', 'MANAGER', 'ADMIN'].includes(String(userRole).trim().toUpperCase());

    const fetchMovies = async () => {
        setLoading(true);
        try {
            const params = new URLSearchParams();
            if (genre !== 'ALL') params.append('genre', genre);
            if (category !== 'ALL') params.append('category', category);
            if (sortBy) params.append('sortBy', sortBy);
            
            const data = await movieService.getAll(params.toString());
            setMovies(data);
            setError('');
        } catch {
            setError(t('failed_load_movies', 'Failed to load movies.'));
        } finally {
            setLoading(false);
        }
    };

    const fetchStaffLists = async () => {
        try {
            const [actorsData, directorsData, screenwritersData] = await Promise.all([
                staffService.getAll('actors'),
                staffService.getAll('directors'),
                staffService.getAll('screenwriters')
            ]);
            setAllActors(actorsData);
            setAllDirectors(directorsData);
            setAllScreenwriters(screenwritersData);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => { 
        fetchMovies(); 
        fetchStaffLists(); 
    }, [genre, category, sortBy]);

    const handleDelete = async (e, id) => {
        e.stopPropagation(); 
        if (!window.confirm(t('confirm_delete_movie', 'Delete this movie?'))) return;
        try {
            await movieService.delete(id);
            setMovies(prev => prev.filter(m => m.id !== id));
            if (selectedMovie?.id === id) setSelectedMovie(null);
            alert(t('movie_deleted_success', 'Movie deleted successfully!'));
        } catch {
            alert(t('error_delete', 'Delete failed.'));
        }
    };

    const handleEditClick = (e, movie) => {
        e.stopPropagation(); 
        const actualYear = movie.releaseYear || movie.releseYear || movie.relese_year;
        const imgs = getMovieImages(movie);

        setEditData({ 
            ...movie,
            releseYear: actualYear, 
            imageUrls: imgs
        });

        setSelectedActors(getActorIds(movie));
        setSelectedDirectors(getDirectorIds(movie));
        setSelectedScreenwriters(getScreenwriterIds(movie));
        
        setShowEditModal(true);
    };

    const handleModalImageUpload = (e) => {
        const files = Array.from(e.target.files);
        if (editData.imageUrls.length + files.length > 3) {
            alert(t('max_images_error', 'Maximum 3 images allowed.'));
            return;
        }
        files.forEach(file => {
            const reader = new FileReader();
            reader.onloadend = () => {
                setEditData(prev => ({ ...prev, imageUrls: [...prev.imageUrls, reader.result] }));
            };
            reader.readAsDataURL(file);
        });
    };

    const handleModalRemoveImage = async (urlToRemove) => {
        setEditData(prev => ({ ...prev, imageUrls: prev.imageUrls.filter(url => url !== urlToRemove) }));
    };

    const toggleSelection = (id, list, setter) => {
        if (list.includes(id)) setter(list.filter(x => x !== id));
        else setter([...list, id]);
    };

    const handleEditSave = async () => {
        try {
            const originalMovie = movies.find(m => m.id === editData.id) || {};

            const moviePayload = {
                name: editData.name,
                releseYear: parseInt(editData.releseYear),
                releaseYear: parseInt(editData.releseYear), 
                duration: parseInt(editData.duration),
                genre: editData.genre,
                category: editData.category,
                actorIds: selectedActors, 
                directorIds: selectedDirectors,
                screenwriterIds: selectedScreenwriters
            };

            await movieService.update(editData.id, moviePayload);

            const token = localStorage.getItem('jwt_token');
            const originalImages = getMovieImages(originalMovie);
            
            for (const base64Str of editData.imageUrls) {
                if (base64Str.startsWith('data:image') && !originalImages.includes(base64Str)) {
                    try {
                        await movieService.uploadImage(editData.id, base64Str, token);
                    } catch(e) {}
                }
            }

            const originalActors = getActorIds(originalMovie);
            for (const id of originalActors) {
                if (!selectedActors.includes(id)) {
                    try { await movieService.removeStaffFromMovie(editData.id, 'actors', id); } catch(e){}
                }
            }
            for (const id of selectedActors) {
                if (!originalActors.includes(id)) {
                    try { await movieService.addStaffToMovie(editData.id, 'actors', id); } catch(e){}
                }
            }

            const originalDirectors = getDirectorIds(originalMovie);
            for (const id of originalDirectors) {
                if (!selectedDirectors.includes(id)) {
                    try { await movieService.removeStaffFromMovie(editData.id, 'directors', id); } catch(e){}
                }
            }
            for (const id of selectedDirectors) {
                if (!originalDirectors.includes(id)) {
                    try { await movieService.addStaffToMovie(editData.id, 'directors', id); } catch(e){}
                }
            }

            const originalScreenwriters = getScreenwriterIds(originalMovie);
            for (const id of originalScreenwriters) {
                if (!selectedScreenwriters.includes(id)) {
                    try { await movieService.removeStaffFromMovie(editData.id, 'screenwriters', id); } catch(e){}
                }
            }
            for (const id of selectedScreenwriters) {
                if (!originalScreenwriters.includes(id)) {
                    try { await movieService.addStaffToMovie(editData.id, 'screenwriters', id); } catch(e){}
                }
            }

            setShowEditModal(false);
            setSelectedMovie(null);
            fetchMovies();
            alert(t('save_changes', 'Saved Successfully!'));
        } catch (err) {
            alert(t('error_update', 'Failed to update: ') + err.message);
        }
    };

    const renderStaffDetails = (movie, roleType) => {
        let ids = [];
        let staffSource = [];
        
        if (roleType === 'actors') { ids = getActorIds(movie); staffSource = allActors; }
        if (roleType === 'directors') { ids = getDirectorIds(movie); staffSource = allDirectors; }
        if (roleType === 'screenwriters') { ids = getScreenwriterIds(movie); staffSource = allScreenwriters; }

        const staffList = staffSource.filter(person => ids.includes(person.id));

        if (staffList.length === 0) return <span style={{ color: 'gray', fontSize: '14px', marginLeft: '10px' }}>{t('none_assigned', 'None')}</span>;

        return (
            <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', marginTop: '8px' }}>
                {staffList.map(person => {
                    const photo = person.photoUrl || person.imageUrl || person.profilePicture;
                    return (
                        <div key={person.id} style={{ display: 'flex', alignItems: 'center', gap: '10px', backgroundColor: 'white', padding: '6px 12px', borderRadius: '8px', border: '1px solid #e0d0f0', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                            {photo ? (
                                <img src={photo} alt={person.name} style={{ width: '40px', height: '40px', borderRadius: '50%', objectFit: 'cover', border: '1px solid #ddd' }} />
                            ) : (
                                <div style={{ width: '40px', height: '40px', borderRadius: '50%', backgroundColor: '#c9b0e8', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px' }}>👤</div>
                            )}
                            <div style={{ display: 'flex', flexDirection: 'column' }}>
                                <span style={{ fontSize: '14px', fontWeight: 'bold', color: 'var(--color-plum)' }}>{person.name}</span>
                                {(person.age || person.gender) && (
                                    <span style={{ fontSize: '11px', color: '#666' }}>
                                        {person.age ? `${person.age} ${t('years', 'ani')}` : ''} 
                                        {person.age && person.gender ? ' | ' : ''}
                                        {person.gender ? person.gender : ''}
                                    </span>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>
        );
    };

    return (
        <div style={{ padding: '32px', fontFamily: "'Georgia', serif" }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                <h1 style={{ margin: 0, color: 'var(--color-plum)', fontSize: '28px', borderBottom: '3px solid var(--color-rose)', paddingBottom: '8px' }}>
                    🎬 {t('movies', 'Movies')}
                </h1>
                
                <div style={{ display: 'flex', gap: '12px' }}>
                    {isWorker && (
                        <button onClick={() => navigate('/add-movie')} style={{ backgroundColor: '#27ae60', color: 'white', padding: '12px 24px', borderRadius: '6px', border: 'none', cursor: 'pointer', fontWeight: 'bold', fontSize: '14px' }}>
                            ➕ {t('add_new_movie', 'Add New Movie')}
                        </button>
                    )}
                </div>
            </div>

            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', marginBottom: '24px', padding: '16px', backgroundColor: '#f8f4ff', borderRadius: '8px', border: '1px solid #e0d0f0' }}>
                <input placeholder={`🔍 ${t('search_by_title', 'Search by title...')}`} value={search} onChange={e => setSearch(e.target.value)} style={{ padding: '8px 12px', border: '1px solid var(--color-plum)', borderRadius: '6px', fontSize: '14px', flex: '1' }} />
                <select value={genre} onChange={e => setGenre(e.target.value)} style={selectStyle}>
                    {GENRES.map(g => <option key={g} value={g}>{g === 'ALL' ? t('all_genres', 'All Genres') : g}</option>)}
                </select>
                <select value={category} onChange={e => setCategory(e.target.value)} style={selectStyle}>
                    {CATEGORIES.map(c => <option key={c} value={c}>{c === 'ALL' ? t('all_categories', 'All Categories') : c.replace('_', ' ')}</option>)}
                </select>
            </div>

            {error && <p style={{ color: 'red' }}>{error}</p>}
            {loading && <p style={{ color: 'gray' }}>{t('loading', 'Loading...')}</p>}

            <div style={{ display: 'grid', gridTemplateColumns: selectedMovie ? '2fr 1fr' : '1fr', gap: '24px', alignItems: 'start' }}>
                
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: '20px' }}>
                    {filtered.map(movie => {
                        const movieYear = movie.releaseYear || movie.releseYear || movie.relese_year;
                        const imgs = getMovieImages(movie);
                        return (
                            <div 
                                key={movie.id} 
                                onClick={() => setSelectedMovie(movie)} 
                                style={{ 
                                    border: selectedMovie?.id === movie.id ? '2px solid #5a2d82' : '1px solid #ddd', 
                                    borderRadius: '10px', 
                                    overflow: 'hidden', 
                                    cursor: 'pointer', 
                                    backgroundColor: selectedMovie?.id === movie.id ? '#fbf8ff' : 'white', 
                                    boxShadow: '0 2px 8px rgba(0,0,0,0.08)' 
                                }}
                            >
                                {/* MODIFICARE AICI: objectFit changed from contain to cover */}
                                <div style={{ height: '220px', backgroundColor: '#1e1133', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                                    {imgs.length > 0 ? <img src={imgs[0]} alt={movie.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} /> : <span style={{ fontSize: '64px' }}>🎞️</span>}
                                </div>
                                <div style={{ padding: '12px' }}>
                                    <h3 style={{ margin: '0 0 6px', fontSize: '15px' }}>{movie.name}</h3>
                                    <p style={{ margin: '3px 0', fontSize: '12px', color: '#666' }}>📅 {movieYear} &nbsp;|&nbsp; ⏱ {movie.duration} min</p>
                                    
                                    {canDoCRUD && (
                                        <div style={{ display: 'flex', gap: '6px', marginTop: '10px' }}>
                                            <button onClick={(e) => handleEditClick(e, movie)} style={btnSmall('#5a2d82')}>✏️ {t('edit', 'Edit')}</button>
                                            <button onClick={(e) => handleDelete(e, movie.id)} style={btnSmall('#c0392b')}>🗑️ {t('delete', 'Delete')}</button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>

                {selectedMovie && (
                    <div style={{ 
                        position: 'sticky', 
                        top: '24px', 
                        padding: '24px', 
                        backgroundColor: '#f8f4ff', 
                        borderRadius: '10px', 
                        border: '1px solid #c9b0e8',
                        maxHeight: 'calc(100vh - 48px)',
                        overflowY: 'auto'
                    }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px', borderBottom: '1px solid #e0d0f0', paddingBottom: '16px' }}>
                            <div>
                                <h2 style={{ margin: '0 0 8px 0', color: 'var(--color-plum)', fontSize: '24px' }}>{selectedMovie.name}</h2>
                                <span style={{ fontSize: '14px', color: '#555' }}>
                                    <strong>{t('genre', 'Genre')}:</strong> {selectedMovie.genre} &nbsp;|&nbsp; <strong>{t('category', 'Category')}:</strong> {selectedMovie.category?.replace('_', ' ')}
                                </span>
                            </div>
                            <button onClick={() => setSelectedMovie(null)} style={{ background: 'none', border: 'none', fontSize: '24px', cursor: 'pointer', color: '#888' }}>✕</button>
                        </div>
                        
                        <div style={{ marginBottom: '20px' }}>
                            <div style={{ fontSize: '16px', fontWeight: 'bold', color: 'var(--color-dark)' }}>🎭 {t('actors', 'Actors')}</div>
                            {renderStaffDetails(selectedMovie, 'actors')}
                        </div>
                        
                        <div style={{ marginBottom: '20px' }}>
                            <div style={{ fontSize: '16px', fontWeight: 'bold', color: 'var(--color-dark)' }}>🎬 {t('directors', 'Directors')}</div>
                            {renderStaffDetails(selectedMovie, 'directors')}
                        </div>
                        
                        <div style={{ marginBottom: '20px' }}>
                            <div style={{ fontSize: '16px', fontWeight: 'bold', color: 'var(--color-dark)' }}>✍️ {t('screenwriters', 'Screenwriters')}</div>
                            {renderStaffDetails(selectedMovie, 'screenwriters')}
                        </div>

                        <div style={{ display: 'flex', gap: '10px', marginTop: '20px', paddingTop: '16px', borderTop: '1px solid #e0d0f0', flexWrap: 'wrap' }}>
                            {getMovieImages(selectedMovie).map((url, i) => (
                                // MODIFICARE AICI: objectFit changed from contain to cover
                                <div key={i} style={{ width: '100%', height: '250px', backgroundColor: '#1e1133', borderRadius: '8px', overflow: 'hidden', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
                                    <img src={url} alt="preview" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>

            {/* MODAL DE EDITARE */}
            {showEditModal && (
                <div style={modalOverlay}>
                    <div style={largeModalBox}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #ddd', paddingBottom: '12px', marginBottom: '16px' }}>
                            <h3 style={{ color: 'var(--color-plum)', margin: 0 }}>✏️ {t('edit_movie_staff', 'Edit Movie & Staff')}</h3>
                            <button onClick={() => setShowEditModal(false)} style={{ background: 'none', border: 'none', fontSize: '20px', cursor: 'pointer' }}>✕</button>
                        </div>

                        <div style={{ overflowY: 'auto', flex: 1, paddingRight: '8px', display: 'flex', flexDirection: 'column', gap: '14px' }}>
                            <input placeholder={t('movie_title', 'Movie Title')} value={editData.name || ''} onChange={e => setEditData({ ...editData, name: e.target.value })} style={inputStyle} />
                            <input placeholder={t('year', 'Release Year')} type="number" value={editData.releseYear || ''} onChange={e => setEditData({ ...editData, releseYear: e.target.value })} style={inputStyle} />
                            <input placeholder={t('duration_min', 'Duration')} type="number" value={editData.duration || ''} onChange={e => setEditData({ ...editData, duration: e.target.value })} style={inputStyle} />
                            
                            <select value={editData.genre || 'ACTION'} onChange={e => setEditData({ ...editData, genre: e.target.value })} style={inputStyle}>
                                {GENRES.filter(g => g !== 'ALL').map(g => <option key={g} value={g}>{g}</option>)}
                            </select>
                            <select value={editData.category || 'FEATURE_FILM'} onChange={e => setEditData({ ...editData, category: e.target.value })} style={inputStyle}>
                                {CATEGORIES.filter(c => c !== 'ALL').map(c => <option key={c} value={c}>{c}</option>)}
                            </select>

                            <div style={innerSectionCard}>
                                <h4 style={{ margin: '0 0 8px', fontSize: '14px' }}>📷 {t('manage_images', 'Manage Images (max 3)')}</h4>
                                <input type="file" accept="image/*" multiple onChange={handleModalImageUpload} disabled={editData.imageUrls?.length >= 3} style={{ width: '100%', marginBottom: '8px' }} />
                                <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                                    {editData.imageUrls?.map((url, i) => (
                                        // MODIFICARE AICI (doar în preview-ul modalului): objectFit cover, și scoatem contain
                                        <div key={i} style={{ position: 'relative', width: '80px', height: '55px', border: '1px solid #ccc', borderRadius: '4px', backgroundColor: '#1e1133', overflow: 'hidden' }}>
                                            <img src={url} alt="preview" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                                            <button type="button" onClick={() => handleModalRemoveImage(url)} style={{ position: 'absolute', top: '2px', right: '2px', backgroundColor: 'red', color: 'white', border: 'none', borderRadius: '50%', width: '16px', height: '16px', fontSize: '10px', cursor: 'pointer' }}>✕</button>
                                        </div>
                                    ))}
                                </div>
                            </div>

                            <div style={innerSectionCard}>
                                <h4 style={{ margin: '0 0 8px', fontSize: '14px' }}>🎭 {t('select_actors', 'Select Actors')}</h4>
                                <div style={staffScrollList}>
                                    {allActors.map(a => (
                                        <label key={a.id} style={staffCheckboxLabel}>
                                            <input type="checkbox" checked={selectedActors.includes(a.id)} onChange={() => toggleSelection(a.id, selectedActors, setSelectedActors)} /> {a.name}
                                        </label>
                                    ))}
                                </div>
                            </div>

                            <div style={innerSectionCard}>
                                <h4 style={{ margin: '0 0 8px', fontSize: '14px' }}>🎬 {t('select_directors', 'Select Directors')}</h4>
                                <div style={staffScrollList}>
                                    {allDirectors.map(d => (
                                        <label key={d.id} style={staffCheckboxLabel}>
                                            <input type="checkbox" checked={selectedDirectors.includes(d.id)} onChange={() => toggleSelection(d.id, selectedDirectors, setSelectedDirectors)} /> {d.name}
                                        </label>
                                    ))}
                                </div>
                            </div>

                            <div style={innerSectionCard}>
                                <h4 style={{ margin: '0 0 8px', fontSize: '14px' }}>✍️ {t('select_screenwriters', 'Select Screenwriters')}</h4>
                                <div style={staffScrollList}>
                                    {allScreenwriters.map(s => (
                                        <label key={s.id} style={staffCheckboxLabel}>
                                            <input type="checkbox" checked={selectedScreenwriters.includes(s.id)} onChange={() => toggleSelection(s.id, selectedScreenwriters, setSelectedScreenwriters)} /> {s.name}
                                        </label>
                                    ))}
                                </div>
                            </div>
                        </div>

                        <div style={{ display: 'flex', gap: '10px', marginTop: '16px', paddingTop: '12px', borderTop: '1px solid #ddd' }}>
                            <button onClick={handleEditSave} style={{ ...btnSmall('#5a2d82'), padding: '12px 20px', fontSize: '14px', flex: 1, fontWeight: 'bold' }}>💾 {t('save_changes', 'Save Changes')}</button>
                            <button onClick={() => setShowEditModal(false)} style={{ ...btnSmall('#888'), padding: '12px 20px', fontSize: '14px' }}>{t('cancel', 'Cancel')}</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

const selectStyle = { padding: '8px 12px', border: '1px solid var(--color-plum)', borderRadius: '6px', fontSize: '14px', backgroundColor: 'white' };
const btnSmall = (bg) => ({ backgroundColor: bg, color: 'white', border: 'none', borderRadius: '4px', padding: '5px 10px', fontSize: '12px', cursor: 'pointer' });
const modalOverlay = { position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 };
const largeModalBox = { backgroundColor: 'white', borderRadius: '10px', padding: '24px', width: '500px', maxHeight: '85vh', display: 'flex', flexDirection: 'column' };
const inputStyle = { padding: '10px', border: '1px solid #ccc', borderRadius: '6px', fontSize: '14px', width: '100%', boxSizing: 'border-box' };
const innerSectionCard = { backgroundColor: '#f8f4ff', borderRadius: '8px', padding: '12px', border: '1px solid #c9b0e8' };
const staffScrollList = { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '6px', maxHeight: '90px', overflowY: 'auto', backgroundColor: 'white', padding: '8px', border: '1px solid #ddd', borderRadius: '4px' };
const staffCheckboxLabel = { display: 'flex', alignItems: 'center', gap: '6px', fontSize: '13px', cursor: 'pointer' };