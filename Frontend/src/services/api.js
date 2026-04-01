const BASE_URL = 'http://localhost:8080/api';

export const getServices = async () => {
    const response = await fetch(`${BASE_URL}/services`);
    const data = await response.json();
    return data;
}

export const getStaff = async () => {
    const response = await fetch(`${BASE_URL}/staff`);
    const data = await response.json();
    return data;
}

export const getStaffShifts = async () => {
    const response = await fetch(`${BASE_URL}/staff-shifts`);
    const data = await response.json();
    return data;
}

export const getStaffByService = async (serviceId) => {
    const response = await fetch(`${BASE_URL}/staff-services/service/${serviceId}`);
    const data = await response.json();
    return data;
}

const getShiftsByStaff = async (staffId) => {
    const response = await fetch(`${BASE_URL}/staff-shifts/staff/${staffId}`);
    const data = await response.json();
    return data;
}

export const getAvailableSlots = async (staffId, date) => {
    const shifts = await getShiftsByStaff(staffId);
    const selectedDate = new Date(date);

    const shiftOfSelectedDate = shifts.filter(shift => {
        shift.day == date.getDate().day()
    })
    const startOfDay = new Date(selectedDate);
    startOfDay.setHours(0, 0, 0, 0);
    const slots = [];

    return shifts;
}
