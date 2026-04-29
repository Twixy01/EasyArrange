import Axios from 'axios';

export const BASE_URL = 'http://localhost:8080/api';

export function getToken() {
    try {
        return localStorage.getItem('token');
    } catch {
        return null;
    }
}

export function getAuthHeaders(includeJson = false) {
    const token = getToken();
    return {
        ...(includeJson ? { 'Content-Type': 'application/json' } : {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {})
    };
}

export function normalizeAxiosError(error) {
    const payload = error?.response?.data;
    const status = error?.response?.status;
    const statusText = error?.response?.statusText;

    const userMessage =
        payload?.detail ||
        payload?.message ||
        'Request failed';
    const debugDetail = payload?.detail || payload?.message || error?.message || 'Request failed';

    const normalized = new Error(
        status ? `HTTP ${status}${statusText ? ` ${statusText}` : ''} - ${debugDetail}` : debugDetail
    );
    normalized.userMessage = userMessage;
    normalized.status = status;
    normalized.payload = payload || { detail: userMessage };
    console.error('API request failed', {
        status,
        statusText,
        url: error?.config?.url,
        method: error?.config?.method,
        payload: normalized.payload
    });
    throw normalized;
}

export async function request(call) {
    try {
        const response = await call();
        return response.data;
    } catch (error) {
        normalizeAxiosError(error);
    }
}

export default Axios;
