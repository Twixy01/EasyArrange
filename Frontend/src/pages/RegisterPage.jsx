import { useState, useContext } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import SectionHeader from '../components/common/SectionHeader'
import Button from '../components/common/Button'
import { UIStateContext } from '../context/UIStateContext'

function RegisterPage() {
  const { showError, showSuccess } = useContext(UIStateContext)
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [phone, setPhone] = useState('') // added phone state
  const [password, setPassword] = useState('')

  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()

  // default role: CUSTOMER
  const defaultRoleId = 2
  const defaultRoleName = 'CUSTOMER'

  // Hungarian phone regex used on backend (accepts +36, 0036 or 06 and valid operator codes)
  const hungarianPhoneRegex = /^(\+36|0036|06)(1|[2-9][0-9])\d{7}$/

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!name || !email || !password) {
      const msg = 'Please fill in all fields.'
      showError(msg)

      return
    }

    if (password.length < 4) {
      const msg = 'Password must be at least 4 characters.'
      showError(msg)
      return
    }

    // if phone provided, validate it client-side to avoid backend 400s
    if (phone && !hungarianPhoneRegex.test(phone.trim())) {
      showError('Invalid phone number. Use Hungarian format like +36123456789 or 06123456789.')
      return
    }

    setIsLoading(true)

    try {
      const payload = {
        name,
        email,
        password,
        // include phone if provided; backend expects phoneNumber in DTO
        phoneNumber: phone && phone.trim() !== '' ? phone.trim() : null,
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

        let message = 'Registration failed. Please try again.'

        if (res.status === 400) {
          message = 'Invalid input. Please check your details.'
        } else if (res.status === 409) {
          message = 'Email already exists.'
        }
        showError(message)
        return
      }

      await res.json()
      showSuccess('Registration successful. You can now log in.')
      navigate('/login')
    } catch (err) {
      console.error('Registration error', err)
      const msg = 'Network error. Please try again.'
      showError(msg)
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

          {/* phone number field (optional) */}
          <div className="field">
            <label htmlFor="phone">Phone</label>
            <input
              id="phone"
              name="phoneNumber"
              type="tel"
              placeholder="+36 20 123 4567 or 06123456789"
              value={phone}
              onChange={e => setPhone(e.target.value)}
              aria-label="phone number"
            />
            <small className="hint">Optional — Hungarian numbers only (e.g. +36123456789)</small>
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
