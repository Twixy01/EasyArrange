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
            } catch (error) {
                console.error(error);
                setError('Failed to load services');
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
            } catch (error) {
                console.error(error);
                setError('Failed to load staff');
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
