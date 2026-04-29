import Axios from 'axios';
import { BASE_URL, getAuthHeaders, request } from './apiClient';

export const staffApi = {
    async getStaff() {
        const response = await Axios.get(`${BASE_URL}/staff`);
        return response.data;
    },

    async getStaffByService(serviceId) {
        const response = await Axios.get(`${BASE_URL}/staff-services/service/${serviceId}`);
        return response.data;
    },

    async getStaffByUser(userId) {
        const response = await Axios.get(`${BASE_URL}/staff/user/${userId}`, { 
            validateStatus: () => true 
        });
        if (response.status >= 200 && response.status < 300) {
            return response.data;
        }
        if (response.status === 404 || response.status === 400) return null;
        const e = new Error(`Server error: ${response.status} ${response.statusText}`);
        e.response = response;
        throw e;
    },

    async createStaff(payload) {
        return request(() =>
            Axios.post(`${BASE_URL}/staff/register`, payload, { 
                headers: getAuthHeaders(true) 
            })
        );
    },

    async getStaffByUserId(userId) {
        return request(() =>
            Axios.get(`${BASE_URL}/staff/user/${userId}`, { 
                headers: getAuthHeaders() 
            })
        );
    },

    async deleteStaff(staffId) {
        return request(() =>
            Axios.delete(`${BASE_URL}/staff/${staffId}`, { 
                headers: getAuthHeaders() 
            })
        );
    },

    async deleteStaffByUserId(userId) {
        try {
            const staff = await staffApi.getStaffByUserId(userId);
            if (!staff?.staffId) return null;
            return staffApi.deleteStaff(staff.staffId);
        } catch (error) {
            if (error?.status === 404) {
                return null;
            }
            throw error;
        }
    },

    async createStaffService(payload) {
        return request(() =>
            Axios.post(`${BASE_URL}/staff-services/create`, payload, { 
                headers: getAuthHeaders(true) 
            })
        );
    },

    async deleteStaffService(staffId, serviceId) {
        return request(() =>
            Axios.delete(`${BASE_URL}/staff-services/${staffId}/${serviceId}`, { 
                headers: getAuthHeaders() 
            })
        );
    }
};

export const createStaff = (payload) => staffApi.createStaff(payload);
export const getStaffByUserId = (userId) => staffApi.getStaffByUserId(userId);
export const deleteStaffByUserId = (userId) => staffApi.deleteStaffByUserId(userId);
export const createStaffService = (payload) => staffApi.createStaffService(payload);
export const deleteStaffService = (staffId, serviceId) => staffApi.deleteStaffService(staffId, serviceId);
