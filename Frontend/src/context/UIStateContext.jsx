/* eslint-disable react-refresh/only-export-components */
import { useServices } from '../hooks/queries/useServices'
import { useStaff } from '../hooks/queries/useStaff'
import { createContext } from 'react'

export const UIStateContext = createContext()

function getErrorMessage(err) {
    if (!err) return null
    if (typeof err === 'string') return err
    if (err?.message) return err.message
    return String(err)
}

export function UIStateProvider({ children }) {

    const {data: services, error: servicesError, isLoading: loadingServices} = useServices()
    const {data: staff, error: staffError, isLoading: loadingStaff} = useStaff()

    const errors = [
        getErrorMessage(servicesError),
        getErrorMessage(staffError)
    ].filter(Boolean)

    const error = errors.length > 0 ? errors.join(' | ') : null

    const value = {
        services,
        staff,
        error,
        loadingServices,
        loadingStaff
    }

    return <UIStateContext.Provider value={value}>{children}</UIStateContext.Provider>;
}
