import { apiFetch } from '../api';

export const userService = {
    login: async (username, password) => {
        return await apiFetch('/users/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
    },

    register: async (formData) => {
        return await apiFetch('/users/register', {
            method: 'POST',
            body: JSON.stringify(formData)
        });
    },

    getAll: async () => {
        return await apiFetch('/users/all');
    },

    delete: async (id) => {
        return await apiFetch(`/users/delete/${id}`, { method: 'DELETE' });
    },

    update: async (id, data) => {
        return await apiFetch(`/users/update/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },

    triggerNotification: async (notificationData) => {
        return await apiFetch('/api/notifications', {
            method: 'POST',
            body: JSON.stringify(notificationData)
        });
    }
};