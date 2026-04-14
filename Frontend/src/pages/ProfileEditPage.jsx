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
    profilePicture: user?.profilePicture || '',
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: ''
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

    if ((form.newPassword && form.newPassword.trim() !== '') || (form.confirmNewPassword && form.confirmNewPassword.trim() !== '')) {
      if (!form.newPassword || !form.confirmNewPassword) {
        setError('Please fill both new password fields')
        return
      }
      if (form.newPassword !== form.confirmNewPassword) {
        setError('New passwords do not match')
        return
      }
      if (form.newPassword.length < 4) {
        setError('New password must be at least 4 characters')
        return
      }
    }

    setSaving(true)

    try {
      const resolvedUserId = user.userId
      if (!resolvedUserId) {
        setError('Cannot determine user id for update')
        setSaving(false)
        return
      }

      let roleId = 2
      try {
        const roleName = user.role.name || null
        if (typeof roleName === 'string' && roleName.toUpperCase().includes('ADMIN')) roleId = 1
      } catch {
        console.debug('role mapping ignored')
      }

      const payload = {
        name: form.name,
        email: form.email,
        currentPassword: form.currentPassword,
        profilePicture: form.profilePicture,
        role: {roleId: roleId, name: user.role.name}
      }

      if (form.newPassword && form.newPassword.trim() !== '') {
        payload.newPassword = form.newPassword
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
          userMessage = p.detail
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
              <p className="muted">For security the current password is required to save changes. To change your password, enter a new password and confirm it.</p>

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

                  {/* phone intentionally commented out for now */}
                  {/* <div className="field">
                    <label>Phone</label>
                    <input name="phone" value={form.phone || ''} onChange={handleChange} />
                  </div> */}

                  <div className="field">
                    <label>Profile picture URL</label>
                    <input name="profilePicture" value={form.profilePicture} onChange={handleChange} />
                  </div>

                  <div className="field">
                    <label>Current password</label>
                    <input name="currentPassword" type="password" value={form.currentPassword} onChange={handleChange} required />
                  </div>

                  <div className="field">
                    <label>New password</label>
                    <input name="newPassword" type="password" value={form.newPassword} onChange={handleChange} />
                  </div>

                  <div className="field">
                    <label>Confirm new password</label>
                    <input name="confirmNewPassword" type="password" value={form.confirmNewPassword} onChange={handleChange} />
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
