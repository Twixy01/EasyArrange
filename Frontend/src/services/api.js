import Axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

function getToken() {
    try {
        return localStorage.getItem('token');
    } catch {
        return null;
    }
}

function getAuthHeaders(includeJson = false) {
    const token = getToken();
    return {
        ...(includeJson ? { 'Content-Type': 'application/json' } : {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {})
    };
}

function normalizeAxiosError(error) {
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

async function request(call) {
    try {
        const response = await call();
        return response.data;
    } catch (error) {
        normalizeAxiosError(error);
    }
}

export const api = {
    async getUser(userId) {
        return request(() => Axios.get(`${BASE_URL}/users/${userId}`));
    },

    async updateUser(userId, userPayload) {
        return request(() =>
            Axios.put(`${BASE_URL}/users/me`, userPayload, {
                headers: getAuthHeaders(true)
            })
        );
    },

    async adminUpdateUser(userId, userPayload) {
        return request(() =>
            Axios.put(`${BASE_URL}/users/admin/${userId}`, userPayload, {
                headers: getAuthHeaders(true)
            })
        );
    },

    async updateBooking(bookingId, bookingPayload) {
        return request(() =>
            Axios.put(`${BASE_URL}/bookings/${bookingId}`, bookingPayload, {
                headers: getAuthHeaders(true)
            })
        );
    },

    async cancelBooking(bookingId) {
        return request(() =>
            Axios.post(`${BASE_URL}/bookings/cancel/${bookingId}`, {
                headers: getAuthHeaders()
            })
        );
    },

    async deleteUser(userId) {
        return request(() =>
            Axios.delete(`${BASE_URL}/users/${userId}`, {
                headers: getAuthHeaders()
            })
        );
    },

    // --- Service management wrappers ---
    async createService(servicePayload) {
        // backend expects POST /api/services/create
        return request(() =>
            Axios.post(`${BASE_URL}/services/create`, servicePayload, { headers: getAuthHeaders(true) })
        );
    },

    async updateService(serviceId, servicePayload) {
        return request(() =>
            Axios.put(`${BASE_URL}/services/${serviceId}`, servicePayload, { headers: getAuthHeaders(true) })
        );
    },

    async deleteService(serviceId) {
        return request(() =>
            Axios.delete(`${BASE_URL}/services/${serviceId}`, { headers: getAuthHeaders() })
        );
    },

    // --- Staff-Service management ---
    async createStaffService(payload) {
        // payload: { staffId, serviceId }
        return request(() =>
            Axios.post(`${BASE_URL}/staff-services/create`, payload, { headers: getAuthHeaders(true) })
        );
    },

    async deleteStaffService(staffId, serviceId) {
        return request(() =>
            Axios.delete(`${BASE_URL}/staff-services/${staffId}/${serviceId}`, { headers: getAuthHeaders() })
        );
    }
};

// Utility: resolve a media/profile picture path returned by backend into a full URL.
// Backend sometimes returns relative paths like "/user/18" or "/api/user/18" — this helper
// ensures the browser requests the correct host:port by prefixing the API base if needed.
export function resolveMediaUrl(path) {
    if (!path) return null;
    try {
        // Already absolute URL
        const u = new URL(path, window.location.href);
        // If the path had a protocol (http/https), return as-is
        if (u.protocol === 'http:' || u.protocol === 'https:') return u.href;
        // Otherwise fall through and manually prefix BASE_URL
    } catch (_) {
        // ignore
    }
    // If path starts with a leading slash, prefix with server origin + '/api' trimmed
    if (path.startsWith('/')) {
        // If path already contains '/api', just prefix origin
        if (path.startsWith('/api')) {
            return `${window.location.protocol}//${window.location.host}${path}`;
        }
        // otherwise prefix with BASE_URL which already contains /api
        return `${BASE_URL}${path.startsWith('/') ? '' : '/'}${path.replace(/^\//, '')}`;
    }
    // fallback: assume it's a relative path under API
    return `${BASE_URL}/${path}`;
}

export const getUser = (userId) => api.getUser(userId);
export const updateUser = (userPayload) => api.updateUser(userPayload);
export const adminUpdateUser = (userId, userPayload) => api.adminUpdateUser(userId, userPayload);
export const updateBooking = (bookingId, bookingPayload) => api.updateBooking(bookingId, bookingPayload);
export const cancelBooking = (bookingId) => api.cancelBooking(bookingId);
export const deleteUser = (userId) => api.deleteUser(userId);

// service exports
export const createService = (payload) => api.createService(payload);
export const updateService = (serviceId, payload) => api.updateService(serviceId, payload);
export const deleteService = (serviceId) => api.deleteService(serviceId);

// staff-service exports
export const createStaffService = (payload) => api.createStaffService(payload);
export const deleteStaffService = (staffId, serviceId) => api.deleteStaffService(staffId, serviceId);
