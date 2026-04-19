import { createContext, useEffect, useState } from 'react'
import { useStaffByUser } from '../hooks/queries/useStaffByUser';

export const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('user')
      const parsed = stored ? JSON.parse(stored) : null
      if (parsed) {
        //normalize email to lowercase
        if (parsed.email) parsed.email = String(parsed.email).toLowerCase()
        //normalize role: support either string or object
        if (typeof parsed.role === 'string') {
          parsed.role = { name: String(parsed.role).toLowerCase() }
        } else if (parsed.role && parsed.role.name) {
          parsed.role.name = String(parsed.role.name).toLowerCase()
        }
      }
      return parsed
    } catch (e) {
      return null
    }
  })

  // derive roleName from either string role or object role.name
  const roleRaw = typeof user?.role === 'string' ? user.role : user?.role?.name
  const roleNameLower = roleRaw ? String(roleRaw).toLowerCase() : null
  // Only fetch staff record when the user's role is 'staff'. Admins are not staff and don't need this call.
  const shouldFetchStaff = roleNameLower === 'staff'

  const { data: staff } = useStaffByUser(
    shouldFetchStaff && user && user.userId ? user.userId : null
  )

  useEffect(() => {
    console.debug('[AuthProvider] mounted - initial user:', user)
    function onStorage(e) {
      if (e.key === 'user' || e.key === 'token') {
        try {
          const stored = localStorage.getItem('user')
          const parsed = stored ? JSON.parse(stored) : null
          if (parsed) {
            if (parsed.email) parsed.email = String(parsed.email).toLowerCase()
            if (typeof parsed.role === 'string') {
              parsed.role = { name: String(parsed.role).toLowerCase() }
            } else if (parsed.role && parsed.role.name) {
              parsed.role.name = String(parsed.role.name).toLowerCase()
            }
          }
          console.debug('[AuthProvider] storage event - new user:', parsed)
          setUser(parsed)
        } catch (err) {
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
      normalized.role = { name: String(normalized.role).toLowerCase() }
    } else if (normalized.role && normalized.role.name) {
      normalized.role = { ...normalized.role, name: String(normalized.role.name).toLowerCase() }
    }
    setUser(normalized)
    try { localStorage.setItem('user', JSON.stringify(normalized)) } catch (e) { /* ignore */ }
    if (userData && userData.token) {
      try { localStorage.setItem('token', userData.token) } catch (e) { /* ignore */ }
    }
  }

  const logout = () => {
    console.debug('[AuthProvider] logout called')
    setUser(null)
    try { localStorage.removeItem('user') } catch (e) { /* ignore */ }
    try { localStorage.removeItem('token') } catch (e) { /* ignore */ }
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
