import Axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

export const salonApi = {
    async getServices() {
        const response = await Axios.get(`${BASE_URL}/services`);
        return response.data;
    },

    async getStaff() {
        const response = await Axios.get(`${BASE_URL}/staff`);
        return response.data;
    },

    async getStaffShifts() {
        const response = await Axios.get(`${BASE_URL}/staff-shifts`);
        return response.data;
    },

    async getStaffByService(serviceId) {
        const response = await Axios.get(`${BASE_URL}/staff-services/service/${serviceId}`);
        return response.data;
    },

    async getShiftsByStaff(staffId) {
        const response = await Axios.get(`${BASE_URL}/staff-shifts/staff/${staffId}`);
        return response.data;
    },

    async getAvailableSlots(staffId, selectedDate, serviceId) {
        const response = await Axios.get(`${BASE_URL}/bookings/staff/${staffId}/available-slots`, {
            params: {
                selectedDate,
                serviceId
            }
        });
        return response.data;
    },

    async createBooking({ staffId, customerId, startDateTime, endDateTime, serviceId }) {
        const bookingData = {
            staffId,
            customerId,
            startDateTime,
            endDateTime,
            serviceId
        };
        const response = await Axios.post(`${BASE_URL}/bookings/create`, bookingData);
        return response.data;
    },
    
};