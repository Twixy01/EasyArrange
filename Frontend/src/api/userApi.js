import Axios from 'axios';
import { BASE_URL, getAuthHeaders, request } from './apiClient';

export const userApi = {
    async getUsers() {
        const response = await Axios.get(`${BASE_URL}/users`);
        return response.data;
    },

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

    async deleteUser(userId) {
        return request(() =>
            Axios.delete(`${BASE_URL}/users/${userId}`, {
                headers: getAuthHeaders()
            })
        );
    }
};

export const getUser = (userId) => userApi.getUser(userId);
export const updateUser = (userId, userPayload) => userApi.updateUser(userId, userPayload);
export const adminUpdateUser = (userId, userPayload) => userApi.adminUpdateUser(userId, userPayload);
export const deleteUser = (userId) => userApi.deleteUser(userId);
