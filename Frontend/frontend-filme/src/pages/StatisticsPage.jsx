import React, { useState, useEffect } from 'react';
import { movieService } from '../services/movieService';
import { useTranslation } from 'react-i18next';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend, LineChart, Line } from 'recharts';

export default function StatisticsPage() {
    const { t } = useTranslation();
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const load = async () => {
            try {
                const moviesData = await movieService.getAll();
                setMovies(moviesData);
            } catch (err) {
                console.error('Failed to load stats data', err);
            } finally {
                setLoading(false);
            }
        };
        load();
    }, []);

    if (loading) return <p style={{ padding: '32px', color: '#888', fontFamily: "'Georgia', serif" }}>{t('loading', 'Loading Statistics...')}</p>;


    const genreCounts = movies.reduce((acc, movie) => {
        const g = movie.genre || 'UNKNOWN';
        acc[g] = (acc[g] || 0) + 1;
        return acc;
    }, {});
    const genreData = Object.keys(genreCounts).map(key => ({ name: key, count: genreCounts[key] }));

    const categoryCounts = movies.reduce((acc, movie) => {
        const c = (movie.category || 'UNKNOWN').replace('_', ' ');
        acc[c] = (acc[c] || 0) + 1;
        return acc;
    }, {});
    const categoryData = Object.keys(categoryCounts).map(key => ({ name: key, value: categoryCounts[key] }));

    const yearCounts = movies.reduce((acc, movie) => {
        const y = movie.releaseYear || movie.releseYear || movie.relese_year || 'N/A';
        if (y !== 'N/A') {
            acc[y] = (acc[y] || 0) + 1;
        }
        return acc;
    }, {});
    const yearData = Object.keys(yearCounts).sort().map(key => ({ year: key, count: yearCounts[key] }));

    const COLORS = ['#5a2d82', '#e84393', '#c9b0e8', '#3498db', '#f1c40f', '#e67e22'];

    return (
        <div style={{ padding: '32px', fontFamily: "'Georgia', serif" }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                <h1 style={{ margin: 0, color: 'var(--color-plum)', fontSize: '28px', borderBottom: '3px solid var(--color-rose)', paddingBottom: '8px' }}>
                    📊 {t('statistics', 'Statistics Dashboard')}
                </h1>
            </div>

            {movies.length === 0 ? (
                <p style={{ color: '#888' }}>{t('no_data', 'No data available to generate statistics.')}</p>
            ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '24px' }}>
                    
                    <div style={chartCardStyle}>
                        <h3 style={chartTitleStyle}>{t('movies_by_genre', 'Movies by Genre')}</h3>
                        <ResponsiveContainer width="100%" height={300}>
                            <BarChart data={genreData} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
                                <CartesianGrid strokeDasharray="3 3" stroke="#eee" />
                                <XAxis dataKey="name" stroke="#5a2d82" fontSize={12} />
                                <YAxis allowDecimals={false} stroke="#5a2d82" />
                                <Tooltip cursor={{ fill: '#f8f4ff' }} contentStyle={{ borderRadius: '8px', borderColor: '#c9b0e8' }} />
                                <Bar dataKey="count" fill="#e84393" radius={[4, 4, 0, 0]} name={t('movies_count', 'No. of Movies')} />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>

                    <div style={chartCardStyle}>
                        <h3 style={chartTitleStyle}>{t('movies_by_category', 'Movies by Category')}</h3>
                        <ResponsiveContainer width="100%" height={300}>
                            <PieChart>
                                <Pie 
                                    data={categoryData} 
                                    cx="50%" 
                                    cy="50%" 
                                    outerRadius={100} 
                                    fill="#8884d8" 
                                    dataKey="value" 
                                    label={({name, percent}) => `${name} ${(percent * 100).toFixed(0)}%`}
                                >
                                    {categoryData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                    ))}
                                </Pie>
                                <Tooltip contentStyle={{ borderRadius: '8px', borderColor: '#c9b0e8' }} />
                                <Legend />
                            </PieChart>
                        </ResponsiveContainer>
                    </div>

                    <div style={{ ...chartCardStyle, gridColumn: '1 / -1' }}>
                        <h3 style={chartTitleStyle}>{t('releases_per_year', 'Releases per Year')}</h3>
                        <ResponsiveContainer width="100%" height={300}>
                            <LineChart data={yearData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                                <CartesianGrid strokeDasharray="3 3" stroke="#eee" />
                                <XAxis dataKey="year" stroke="#5a2d82" />
                                <YAxis allowDecimals={false} stroke="#5a2d82" />
                                <Tooltip contentStyle={{ borderRadius: '8px', borderColor: '#c9b0e8' }} />
                                <Line type="monotone" dataKey="count" stroke="#5a2d82" strokeWidth={3} dot={{ r: 6, fill: '#e84393' }} activeDot={{ r: 8 }} name={t('movies_count', 'No. of Movies')} />
                            </LineChart>
                        </ResponsiveContainer>
                    </div>

                </div>
            )}
        </div>
    );
}

const chartCardStyle = {
    backgroundColor: 'white',
    border: '1px solid #e0d0f0',
    borderRadius: '12px',
    padding: '24px',
    boxShadow: '0 4px 12px rgba(0,0,0,0.05)'
};

const chartTitleStyle = {
    margin: '0 0 20px 0',
    color: '#5a2d82',
    textAlign: 'center',
    fontSize: '18px'
};