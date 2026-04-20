import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Card from '../components/common/Card'
import { useAuth } from '../hooks/useAuth'
import { updateUser, getUser } from '../services/api'

export default function ChangePasswordPage() {
  const { user, login } = useAuth()
  const navigate = useNavigate()

  const [form, setForm] = useState({ currentPassword: '', newPassword: '', confirmNewPassword: '' })
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(null)
  const [serverDebug, setServerDebug] = useState(null)

  if (!user) return (
    <section className="section">
      <div className="container">
        <Card>
          <div className="card-body">
            <h2>Please log in</h2>
            <p className="muted">You must be signed in to change your password.</p>
          </div>
        </Card>
      </div>
    </section>
  )

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(f => ({ ...f, [name]: value }))
  }

  const handleCancel = () => {
    navigate('/profile')
  }

  const handleSubmit = async () => {
    setError(null)
    setSuccess(null)
    if (!form.currentPassword) return setError('Current password is required')
    if (!form.newPassword) return setError('New password is required')
    if (form.newPassword !== form.confirmNewPassword) return setError('New passwords do not match')
    if (form.newPassword.length < 4) return setError('New password must be at least 4 characters')

    setSaving(true)
    try {
      const userId = user.userId
      if (!userId) throw new Error('Unknown user')
      const fresh = await getUser(userId)
      const roleFromServer = fresh.role || fresh.roleResponse || null
      const roleObj = roleFromServer ? { roleId: roleFromServer.roleId ?? roleFromServer.roleId, name: roleFromServer.name ?? roleFromServer } : null
      const payload = {
        name: fresh.name || user.name || '',
        email: fresh.email || user.email || '',
        phoneNumber: fresh.phoneNumber ?? fresh.phone ?? user.phoneNumber ?? user.phone ?? null,
        profilePicture: fresh.profilePicture ?? user.profilePicture ?? null,
        role: roleObj,
        currentPassword: form.currentPassword,
        newPassword: form.newPassword
      }
      console.debug('ChangePassword payload', payload)
      const updated = await updateUser(userId, payload)
      const token = (() => { try { return localStorage.getItem('token') } catch { return null } })()
      const newUser = { ...(updated || {}), token }
      try { login(newUser) } catch { /* ignore */ }
      setSuccess('Password changed successfully')
      setTimeout(() => setSuccess(null), 3000)
      navigate('/profile')
    } catch (err) {
      const payloadErr = err?.payload || err?.response?.data
      console.error('Change password failed', err, payloadErr)
      setServerDebug(payloadErr || err?.response)
      if (payloadErr) {
        if (payloadErr.fieldErrors) {
          const msgs = Object.entries(payloadErr.fieldErrors).map(([k, v]) => `${k}: ${v}`).join(' ')
          setError(msgs || payloadErr.detail || payloadErr.message || err.message)
        } else {
          setError(payloadErr.detail || payloadErr.message || err.message)
        }
      } else {
        setError(err.message || 'Failed to change password')
      }
    } finally {
      setSaving(false)
    }
  }

  return (
    <section className="section">
      <div className="container">
        <div className="page">
          <Card className="form-card">
            <div className="card-body">
              <h2 style={{ marginTop: 0 }}>Create new password</h2>
              <p className="muted">Enter your current password and choose a new one.</p>

              <div style={{ display: 'grid', gap: 12 }}>
                <div>
                  <label className="muted">Current password</label>
                  <input name="currentPassword" type="password" value={form.currentPassword} onChange={handleChange} />
                </div>
                <div>
                  <label className="muted">New password</label>
                  <input name="newPassword" type="password" value={form.newPassword} onChange={handleChange} />
                </div>
                <div>
                  <label className="muted">Confirm new password</label>
                  <input name="confirmNewPassword" type="password" value={form.confirmNewPassword} onChange={handleChange} />
                </div>

                {error && <div className="form-error">{error}</div>}
                {serverDebug && <pre style={{marginTop:8, maxHeight:200, overflow:'auto', background:'#f6f8fa', padding:8}}>{JSON.stringify(serverDebug, null, 2)}</pre>}
                {success && <div className="form-success">{success}</div>}

                <div style={{ display: 'flex', gap: 8 }}>
                  <button className="btn-primary" onClick={handleSubmit} disabled={saving}>{saving ? 'Saving...' : 'Create new password'}</button>
                  <button className="btn-secondary" onClick={handleCancel}>Cancel</button>
                </div>
              </div>

            </div>
          </Card>
        </div>
      </div>
    </section>
  )
}
