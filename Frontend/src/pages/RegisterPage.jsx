import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import SectionHeader from '../components/common/SectionHeader'
import Button from '../components/common/Button'

function RegisterPage() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()

  // default role: CUSTOMER
  const defaultRoleId = 2
  const defaultRoleName = 'CUSTOMER'

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
      const payload = {
        name,
        email,
        password,
        role: { roleId: defaultRoleId, name: defaultRoleName }
      }

      const res = await fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
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
    <section className="section">
      <div className="container narrow">
        <SectionHeader
          eyebrow="Create account"
          title="Join the salon experience"
          description="Register to manage bookings and your personal profile."
          center
        />
        {error && <div className="error" role="alert">{error}</div>}

        <form className="register-form" onSubmit={handleSubmit} noValidate>
          <div className="field">
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
          </div>

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
          <Button type="submit" disabled={isLoading}>
            {isLoading ? 'Creating account...' : 'Register'}
          </Button>
        </form>

        <p>
          Already have an account? <Link to="/login">Log in</Link>
        </p>
      </div>
    </section>
  )
}

export default RegisterPage
