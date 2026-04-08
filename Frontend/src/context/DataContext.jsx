/* eslint-disable react-refresh/only-export-components */
import { getStaff, getServices } from '../services/api'
import { createContext, useState, useEffect } from 'react'

export const DataContext = createContext()

const FALLBACK_SERVICES = [
  { serviceId: 1, name: 'Sample Haircut', description: 'A quick trim', price: 2500, duration: 30, image: '/public/hero.png' },
  { serviceId: 2, name: 'Sample Massage', description: 'Relaxing 60min massage', price: 7000, duration: 60, image: '/public/hero.png' }
]

const FALLBACK_STAFF = [
  { staffId: 1, title: 'Stylist', bio: 'Experienced stylist', user: { name: 'Alex', profilePicture: '/public/avatar-placeholder.png' } },
  { staffId: 2, title: 'Therapist', bio: 'Certified therapist', user: { name: 'Jamie', profilePicture: '/public/avatar-placeholder.png' } }
]

export function DataProvider({ children }) {
    const [services, setServices] = useState([])
    const [staff, setStaff] = useState([])



    const [error, setError] = useState(null)
    const [loadingServices, setLoadingServices] = useState(true)
    const [loadingStaff, setLoadingStaff] = useState(true)


    useEffect(() => {
        const loadServices = async () => {
            try {
                const response = await getServices();
                setServices(response);
            } catch (err) {
                console.error(err);
                const msg = err && err.payload && (err.payload.detail || JSON.stringify(err.payload)) || err.message || 'Failed to load services'
                setError(msg);
                // fallback sample services so UI remains usable when backend is down
                setServices(FALLBACK_SERVICES)
            } finally {
                setLoadingServices(false);
            }
        };

        loadServices();
    }, [])

    useEffect(() => {
        const loadStaff = async () => {
            try {
                const response = await getStaff();
                setStaff(response);
            } catch (err) {
                console.error(err);
                const msg = err && err.payload && (err.payload.detail || JSON.stringify(err.payload)) || err.message || 'Failed to load staff'
                setError(prev => prev ? `${prev} | ${msg}` : msg);
                // fallback sample staff
                setStaff(FALLBACK_STAFF)
            } finally {
                setLoadingStaff(false);
            }
        };

        loadStaff();
    }, [])


    const value = {
        services,
        staff,
        error,
        loadingServices,
        loadingStaff
    }

    return <DataContext.Provider value={value}>{children}</DataContext.Provider>;
}
