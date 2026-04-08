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
    currentPassword: ''
  })
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState(null)

  if (!user) {
    return (
      <div className="page">
        <Card>
          <h2>Please log in to edit your profile</h2>
        </Card>
      </div>
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

    setSaving(true)

    try {
      const resolvedUserId = user.userId
      if (!resolvedUserId) {
        setError('Cannot determine user id for update')
        setSaving(false)
        return
      }

      // Map role name to a conservative default id (matching RegisterPage defaultRoleId = 2)
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

      const updated = await updateUser(resolvedUserId, payload)

      // Update auth context / localStorage with new user info (preserve token if present)
      const token = (() => { try { return localStorage.getItem('token') } catch { return null } })()
      const newUser = { ...(updated || {}), token: token }
      login(newUser)
      navigate('/profile')

    } catch (err) {

      // try to surface backend validation messages if present
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
    <div className="page profile-edit-page">
      <Card>
        <h2>Edit Profile</h2>
        <p className="muted">Note: For security the current password is required to save changes.</p>
        <form onSubmit={handleSubmit} className="form">
          <label>
            Name
            <input name="name" value={form.name} onChange={handleChange} />
          </label>

          <label>
            Email
            <input name="email" type="email" value={form.email} onChange={handleChange} />
          </label>

          <label>
            Current password:
            <input name="currentPassword" type="password" value={form.currentPassword} onChange={handleChange} required/>
          </label>

          <label>
            Profile picture URL
            <input name="profilePicture" value={form.profilePicture} onChange={handleChange} />
          </label>

          {error && <p className="muted">{error}</p>}

          <div style={{ marginTop: 12 }}>
            <button type="button" onClick={handleCancel} className="btn secondary">Cancel</button>
            <button type="submit" className="btn" style={{ marginLeft: 8 }} disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
          </div>
        </form>
      </Card>
    </div>
  )
}

export default ProfileEditPage
