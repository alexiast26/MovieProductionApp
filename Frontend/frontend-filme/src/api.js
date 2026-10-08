const BASE_URL = 'http://localhost:8080/api'; // API Gateway-ul tău din Docker

export const apiFetch = async (endpoint, options = {}) => {
    const token = localStorage.getItem('jwt_token');
    const headers = {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` }),
        ...options.headers
    };

    const response = await fetch(`${BASE_URL}${endpoint}`, { ...options, headers });
    
    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Eroare la comunicarea cu serverul');
    }
    
    const text = await response.text();
    return text ? JSON.parse(text) : null;
};