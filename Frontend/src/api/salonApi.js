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
    
    async updateShiftForStaffDay({staffId, day, startShift, endShift}){
        const updateData = {
            staffId,
            day,
            startShift,
            endShift
        };
        const response = await Axios.post(`${BASE_URL}/staff-shifts/update`, updateData);
        return response.data;
    },

    async deleteStaffShift({staffId, shiftId}) {
        const response = await Axios.delete(`${BASE_URL}/staff-shifts/${staffId}/${shiftId}`);
        return response.data;
    },

    async getStaffByUser(userId) {
        const response = await Axios.get(`${BASE_URL}/staff/user/${userId}`);
        return response.data;
    },

    async getBookingsByCustomer(customerId) {
        const response = await Axios.get(`${BASE_URL}/bookings/customer/${customerId}`);
        return response.data;
    },
    async cancelBooking(bookingId) {
        const response = await Axios.post(`${BASE_URL}/bookings/cancel/${bookingId}`, {});
        return response.data;
    }
};