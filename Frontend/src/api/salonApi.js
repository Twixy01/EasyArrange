import Axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

export const salonApi = {
    async getServices(){
        const response = await Axios.get(`${BASE_URL}/services`);
        return response.data;
    },

    async getStaff(){
        const response = await Axios.get(`${BASE_URL}/staff`);
        return response.data;
    },

    async getStaffShifts(){
        const response = await Axios.get(`${BASE_URL}/staff-shifts`);
        return response.data;
    },

    async getStaffByService(serviceId){
        const response = await Axios.get(`${BASE_URL}/staff-services/service/${serviceId}`);
        return response.data;
    },

    async getShiftsByStaff(staffId) {
        const response = await Axios.get(`${BASE_URL}/staff-shifts/staff/${staffId}`);
        return response.data;
    },

    async getAvailableSlots(staffId, selectedDate) {
        const shifts = await this.getShiftsByStaff(staffId);
        const selectedDateObj = new Date(selectedDate);

        const shiftOfSelectedDate = shifts.find(
            shift => shift.day === formatDayOfWeek(selectedDateObj.getDay())
        );

        if (!shiftOfSelectedDate) {
            return [];
        }

        const startOfDay = new Date(`${selectedDate}T${shiftOfSelectedDate.startShift}`);
        const endOfDay = new Date(`${selectedDate}T${shiftOfSelectedDate.endShift}`);

        const slots = [];

        for (
            let time = new Date(startOfDay);
            time < endOfDay;
            time.setMinutes(time.getMinutes() + 15)
        ) {
            slots.push(
                {
                    start: formatTime(time),
                    label: formatTime(time)
                }
            );
        }

        return slots;
    }
}

function formatDayOfWeek(day) {
    const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    return days[day];
}

function formatTime(time) {
    return time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}
