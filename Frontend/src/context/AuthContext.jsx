/* eslint-disable react-refresh/only-export-components */
import { createContext, useEffect, useState } from 'react'
import { useStaffByUser } from '../hooks/queries/useStaffByUser';

export const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('user')
      const parsed = stored ? JSON.parse(stored) : null
      if (parsed) {
        if (parsed.email) parsed.email = String(parsed.email).toLowerCase()
        if (typeof parsed.role === 'string') {
          // normalize role to UPPERCASE to match database conventions
          parsed.role = { name: String(parsed.role).toUpperCase() }
        } else if (parsed.role && parsed.role.name) {
          parsed.role.name = String(parsed.role.name).toUpperCase()
        }
      }
      return parsed
    } catch {
      return null
    }
  })

  // ensure we compare role names in UPPERCASE throughout the app
  const roleRaw = typeof user?.role === 'string' ? user.role : user?.role?.name
  const roleNameUpper = roleRaw ? String(roleRaw).toUpperCase() : null
  const shouldFetchStaff = roleNameUpper === 'STAFF' || roleNameUpper === 'ADMIN'

  const { data: staff } = useStaffByUser(
    shouldFetchStaff && user && user.userId ? user.userId : null
  )

  useEffect(() => {
    console.debug('[AuthProvider] mounted')
    function onStorage(e) {
      if (e.key === 'user' || e.key === 'token') {
        try {
          const stored = localStorage.getItem('user')
          const parsed = stored ? JSON.parse(stored) : null
          if (parsed) {
            if (parsed.email) parsed.email = String(parsed.email).toLowerCase()
            if (typeof parsed.role === 'string') {
              // normalize to UPPERCASE
              parsed.role = { name: String(parsed.role).toUpperCase() }
            } else if (parsed.role && parsed.role.name) {
              parsed.role.name = String(parsed.role.name).toUpperCase()
            }
          }
          console.debug('[AuthProvider] storage event - new user:', parsed)
          setUser(parsed)
        } catch {
          console.debug('[AuthProvider] storage event - parse error')
          setUser(null)
        }
      }
    }
    window.addEventListener('storage', onStorage)
    return () => window.removeEventListener('storage', onStorage)
  }, [])

  const login = (userData) => {
    console.debug('[AuthProvider] login called with:', userData)
    const normalized = { ...userData }
    if (normalized.email) normalized.email = String(normalized.email).toLowerCase()
    if (typeof normalized.role === 'string') {
      // store role name as UPPERCASE
      normalized.role = { name: String(normalized.role).toUpperCase() }
    } else if (normalized.role && normalized.role.name) {
      normalized.role = { ...normalized.role, name: String(normalized.role.name).toUpperCase() }
    }
    setUser(normalized)
    try { localStorage.setItem('user', JSON.stringify(normalized)) } catch { /* ignore */ }
    if (userData && userData.token) {
      try { localStorage.setItem('token', userData.token) } catch { /* ignore */ }
    }
  }

  const logout = () => {
    console.debug('[AuthProvider] logout called')
    setUser(null)
    try { localStorage.removeItem('user') } catch { /* ignore */ }
    try { localStorage.removeItem('token') } catch { /* ignore */ }
  }

  const value = {
    user,
    staff,
    isLoggedIn: Boolean(user),
    login,
    logout
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
