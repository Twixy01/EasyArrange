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