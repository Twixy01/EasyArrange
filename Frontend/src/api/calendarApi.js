import Axios from 'axios';
import { BASE_URL } from './apiClient';

export const calendarApi = {
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

    async getCalendarBlocksByStaff(staffId) {
        const response = await Axios.get(`${BASE_URL}/calendar-blocks/staff/${staffId}`);
        return response.data;
    },

    async deleteCalendarBlock(calendarBlockId) {
        const response = await Axios.delete(`${BASE_URL}/calendar-blocks/${calendarBlockId}`);
        return response.data;
    }
};
