import { useState, useContext } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import SectionHeader from '../components/common/SectionHeader'
import Button from '../components/common/Button'
import { UIStateContext } from '../context/UIStateContext'

function LoginPage() {
  const { showError } = useContext(UIStateContext)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()
  const { login } = useAuth()

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!email || !password) {
      showError('Please fill in both fields.')
      return
    }

    if (password.length < 4) {
      showError('Password must be at least 4 characters.')
      return
    }

    setIsLoading(true)

    try {
      const res = await fetch('http://localhost:8080/api/users/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      })

      if (!res.ok) {
        let bodyText = await res.text()
        try {
          const bodyJson = JSON.parse(bodyText)
          if (bodyJson.fieldErrors) {
            if (bodyJson.fieldErrors.password) {
              showError(bodyJson.fieldErrors.password)
              return
            }
            const msgs = Object.values(bodyJson.fieldErrors).join(' ')
            if (msgs) {
              showError(msgs)
              return
            }
          }
          if (bodyJson.detail) {
            showError(bodyJson.detail)
            return
          }
          if (bodyJson.title) {
            showError(bodyJson.title)
            return
          }
        } catch (parseErr) {
          console.error('Error body not JSON', parseErr)
        }

        console.error('Login failed', { status: res.status, text: bodyText })
        if (res.status === 401) {
          showError('Invalid credentials.')
        } else {
          showError('Login failed. Please try again.')
        }
        return
      }

      const data = await res.json()
      login(data)

      console.log('Logged in', data)
      navigate('/')
    } catch (err) {
      console.error('Login error', err)
      showError('Network error. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <section className="section">
      <div className="container narrow">
        <SectionHeader
          eyebrow="Welcome back"
          title="Login to continue"
          description="Bookings can only be submitted by authenticated users."
          center
          />
        <form className="form-card" onSubmit={handleSubmit} noValidate>
          <div className="field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="example@gmail.com"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="field">
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
          </div>

          <Button type="submit" className="full-width" disabled={isLoading}>
            {isLoading ? 'Logging in...' : 'Login'}
          </Button>
        </form>

        <p className="form-footer">
          Don't have an account? <Link to="/register">Register</Link>
        </p>
      </div>
    </section>
  )
}

export default LoginPage
