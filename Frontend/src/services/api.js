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

    const detail =
        payload?.detail ||
        payload?.message ||
        error?.message ||
        'Request failed';

    const normalized = new Error(
        status ? `HTTP ${status}${statusText ? ` ${statusText}` : ''} - ${detail}` : detail
    );
    normalized.payload = payload || { detail };
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
            Axios.put(`${BASE_URL}/users/${userId}`, userPayload, {
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
    }
};

export const getUser = (userId) => api.getUser(userId);
export const updateUser = (userId, userPayload) => api.updateUser(userId, userPayload);
export const adminUpdateUser = (userId, userPayload) => api.adminUpdateUser(userId, userPayload);
export const updateBooking = (bookingId, bookingPayload) => api.updateBooking(bookingId, bookingPayload);
export const cancelBooking = (bookingId) => api.cancelBooking(bookingId);
export const deleteUser = (userId) => api.deleteUser(userId);
