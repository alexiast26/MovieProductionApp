import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { apiFetch } from '../api';

export default function Sidebar({ userRole, onLogout }) {
    const { t, i18n } = useTranslation();

    const runExport = async (type, format) => {
        try {
            let fileContent = '';
            let mimeType = 'text/plain;charset=utf-8;';

            if (type === 'movies') {
                const movies = await apiFetch('/movies/all');
                if (format === 'csv') {
                    fileContent = "ID,Titlu,An Lansare,Durata (min),Gen,Categorie\n";
                    movies.forEach(m => {
                        const year = m.releaseYear || m.releseYear || m.relese_year || 'N/A';
                        const safeName = m.name ? `"${m.name.replace(/"/g, '""')}"` : 'N/A';
                        fileContent += `${m.id},${safeName},${year},${m.duration},${m.genre},${m.category}\n`;
                    });
                    mimeType = 'text/csv;charset=utf-8;';
                } else if (format === 'json') {
                    fileContent = JSON.stringify(movies, null, 2);
                    mimeType = 'application/json';
                } else if (format === 'xml') {
                    fileContent = '<?xml version="1.0" encoding="UTF-8"?>\n<movies>\n';
                    movies.forEach(m => {
                        const year = m.releaseYear || m.releseYear || m.relese_year || 'N/A';
                        fileContent += `  <movie>\n`;
                        fileContent += `    <id>${m.id}</id>\n`;
                        fileContent += `    <name>${m.name || 'N/A'}</name>\n`;
                        fileContent += `    <releaseYear>${year}</releaseYear>\n`;
                        fileContent += `    <duration>${m.duration}</duration>\n`;
                        fileContent += `    <genre>${m.genre}</genre>\n`;
                        fileContent += `    <category>${m.category}</category>\n`;
                        fileContent += `  </movie>\n`;
                    });
                    fileContent += '</movies>';
                    mimeType = 'application/xml;charset=utf-8;';
                } else if (format === 'doc') {
                    fileContent = `<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">\n`;
                    fileContent += `<head><meta charset="utf-8"><title>Export Filme</title><style>table{border-collapse:collapse;width:100%;}th,td{border:1px solid #000;padding:6px;text-align:left;}th{background-color:#f2f2f2;}</style></head>\n`;
                    fileContent += `<body><h2>Lista Filme</h2>\n<table>\n`;
                    fileContent += `<tr><th>ID</th><th>Titlu</th><th>An</th><th>Durata</th><th>Gen</th><th>Categorie</th></tr>\n`;
                    movies.forEach(m => {
                        const year = m.releaseYear || m.releseYear || m.relese_year || 'N/A';
                        fileContent += `<tr><td>${m.id}</td><td>${m.name || 'N/A'}</td><td>${year}</td><td>${m.duration} min</td><td>${m.genre}</td><td>${m.category}</td></tr>\n`;
                    });
                    fileContent += `</table>\n</body>\n</html>`;
                    mimeType = 'application/msword';
                }
            } else if (type === 'users') {
                const users = await apiFetch('/users/all');
                if (format === 'csv') {
                    fileContent = "ID,Email,Role\n";
                    users.forEach(u => {
                        fileContent += `${u.id},${u.email},${u.role || u.userRole}\n`;
                    });
                    mimeType = 'text/csv;charset=utf-8;';
                }
            }

            const blob = new Blob([fileContent], { type: mimeType });
            const link = document.createElement('a');
            link.href = URL.createObjectURL(blob);
            link.download = `${type}_export.${format}`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } catch (err) {
            alert('Export failed: ' + err.message);
        }
    };

    return (
        <div style={{ 
            width: '300px',
            backgroundColor: 'var(--color-dark)', 
            color: 'var(--color-light)', 
            height: '100vh', 
            display: 'flex', 
            flexDirection: 'column', 
            padding: '25px',
            boxSizing: 'border-box' 
        }}>
            <h2 style={{ color: 'var(--color-rose)', margin: '0', fontSize: '28px' }}>Cinematics</h2>
            <p style={{ color: 'gray', fontSize: '14px', marginBottom: '30px' }}>Role: {userRole}</p>
            
            <nav style={{ display: 'flex', flexDirection: 'column', gap: '15px', flex: 1, overflowY: 'auto' }}>
                {(userRole === 'EMPLOYEE' || userRole === 'MANAGER' || userRole === 'WORKER') && (
                    <Link to="/movies" style={linkStyle}>🎬 {t('movies', 'Movies')}</Link>
                )}
                {userRole === 'MANAGER' && (
                    <>
                        <Link to="/staff" style={linkStyle}>🎭 {t('staff', 'Staff')}</Link>
                        <Link to="/statistics" style={linkStyle}>📊 {t('statistics', 'Statistics')}</Link>
                    </>
                )}
                {userRole === 'ADMIN' && (
                    <>
                        <Link to="/users" style={linkStyle}>👥 {t('users', 'Users')}</Link>
                        <Link to="/add-user" style={linkStyle}>👤 {t('add_user', 'Add User')}</Link>
                    </>
                )}

                <div style={{ marginTop: '30px', padding: '15px', backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: '8px' }}>
                    <div style={{ fontSize: '14px', color: 'var(--color-rose)', fontWeight: 'bold', textTransform: 'uppercase', marginBottom: '10px' }}>
                        {t('quick_export', 'Quick Export')}
                    </div>
                    
                    {(userRole === 'EMPLOYEE' || userRole === 'MANAGER' || userRole === 'WORKER') && (
                        <div style={{ marginTop: '10px' }}>
                            <div style={{ fontSize: '14px', marginBottom: '8px' }}>{t('export_movies', 'Movies Export')}:</div>
                            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px' }}>
                                {['csv', 'json', 'xml', 'doc'].map(f => (
                                    <button key={f} onClick={() => runExport('movies', f)} style={exportBtnStyle}>
                                        {f.toUpperCase()}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}

                    {userRole === 'ADMIN' && (
                        <div style={{ marginTop: '10px' }}>
                            <div style={{ fontSize: '14px', marginBottom: '8px' }}>{t('export_users', 'Users Export')}:</div>
                            <button onClick={() => runExport('users', 'csv')} style={{...exportBtnStyle, width: '100%', backgroundColor: 'var(--color-rose)'}}>
                                DOWNLOAD CSV
                            </button>
                        </div>
                    )}
                </div>
            </nav>

            <div style={{ marginTop: 'auto', borderTop: '1px solid var(--color-plum)', paddingTop: '20px' }}>
                <div style={{ display: 'flex', gap: '8px', marginBottom: '15px' }}>
                    {['ro', 'en', 'fr'].map(l => (
                        <button key={l} onClick={() => i18n.changeLanguage(l)} style={langBtnStyle}>
                            {l.toUpperCase()}
                        </button>
                    ))}
                </div>
                <button onClick={onLogout} className="btn btn-primary" style={{ width: '100%', padding: '12px', fontSize: '16px' }}>
                    🚪 {t('logout', 'Logout')}
                </button>
            </div>
        </div>
    );
}

const linkStyle = { 
    color: 'var(--color-light)', 
    textDecoration: 'none', 
    fontSize: '16px', 
    padding: '8px 0',
    display: 'block'
};

const exportBtnStyle = { 
    fontSize: '12px', 
    padding: '8px', 
    cursor: 'pointer', 
    backgroundColor: 'var(--color-plum)', 
    color: 'white', 
    border: 'none',
    borderRadius: '4px',
    fontWeight: 'bold'
};

const langBtnStyle = { 
    flex: 1, 
    fontSize: '12px', 
    padding: '8px',
    cursor: 'pointer',
    borderRadius: '4px',
    border: '1px solid var(--color-plum)',
    backgroundColor: 'transparent',
    color: 'var(--color-light)'
};