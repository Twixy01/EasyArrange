import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import Card from '../components/common/Card'
import { updateUser } from '../services/api'

function ProfileEditPage() {
  const { user, login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({
    name: user?.name || '',
    email: user?.email || '',
    phoneNumber: user?.phoneNumber || user?.phone || '',
    profilePicture: user?.profilePicture || '',
    currentPassword: ''
  })
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState(null)

  if (!user) {
    return (
      <section className="section">
        <div className="container">
          <Card>
            <div className="card-body">
              <h2>Please log in to edit your profile</h2>
            </div>
          </Card>
        </div>
      </section>
    )
  }

  const hungarianPhoneRegex = /^(\+36|0036|06)(1|[2-9][0-9])\d{7}$/

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
  }

  const handleCancel = () => {
    navigate('/profile')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)

    if (!form.name || !form.email || !form.currentPassword) {
      setError('Name, email and current password are required')
      return
    }

    // validate phone client-side if provided
    const phoneTrim = form.phoneNumber ? String(form.phoneNumber).trim() : ''
    if (phoneTrim !== '' && !hungarianPhoneRegex.test(phoneTrim)) {
      setError('Invalid phone number. Use Hungarian format like +36123456789 or 06123456789.')
      return
    }

    setSaving(true)

    try {
      const resolvedUserId = user.userId
      if (!resolvedUserId) {
        setError('Cannot determine user id for update')
        setSaving(false)
        return
      }

      // Normalize email to avoid case-only collisions (backend treats email checks strictly)
      const normalizedEmail = String(form.email || '').trim().toLowerCase()

      // If normalized email equals the currently stored user's email (case-insensitive),
      // send the original user.email value so backend equality check succeeds and doesn't
      // falsely think the email changed and collide with another record.
      const emailToSend = (user?.email && String(user.email).trim().toLowerCase() === normalizedEmail)
        ? user.email
        : normalizedEmail

      // Send phoneNumber as null when empty to prevent backend regex validation errors
      const phonePayload = phoneTrim === '' ? null : phoneTrim

      // Determine role id and ensure role name is uppercase
      let roleId = 2
      let roleName = 'CUSTOMER'
      try {
        roleName = (user?.role?.name || 'CUSTOMER').toString().toUpperCase()
        if (roleName.includes('ADMIN')) roleId = 1
        else if (roleName.includes('STAFF')) roleId = 3
      } catch {
        // fallback to default
      }

      const payload = {
        name: form.name,
        email: emailToSend,
        phoneNumber: phonePayload,
        currentPassword: form.currentPassword,
        profilePicture: form.profilePicture || null,
        role: { roleId: roleId, name: roleName }
      }

      const updated = await updateUser(resolvedUserId, payload)

      const token = (() => { try { return localStorage.getItem('token') } catch { return null } })()
      const newUser = { ...(updated || {}), token: token }
      login(newUser)
      navigate('/profile')

    } catch (err) {

      let userMessage = err.message || String(err)
      if (err && err.payload) {
        const p = err.payload
        if (p.fieldErrors) {
          const msgs = Object.values(p.fieldErrors).filter(Boolean).join(' ')
          if (msgs) userMessage = msgs
        } else if (p.detail) {
          // backend often uses detail for validation messages like "Email already exists!"
          userMessage = p.detail
        } else if (p.message) {
          userMessage = p.message
        }
      }
      setError(userMessage)
    } finally {
      setSaving(false)
    }
  }

  return (
    <section className="section">
      <div className="container">
        <div className="page profile-edit-page">
          <Card className="form-card">
            <div className="card-body">
              <h2 style={{ marginTop: 0 }}>Edit Profile</h2>
              <p className="muted">For security the current password is required to save changes. To change your password, go to the password change section.</p>

              <form onSubmit={handleSubmit} className="form">
                <div className="form-grid">
                  <div className="field">
                    <label>Name</label>
                    <input name="name" value={form.name} onChange={handleChange} />
                  </div>

                  <div className="field">
                    <label>Email</label>
                    <input name="email" type="email" value={form.email} onChange={handleChange} />
                  </div>

                  <div className="field">
                    <label>Phone</label>
                    <input name="phoneNumber" value={form.phoneNumber || ''} onChange={handleChange} />
                  </div>

                  <div className="field">
                    <label>Profile picture URL</label>
                    <input name="profilePicture" value={form.profilePicture} onChange={handleChange} />
                  </div>

                  <div className="field">
                    <label>Current password</label>
                    <input name="currentPassword" type="password" value={form.currentPassword} onChange={handleChange} required />
                  </div>
                </div>

                {error && <div className="form-error" style={{ marginTop: 12 }}>{error}</div>}

                <div style={{ marginTop: 12, display: 'flex', gap: 8 }}>
                  <button type="button" onClick={handleCancel} className="btn btn-secondary">Cancel</button>
                  <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving...' : 'Save changes'}</button>
                </div>
              </form>
            </div>
          </Card>
        </div>
      </div>
    </section>
  )
}

export default ProfileEditPage
