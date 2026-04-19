import React, { useMemo, useState } from 'react'
import Card from '../components/common/Card'
import { useUsers } from '../hooks/queries/useUsers'
import { useAuth } from '../hooks/useAuth'
import { Link } from 'react-router-dom'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { updateUser, deleteUser, adminUpdateUser } from '../services/api'

function ManageUsers() {
    const { user } = useAuth()
    const isAdmin = !!(user && user.role && String(user.role.name).toLowerCase() === 'admin')

    const { data: users = [], isLoading, error } = useUsers()
    const queryClient = useQueryClient()

    const [editingId, setEditingId] = useState(null)
    const [editValues, setEditValues] = useState({})
    const [fieldErrors, setFieldErrors] = useState(null)
    const [serverError, setServerError] = useState(null)

    //remove the logged-in user from the displayed list so they don't appear in the admin form
    const displayedUsers = Array.isArray(users) ? users.filter(u => u.userId !== user?.userId) : []

    //derive available roles from displayedUsers (fallback when no dedicated roles API exists)
    const availableRoles = useMemo(() => {
        const map = new Map()
        const list = displayedUsers
        list.forEach(u => {
            const r = u.role
            if (!r) return
            const key = r.roleId ?? r.name
            if (!map.has(key)) {
                map.set(key, { roleId: r.roleId ?? null, name: r.name ?? String(r) })
            }
        })
        // ensure at least common roles if none found
        if (map.size === 0) {
            map.set('customer', { roleId: null, name: 'customer' })
            map.set('staff', { roleId: null, name: 'staff' })
            map.set('admin', { roleId: null, name: 'admin' })
        }
        return Array.from(map.values())
    }, [displayedUsers])

    const updateMutation = useMutation({
        mutationFn: ({ userId, payload }) => updateUser(userId, payload),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['users'] })
            setEditingId(null)
            setEditValues({})
            setFieldErrors(null)
            setServerError(null)
        },
        onError: (err) => {
            console.error('Update user error', err)
            const payload = err?.payload || err?.response?.data
            if (payload) {
                setServerError(payload.detail || payload.message || null)
                setFieldErrors(payload.fieldErrors || null)
            } else {
                setServerError(err?.message || 'Update failed')
            }
        }
    })

    const adminUpdateMutation = useMutation({
        mutationFn: ({ userId, payload }) => adminUpdateUser(userId, payload),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['users'] })
            setEditingId(null)
            setEditValues({})
            setFieldErrors(null)
            setServerError(null)
        },
        onError: (err) => {
            console.error('Admin update user error', err)
            const payload = err?.payload || err?.response?.data
            if (payload) {
                setServerError(payload.detail || payload.message || null)
                setFieldErrors(payload.fieldErrors || null)
            } else {
                setServerError(err?.message || 'Update failed')
            }
        }
    })

    const deleteMutation = useMutation({
        mutationFn: (userId) => deleteUser(userId),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['users'] })
        },
        onError: (err) => {
            console.error('Delete user error', err)
        }
    })

    if (!user) {
        return (
            <section className="section">
                <div className="container">
                    <Card>
                        <div className="card-body">
                            <h2>Not signed in</h2>
                            <p className="muted">Please <Link to="/login">log in</Link> to access this page.</p>
                        </div>
                    </Card>
                </div>
            </section>
        )
    }

    if (!isAdmin) {
        return (
            <section className="section">
                <div className="container">
                    <Card>
                        <div className="card-body">
                            <h2>Access denied</h2>
                            <p className="muted">You do not have permission to view this page.</p>
                        </div>
                    </Card>
                </div>
            </section>
        )
    }

    const startEdit = (u) => {
        // admins may edit anyone
        // prefill role as object {roleId,name} if available
        const roleObj = (u.role && (u.role.roleId || u.role.roleId === 0)) ? { roleId: u.role.roleId, name: u.role.name } : (u.role?.name ? { name: u.role.name } : {})
        setEditingId(u.userId)
        setEditValues({ name: u.name || '', email: u.email || '', role: roleObj, currentPassword: '', newPassword: '' })
        setFieldErrors(null)
        setServerError(null)
    }

    const cancelEdit = () => {
        setEditingId(null)
        setEditValues({})
        setFieldErrors(null)
        setServerError(null)
    }

    const saveEdit = async (userId, originalUser) => {
        // client-side validation
        const name = (editValues.name || '').trim()
        if (!name) {
            setFieldErrors({ ...(fieldErrors || {}), name: 'Name must not be blank' })
            return
        }

        // normalize role object: prefer explicit roleId, else fallback to role name
        let roleObj = null
        if (editValues.role && (editValues.role.roleId || editValues.role.roleId === 0)) {
            roleObj = { roleId: Number(editValues.role.roleId), name: editValues.role.name }
        } else if (editValues.role && editValues.role.name) {
            // no roleId available; try to find matching role from availableRoles
            const match = availableRoles.find(r => String(r.name).toLowerCase() === String(editValues.role.name).toLowerCase())
            roleObj = match && match.roleId ? { roleId: match.roleId, name: match.name } : { roleId: null, name: editValues.role.name }
        }

        if (!roleObj) {
            setFieldErrors({ ...(fieldErrors || {}), role: 'Role must be specified' })
            return
        }

        // Build payload according to backend expectation. Backend requires `email` in the request; we keep email unchanged
        const payload = {
            name,
            email: originalUser.email,
            // include currentPassword only if editing yourself (backend enforces it)
            currentPassword: (originalUser.userId === user.userId) ? editValues.currentPassword : undefined,
            // admin editing others typically won't supply currentPassword; backend may reject if it enforces it
            newPassword: null,
            profilePicture: null,
            role: roleObj
        }

        setFieldErrors(null)
        setServerError(null)

        try {
            // if current user is admin and editing someone else, call admin endpoint
            if (isAdmin && originalUser.userId !== user.userId) {
                await adminUpdateMutation.mutateAsync({ userId, payload })
            } else {
                await updateMutation.mutateAsync({ userId, payload })
            }
        } catch (err) {
            console.error('Failed to update user', err)
            const payloadErr = err?.payload || err?.response?.data
            if (payloadErr) {
                setServerError(payloadErr.detail || payloadErr.message || null)
                setFieldErrors(payloadErr.fieldErrors || null)
            } else {
                setServerError(err?.message || 'Failed to update user')
            }
        }
    }

    const handleDelete = async (userId) => {
        if (!window.confirm('Are you sure you want to delete this user?')) return
        try {
            await deleteMutation.mutateAsync(userId)
        } catch (err) {
            console.error('Failed to delete user', err)
            const detail = err?.payload?.detail || err?.message || 'Failed to delete user'
            alert(detail)
        }
    }

    return (
        <section className="section">
            <div className="container">
                <Card>
                    <div className="card-body">
                        <h2>Manage Users</h2>
                        <p className="muted">Below is a simple listing of users. You can edit names and roles here.</p>

                        {serverError && <div className="form-error" style={{ marginBottom: 8 }}>{serverError}</div>}

                        {isLoading && <p className="muted">Loading users...</p>}
                        {error && <div className="form-error">{error?.message || 'Failed to load users'}</div>}

                        {!isLoading && displayedUsers && displayedUsers.length > 0 && (
                            <div className="user-list-form">
                                {displayedUsers.map(u => {
                                    const isEditing = editingId === u.userId
                                    return (
                                        <fieldset key={u.userId} style={{ marginBottom: 12, padding: 8, border: '1px solid #eee' }}>
                                            <legend style={{ fontWeight: 600 }}>{u.name || 'Unnamed'}</legend>
                                            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8, alignItems: 'center' }}>
                                                <div>
                                                    <label className="muted">Name</label>
                                                    <input type="text" value={isEditing ? editValues.name : (u.name || '')} onChange={(e) => setEditValues(v => ({ ...v, name: e.target.value }))} readOnly={!isEditing} />
                                                    {fieldErrors?.name && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.name}</div>}
                                                </div>
                                                <div>
                                                    <label className="muted">Email</label>
                                                    <input type="text" value={u.email || ''} readOnly />
                                                </div>

                                                <div>
                                                    <label className="muted">Role</label>
                                                    {isEditing ? (
                                                        <select value={editValues.role?.roleId ?? editValues.role?.name ?? ''} onChange={(e) => {
                                                            const val = e.target.value
                                                            const roleById = availableRoles.find(r => String(r.roleId) === String(val))
                                                            if (roleById) setEditValues(v => ({ ...v, role: roleById }))
                                                            else setEditValues(v => ({ ...v, role: { name: val } }))
                                                        }}>
                                                            <option value="">Select role</option>
                                                            {availableRoles.map(r => (
                                                                <option key={r.roleId ?? r.name} value={r.roleId ?? r.name}>{r.name}</option>
                                                            ))}
                                                        </select>
                                                    ) : (
                                                        <input type="text" value={u.role?.name || u.role || ''} readOnly />
                                                    )}
                                                    {fieldErrors?.role && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.role}</div>}
                                                </div>

                                                <div>
                                                    <label className="muted">ID</label>
                                                    <input type="text" value={u.userId || ''} readOnly />
                                                </div>

                                                {/* Phone intentionally commented out until needed */}
                                                {/* <div>
                                                    <label className="muted">Phone</label>
                                                    <input type="text" value={u.phone || ''} readOnly />
                                                </div> */}

                                                {/* show currentPassword only when editing your own account */}
                                                {isEditing && u.userId === user.userId && (
                                                    <>
                                                        <div>
                                                            <label className="muted">Current password</label>
                                                            <input type="password" value={editValues.currentPassword || ''} onChange={(e) => setEditValues(v => ({ ...v, currentPassword: e.target.value }))} />
                                                            {fieldErrors?.currentPassword && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.currentPassword}</div>}
                                                        </div>
                                                        <div>
                                                            <label className="muted">New password (optional)</label>
                                                            <input type="password" value={editValues.newPassword || ''} onChange={(e) => setEditValues(v => ({ ...v, newPassword: e.target.value }))} />
                                                            {fieldErrors?.newPassword && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.newPassword}</div>}
                                                        </div>
                                                    </>
                                                )}

                                                <div style={{ gridColumn: '1 / -1', display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                                                    {isEditing ? (
                                                        <>
                                                            <button className="btn-primary" onClick={() => saveEdit(u.userId, u)} disabled={updateMutation.isLoading || (u.userId === user.userId && !(editValues.currentPassword && editValues.currentPassword.trim()))}>Save</button>
                                                            <button className="btn-secondary" onClick={cancelEdit}>Cancel</button>
                                                        </>
                                                    ) : (
                                                        <>
                                                            <button className="btn" onClick={() => startEdit(u)}>Edit</button>
                                                            <button className="btn-danger" onClick={() => handleDelete(u.userId)} disabled={deleteMutation.isLoading}>Delete</button>
                                                        </>
                                                    )}
                                                </div>

                                            </div>
                                        </fieldset>
                                    )
                                })}
                            </div>
                        )}

                        {!isLoading && (!displayedUsers || displayedUsers.length === 0) && (
                            <p className="muted">No users found.</p>
                        )}
                    </div>
                </Card>
            </div>
        </section>
    )
}

export default ManageUsers
