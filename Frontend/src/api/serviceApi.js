import Axios from 'axios';
import { BASE_URL, getAuthHeaders, request } from './apiClient';

export const serviceApi = {
    async getServices() {
        const response = await Axios.get(`${BASE_URL}/services`);
        return response.data;
    },

    async createService(servicePayload) {
        return request(() =>
            Axios.post(`${BASE_URL}/services/create`, servicePayload, { 
                headers: getAuthHeaders(true) 
            })
        );
    },

    async updateService(serviceId, servicePayload) {
        return request(() =>
            Axios.put(`${BASE_URL}/services/${serviceId}`, servicePayload, { 
                headers: getAuthHeaders(true) 
            })
        );
    },

    async deleteService(serviceId) {
        return request(() =>
            Axios.delete(`${BASE_URL}/services/${serviceId}`, { 
                headers: getAuthHeaders() 
            })
        );
    }
};

export const createService = (payload) => serviceApi.createService(payload);
export const updateService = (serviceId, payload) => serviceApi.updateService(serviceId, payload);
export const deleteService = (serviceId) => serviceApi.deleteService(serviceId);
