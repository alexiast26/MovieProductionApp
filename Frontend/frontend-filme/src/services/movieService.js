import { apiFetch } from '../api';

export const movieService = {
    // Primește și filtrele (query)
    getAll: async (queryString = '') => {
        return await apiFetch(`/movies${queryString ? '?' + queryString : '/all'}`);
    },

    create: async (movieData) => {
        return await apiFetch('/movies', {
            method: 'POST',
            body: JSON.stringify(movieData)
        });
    },

    delete: async (id) => {
        return await apiFetch(`/movies/${id}`, { method: 'DELETE' });
    },

    update: async (id, movieData) => {
        return await apiFetch(`/movies/${id}`, {
            method: 'PUT',
            body: JSON.stringify(movieData)
        });
    },

    uploadImage: async (id, base64Str, token) => {
        return await fetch(`http://localhost:8080/api/movies/${id}/images`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'text/plain' },
            body: base64Str
        });
    },

    addStaffToMovie: async (movieId, staffType, staffId) => {
        return await apiFetch(`/movies/${movieId}/${staffType}/${staffId}`, { method: 'POST' });
    },

    removeStaffFromMovie: async (movieId, staffType, staffId) => {
        return await apiFetch(`/movies/${movieId}/${staffType}/${staffId}`, { method: 'DELETE' });
    }
};