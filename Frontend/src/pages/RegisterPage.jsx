import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'


function RegisterPage() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()

  const defaultRoleId = 2

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)

    if (!name || !email || !password) {
      setError('Please fill in all fields.')
      return
    }

    if (password.length < 4) {
      setError('Password must be at least 4 characters.')
      return
    }

    setIsLoading(true)

    try {
      const res = await fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password, roleId: defaultRoleId })
      })

      if (!res.ok) {
        const text = await res.text()
        console.error('Register failed', { status: res.status, text })
        if (res.status === 400) {
          setError(text || 'Invalid input. Please check your details.')
        } else if (res.status === 409) {
          setError(text || 'Email already exists.')
        } else {
          setError(text || 'Registration failed. Please try again.')
        }
        return
      }

      await res.json()
      navigate('/login')
    } catch (err) {
      console.error('Registration error', err)
      setError('Network error. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="register-page">
      <h1>Create an account</h1>
      {error && <div className="error" role="alert">{error}</div>}

      <form className="register-form" onSubmit={handleSubmit} noValidate>
        <label htmlFor="name">Username</label>
        <input
          id="name"
          name="name"
          type="text"
          placeholder="Your name"
          value={name}
          onChange={e => setName(e.target.value)}
          required
        />

        <label htmlFor="email">Email</label>
        <input
          id="email"
          name="email"
          type="email"
          placeholder="you@company.com"
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />

        <label htmlFor="password">Password</label>
        <input
          id="password"
          name="password"
          type="password"
          placeholder="At least 4 characters"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
          minLength={4}
        />

        <button type="submit" disabled={isLoading}>
          {isLoading ? 'Creating account...' : 'Register'}
        </button>
      </form>

      <p>
        Already have an account? <Link to="/login">Log in</Link>
      </p>
    </div>
  )
}

export default RegisterPage
