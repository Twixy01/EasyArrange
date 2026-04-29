import Axios from 'axios';
import { BASE_URL } from './apiClient';

export const shiftApi = {
    async getStaffShifts() {
        const response = await Axios.get(`${BASE_URL}/staff-shifts`);
        return response.data;
    },

    async getShiftsByStaff(staffId) {
        const response = await Axios.get(`${BASE_URL}/staff-shifts/staff/${staffId}`);
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
    }
};
