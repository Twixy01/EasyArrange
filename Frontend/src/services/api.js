const BASE_URL = 'http://localhost:8080/api';

async function fetchJsonOrThrow(url, options) {
  let res;
  let text;
  try {
    res = await fetch(url, options);
  } catch (netErr) {
    const e = new Error(`Network error: ${netErr?.message || netErr}`);
    e.payload = { detail: netErr?.message || String(netErr) };
    throw e;
  }

  text = await res.text();
  if (!res.ok) {
    // try parse JSON body, otherwise include raw text
    let parsed;
    try {
      parsed = JSON.parse(text || '{}')
    } catch {
      throw new Error(`HTTP ${res.status} ${res.statusText} - ${text}`)
    }
    const e = new Error(`HTTP ${res.status} ${res.statusText} - ${JSON.stringify(parsed)}`)
    e.payload = parsed
    throw e
  }
  try {
    return JSON.parse(text || 'null')
  } catch {
    return text
  }
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

//update a booking by id with the given payload (BookingUpdateRequest shape)
export const updateBooking = async (bookingId, bookingPayload) => {
  const token = (() => { try { return localStorage.getItem('token') } catch { return null } })();
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    },
    body: JSON.stringify(bookingPayload)
  }
  return fetchJsonOrThrow(`${BASE_URL}/bookings/${bookingId}`, options)
}

//cancel (delete) a booking by id
export const cancelBooking = async (bookingId) => {
  const token = (() => { try { return localStorage.getItem('token') } catch { return null } })();
  const options = {
    method: 'DELETE',
    headers: {
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    }
  }
  return fetchJsonOrThrow(`${BASE_URL}/bookings/${bookingId}`, options)
}

// permanently remove a cancelled booking (admin/owner action)
export const cancelHardBooking = async (bookingId) => {
  const token = (() => { try { return localStorage.getItem('token') } catch { return null } })();
  const options = {
    method: 'DELETE',
    headers: {
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    }
  }
  return fetchJsonOrThrow(`${BASE_URL}/bookings/${bookingId}/hard`, options)
}
