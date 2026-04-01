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

    if (!response.ok) {
        throw new Error('Failed to fetch shifts');
    }

    return await response.json();
};

export const getAvailableSlots = async (staffId, selectedDate) => {
    const shifts = await getShiftsByStaff(staffId);
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
};

function formatDayOfWeek(day) {
    const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    return days[day];
}

function formatTime(time) {
    return time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}
