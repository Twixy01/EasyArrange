import Axios from 'axios';
import { BASE_URL, getAuthHeaders, request } from './apiClient';

export const bookingApi = {
    async getAvailableSlots(staffId, selectedDate, serviceId) {
        const response = await Axios.get(`${BASE_URL}/bookings/staff/${staffId}/available-slots`, {
            params: {
                selectedDate,
                serviceId
            }
        });
        return response.data;
    },

    async createBooking({ staffId, userId, startDateTime, endDateTime, serviceId }) {
        const bookingData = {
            staffId,
            userId,
            startDateTime,
            endDateTime,
            serviceId
        };
        const response = await Axios.post(`${BASE_URL}/bookings/create`, bookingData);
        return response.data;
    },

    async getBookingsByUser(userId) {
        const response = await Axios.get(`${BASE_URL}/bookings/user/${userId}`);
        return response.data;
    },

    async getBookingsByStaff(staffId) {
        const response = await Axios.get(`${BASE_URL}/bookings/staff/${staffId}`);
        return response.data;
    },

    async cancelBooking(bookingId) {
        return request(() =>
            Axios.post(`${BASE_URL}/bookings/cancel/${bookingId}`, {}, {
                headers: getAuthHeaders()
            })
        );
    },

    async updateBookingStatus(bookingId, bookingUpdateBody, isStaff) {
        const response = await Axios.put(`${BASE_URL}/bookings/${bookingId}/${isStaff}`, bookingUpdateBody);
        return response.data;
    },

    async updateBooking(bookingId, bookingPayload) {
        return request(() =>
            Axios.put(`${BASE_URL}/bookings/${bookingId}`, bookingPayload, {
                headers: getAuthHeaders(true)
            })
        );
    },

    async getOverlappingBookings(staffId, start, end) {
        const response = await Axios.get(`${BASE_URL}/bookings/staff/${staffId}/overlaps`, {
            params: {
                start,
                end
            }
        })
        return response.data;
    }
};

export const cancelBooking = (bookingId) => bookingApi.cancelBooking(bookingId);
export const updateBooking = (bookingId, bookingPayload) => bookingApi.updateBooking(bookingId, bookingPayload);
