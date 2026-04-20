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

    async updateShiftForStaffDay({ staffId, day, startShift, endShift }) {
        const updateData = {
            staffId,
            day,
            startShift,
            endShift
        };
        const response = await Axios.post(`${BASE_URL}/staff-shifts/update`, updateData);
        return response.data;
    },

    async deleteStaffShift({ staffId, shiftId }) {
        const response = await Axios.delete(`${BASE_URL}/staff-shifts/${staffId}/${shiftId}`);
        return response.data;
    },

    // Robust getStaffByUser: accept all statuses and translate 400/404 to `null` (means "not a staff")
    async getStaffByUser(userId) {
        const response = await Axios.get(`${BASE_URL}/staff/user/${userId}`, { validateStatus: () => true });
        // success ---> return data
        if (response.status >= 200 && response.status < 300) {
            return response.data;
        }
        //treat not-found or validation as "no staff" for this user
        if (response.status === 404 || response.status === 400) return null;
        //other statuses are unexpected ---> throw so callers can handle
        const e = new Error(`Server error: ${response.status} ${response.statusText}`);
        e.response = response;
        throw e;
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

    async getOverlappingBookings(staffId, start, end) {
        const response = await Axios.get(`${BASE_URL}/bookings/staff/${staffId}/overlaps`, {
            params: {
                start,
                end
            }
        })
        return response.data;
    },

    async getCalendarBlocksByStaff(staffId) {
        const response = await Axios.get(`${BASE_URL}/calendar-blocks/staff/${staffId}`);
        return response.data;
    },

    async deleteCalendarBlock(calendarBlockId) {
        const response = await Axios.delete(`${BASE_URL}/calendar-blocks/${calendarBlockId}`);
        return response.data;
    },

    async getUsers() {
        const response = await Axios.get(`${BASE_URL}/users`);
        return response.data;
    }
};