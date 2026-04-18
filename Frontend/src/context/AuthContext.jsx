import { createContext, useEffect, useState } from 'react'
import { useStaffByUser } from '../hooks/queries/useStaffByUser';

export const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('user')
      return stored ? JSON.parse(stored) : null
    } catch (e) {
      return null
    }
  })
  

  const shouldFetchStaff = user?.role === 'STAFF' || user?.role === 'ADMIN';

  const { data: staff } = useStaffByUser(
    shouldFetchStaff ? user.userId : null
  );

  useEffect(() => {
    console.debug('[AuthProvider] mounted - initial user:', user)
    function onStorage(e) {
      if (e.key === 'user' || e.key === 'token') {
        try {
          const stored = localStorage.getItem('user')
          const parsed = stored ? JSON.parse(stored) : null
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
    setUser(userData)
    try { localStorage.setItem('user', JSON.stringify(userData)) } catch (e) { /* ignore */ }
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
