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
        try {
            const response = await Axios.get(`${BASE_URL}/staff/user/${userId}`);
            return response.data;
        } catch (err) {
            // if user is not a staff, the backend may return 400/404 — return null so callers can handle absence
            if (err?.response?.status === 400 || err?.response?.status === 404) return null
            throw err
        }
    },

    async createCalendarBlock({ title, startDateTime, endDateTime, staffId }) {
        const calendarBlockData = {
            title,
            startDateTime,
            endDateTime,
            staffId
        };
        const response = await Axios.post(`${BASE_URL}/calendar-blocks/create`, calendarBlockData);
        return response.data;
    },
    
    async getBookingsByCustomer(customerId) {
        const response = await Axios.get(`${BASE_URL}/bookings/customer/${customerId}`);
        return response.data;
    },
    
    async cancelBooking(bookingId) {
        const response = await Axios.post(`${BASE_URL}/bookings/cancel/${bookingId}`, {});
        return response.data;
    },

    async getUsers() {
        const response = await Axios.get(`${BASE_URL}/users`);
        return response.data;
    }
};