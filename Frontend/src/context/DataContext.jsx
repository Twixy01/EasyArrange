/* eslint-disable react-refresh/only-export-components */
import { getStaff, getServices } from '../services/api'
import { createContext, useState, useEffect } from 'react'

export const DataContext = createContext()

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
