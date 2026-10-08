import { apiFetch } from '../api';

export const staffService = {
    getAll: async (category) => {
        return await apiFetch(`/staff/${category}/all`);
    },
    
    searchByName: async (category, name) => {
        return await apiFetch(`/staff/${category}/search?name=${name}`);
    },
    
    // 👇 ACEASTA ESTE FUNCȚIA CARE ÎȚI LIPSEA ȘI GENERA EROAREA 👇
    create: async (category, data) => {
        return await apiFetch(`/staff/${category}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    delete: async (category, id) => {
        return await apiFetch(`/staff/${category}/${id}`, { method: 'DELETE' });
    },
    
    update: async (category, id, data) => {
        return await apiFetch(`/staff/${category}/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    getAssociatedMovies: async (endpoint) => {
        return await apiFetch(endpoint);
    }
};