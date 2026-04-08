const BASE_URL = 'http://localhost:8080/api';

async function fetchJsonOrThrow(url, options) {
  const res = await fetch(url, options);
  const text = await res.text();
  if (!res.ok) {
    // try parse JSON body, otherwise include raw text
    let parsed;
    try {
      parsed = JSON.parse(text || '{}')
    } catch {
      const err = new Error(`HTTP ${res.status} ${res.statusText} - ${text}`)
      throw err
    }
    const err = new Error(`HTTP ${res.status} ${res.statusText} - ${JSON.stringify(parsed)}`)
    err.payload = parsed
    throw err
  }
  try {
    return JSON.parse(text || 'null')
  } catch {
    return text
  }
}

export const getServices = async () => {
  return fetchJsonOrThrow(`${BASE_URL}/services`)
}

export const getStaff = async () => {
  return fetchJsonOrThrow(`${BASE_URL}/staff`)
}

export const getStaffShifts = async () => {
  return fetchJsonOrThrow(`${BASE_URL}/staff-shifts`)
}

export const getStaffByService = async (serviceId) => {
  return fetchJsonOrThrow(`${BASE_URL}/staff-services/service/${serviceId}`)
}

const getShiftsByStaff = async (staffId) => {
  return fetchJsonOrThrow(`${BASE_URL}/staff-shifts/staff/${staffId}`)
}

export const getAvailableSlots = async (staffId, date) => {
  if (!staffId) return [];
  const shifts = await getShiftsByStaff(staffId);
  if (!Array.isArray(shifts)) return [];
  if (!date) return shifts;

  const selectedDate = new Date(date);
  const selectedDateString = selectedDate.toDateString();

  // return shifts that start on the selected date
  const shiftOfSelectedDate = shifts.filter(shift => {
    if (!shift || !shift.startTime) return false;
    const shiftDate = new Date(shift.startTime);
    return shiftDate.toDateString() === selectedDateString;
  });

  return shiftOfSelectedDate;
}

export const getUser = async (userId) => {
  return fetchJsonOrThrow(`${BASE_URL}/users/${userId}`)
}

export const getBookingsByCustomer = async (customerId) => {
  return fetchJsonOrThrow(`${BASE_URL}/bookings/customer/${customerId}`)
}

export const updateUser = async (userId, userPayload) => {
  const token = (() => { try { return localStorage.getItem('token') } catch { return null } })();
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    },
    body: JSON.stringify(userPayload)
  }
  return fetchJsonOrThrow(`${BASE_URL}/users/${userId}`, options)
}
