import React, { useMemo, useState, useContext, useEffect } from 'react'
const ID_TO_NAME = { 1: 'ADMIN', 2: 'CUSTOMER', 3: 'STAFF' }

import Card from '../components/common/Card'
import { useUsers } from '../hooks/queries/useUsers'
import { useAuth } from '../hooks/useAuth'
import { Link } from 'react-router-dom'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { updateUser, deleteUser, adminUpdateUser, createStaff, deleteStaffByUserId, getStaffByUserId } from '../services/api'
import { UIStateContext } from '../context/UIStateContext'
import avatarPlaceholder from '../assets/avatar-placeholder.png'

function ManageUsers() {
    const { showSuccess, showError, getErrorMessage } = useContext(UIStateContext)

    const { user } = useAuth()
    const isAdmin = !!(user && user.role && String(user.role.name).toUpperCase() === 'ADMIN')

    const { data: users = [], isLoading, error } = useUsers()
    const queryClient = useQueryClient()

    const [editingId, setEditingId] = useState(null)
    const [editValues, setEditValues] = useState({})
    const [fieldErrors, setFieldErrors] = useState(null)
    const [roleFilter, setRoleFilter] = useState('ALL')

    const displayedUsers = useMemo(() => {
        if (!Array.isArray(users)) return []
        return users
            .filter(u => u.userId !== user?.userId)
            .map(u => {
                const raw = u.role
                let roleId = null
                let roleName = ''
                if (raw != null) {
                    if (typeof raw === 'number') {
                        roleId = Number(raw)
                    } else if (typeof raw === 'string') {
                        if (/^\d+$/.test(raw)) roleId = Number(raw)
                        else roleName = raw
                    } else if (typeof raw === 'object') {
                        if (raw.roleId != null) roleId = Number(raw.roleId)
                        else if (raw.id != null) roleId = Number(raw.id)
                        roleName = raw.name ?? roleName
                    }
                }
                return { ...u, normalizedRole: { roleId: roleId, roleName: (roleName || '').toString() } }
            })
    }, [users, user?.userId])

    const filteredUsers = useMemo(() => {
        if (!displayedUsers || !Array.isArray(displayedUsers)) return []
        if (!roleFilter || roleFilter === 'ALL') return displayedUsers

        const numeric = /^\d+$/.test(String(roleFilter))
        if (numeric) {
            const id = Number(roleFilter)
            const idToName = { 1: 'ADMIN', 2: 'CUSTOMER', 3: 'STAFF' }
            const wantName = idToName[id]
            return displayedUsers.filter(u => {
                const rid = Number(u.normalizedRole?.roleId)
                const rname = String(u.normalizedRole?.roleName || u.role?.name || u.role || '').toUpperCase()
                return rid === id || (wantName && rname === wantName)
            })
        }

        const wanted = String(roleFilter).toUpperCase()
        return displayedUsers.filter(u => String((u.normalizedRole?.roleName || u.role?.name || u.role || '')).toUpperCase() === wanted)
    }, [displayedUsers, roleFilter])

    const availableRoles = useMemo(() => {
        const map = new Map()
        displayedUsers.forEach(u => {
            const rId = u.normalizedRole?.roleId
            const rName = u.normalizedRole?.roleName || (u.role?.name ?? (typeof u.role === 'string' ? u.role : ''))
            const key = rId != null ? String(rId) : (rName || '')
            if (!key) return
            if (!map.has(key)) map.set(key, { roleId: rId ?? null, name: rName })
        })
        if (map.size === 0) {
            map.set('1', { roleId: 1, name: ID_TO_NAME[1] })
            map.set('2', { roleId: 2, name: ID_TO_NAME[2] })
            map.set('3', { roleId: 3, name: ID_TO_NAME[3] })
        }
        return Array.from(map.values())
    }, [displayedUsers]);

    const roleOptions = useMemo(() => {
        const seen = new Set()
        const opts = []
        const canonIds = [1, 2, 3]
        canonIds.forEach(id => {
            const name = ID_TO_NAME[id]
            const key = String(id)
            if (!seen.has(key)) {
                seen.add(key)
                opts.push({ value: key, label: name })
            }
        })

        const rolesArr = availableRoles || []
        rolesArr.forEach(r => {
            const key = r.roleId != null ? String(r.roleId) : r.name
            if (!key) {
                return
            }
            if (!seen.has(key)) {
                seen.add(key)
                opts.push({ value: r.roleId != null ? String(r.roleId) : r.name, label: r.name })
            }
        })

        return opts
    }, [availableRoles]);


    const updateMutation = useMutation({
        mutationFn: ({ payload }) => updateUser(payload),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['users'] })
            showSuccess("User updated successfully.")
            setEditingId(null)
            setEditValues({})
            setFieldErrors(null)
        },
        onError: (err) => {
            const payload = err?.payload || err?.response?.data
            if (payload) {
                showError(getErrorMessage(err, "Failed to update user. Please check your input and try again."))
                setFieldErrors(payload.fieldErrors || null)
            } else {
                showError(getErrorMessage(err, "Failed to update user."))
            }
        }
    })

    const adminUpdateMutation = useMutation({
        mutationFn: ({ userId, payload }) => adminUpdateUser(userId, payload),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['users'] })
            showSuccess("User updated successfully.")
            setEditingId(null)
            setEditValues({})
            setFieldErrors(null)
        },
        onError: (err) => {
            console.error('Admin update user error', err)
            const payload = err?.payload || err?.response?.data
            if (payload) {
                showError(getErrorMessage(err, "Failed to update user. Please check your input and try again."))
                setFieldErrors(payload.fieldErrors || null)
            } else {
                showError(getErrorMessage(err, "Failed to update user."))
            }
        }
    })

    const deleteMutation = useMutation({
        mutationFn: (userId) => deleteUser(userId),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['users'] })
            showSuccess("User deleted successfully.")
        },
        onError: (err) => {
            showError(getErrorMessage(err, "Failed to delete user."))
        }
    })

    useEffect(() => {
        if (error) {
            const message = getErrorMessage(error, "Failed to load users. Please try again later.")
            showError(message)
        }
    }, [error, getErrorMessage, showError])

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

    const startEdit = async (u) => {
        const roleObj = {
            roleId: u.normalizedRole?.roleId ?? null,
            name: u.normalizedRole?.roleName || u.role?.name || (typeof u.role === 'string' ? u.role : '')
        }
        setEditingId(u.userId)
        setEditValues({ name: u.name || '', email: u.email || '', phoneNumber: u.phoneNumber ?? u.phone ?? '', role: roleObj, currentPassword: '', newPassword: '', staffTitle: '', staffBio: '' })
        setFieldErrors(null)

        if (String(roleObj?.name || ID_TO_NAME[Number(roleObj?.roleId)] || '').toUpperCase() === 'STAFF') {
            try {
                const staff = await getStaffByUserId(u.userId)
                setEditValues((v) => ({
                    ...v,
                    staffTitle: staff?.title || '',
                    staffBio: staff?.bio || ''
                }))
            } catch {
                // If no staff record is found, keep fields empty and let save flow handle creation.
            }
        }
    }

    const cancelEdit = () => {
        setEditingId(null)
        setEditValues({})
        setFieldErrors(null)
    }

    const resolveRoleName = (u) => String(u?.normalizedRole?.roleName || u?.role?.name || u?.role || '').toUpperCase()

    const isSelectedRoleStaff = String(editValues?.role?.name || ID_TO_NAME[Number(editValues?.role?.roleId)] || '').toUpperCase() === 'STAFF'

    const saveEdit = async (userId, originalUser) => {
        const name = (editValues.name || '').trim()
        if (!name) {
            setFieldErrors({ ...(fieldErrors || {}), name: 'Name must not be blank' })
            return
        }

        let roleObj = null
        if (editValues.role && (editValues.role.roleId || editValues.role.roleId === 0)) {
            roleObj = { roleId: Number(editValues.role.roleId), name: editValues.role.name }
        } else if (editValues.role && editValues.role.name) {
            const match = availableRoles.find(r => String(r.name).toUpperCase() === String(editValues.role.name).toUpperCase())
            roleObj = match && match.roleId ? { roleId: match.roleId, name: match.name } : { roleId: null, name: editValues.role.name }
        }

        if (!roleObj) {
            setFieldErrors({ ...(fieldErrors || {}), role: 'Role must be specified' })
            return
        }

        const originalRoleName = resolveRoleName(originalUser)
        const selectedRoleName = String(roleObj?.name || ID_TO_NAME[Number(roleObj?.roleId)] || '').toUpperCase()
        const becameStaff = selectedRoleName === 'STAFF' && originalRoleName !== 'STAFF'
        const leftStaff = originalRoleName === 'STAFF' && selectedRoleName !== 'STAFF'

        const staffTitle = (editValues.staffTitle || '').trim()
        const staffBio = (editValues.staffBio || '').trim()
        if (becameStaff && (!staffTitle || !staffBio)) {
            setFieldErrors({
                ...(fieldErrors || {}),
                ...(staffTitle ? {} : { staffTitle: 'Title is required when role is STAFF' }),
                ...(staffBio ? {} : { staffBio: 'Bio is required when role is STAFF' })
            })
            return
        }

        const payload = {
            name,
            email: originalUser.email,
            phoneNumber: originalUser.phoneNumber,
            currentPassword: (originalUser.userId === user.userId) ? editValues.currentPassword : undefined,
            newPassword: null,
            profilePicture: originalUser.profilePicture || avatarPlaceholder,
            role: roleObj
        }

        setFieldErrors(null)

        try {
            if (isAdmin && originalUser.userId !== user.userId) {
                await adminUpdateMutation.mutateAsync({ userId, payload })
            } else {
                await updateMutation.mutateAsync({ userId, payload })
            }

            if (becameStaff) {
                await createStaff({ userId, title: staffTitle, bio: staffBio })
                await queryClient.invalidateQueries({ queryKey: ['staff'] })
            }

            if (leftStaff) {
                await deleteStaffByUserId(userId)
                await queryClient.invalidateQueries({ queryKey: ['staff'] })
            }
        } catch (err) {
            const payloadErr = err?.payload || err?.response?.data
            if (payloadErr) {
                showError(getErrorMessage(err, "Failed to update user. Please check your input and try again."))
                setFieldErrors(payloadErr.fieldErrors || null)
            } else {
                showError(getErrorMessage(err, "Failed to update user. Please check your input and try again."))
            }
        }
    }

    const handleDelete = async (userId) => {
        if (!window.confirm('Are you sure you want to delete this user?')) return
        if (users.find(u => u.userId === userId).role?.name === 'STAFF') {
            showError("Cannot delete a user with STAFF role. Change the role before deleting.")
            return
        }
        try {
            await deleteMutation.mutateAsync(userId)
            showSuccess("User deleted successfully.")
        } catch (err) {
            showError(getErrorMessage(err, "Failed to delete user. Please try again."))
        }
    }

    return (
        <section className="section manage-users-page">
            <div className="container">
                <Card>
                    <div className="card-body">
                        <h1 className="title">Manage Users</h1>

                        {error && (
                            <div className="notification is-danger">
                                <button className="delete" onClick={() => showError('')}></button>
                                {getErrorMessage(error, "Failed to load users. Please try again later.")}
                            </div>
                        )}

                        {isLoading && <p className="muted">Loading users...</p>}

                        {!isLoading && displayedUsers && displayedUsers.length > 0 && (
                            <>
                                <div style={{ marginBottom: 16 }}>
                                    <label className="muted">Filter by role</label>
                                    <select value={roleFilter} onChange={(e) => setRoleFilter(e.target.value)} style={{ marginLeft: 8 }}>
                                        <option value="ALL">All roles</option>
                                        {roleOptions.map(o => (
                                            <option key={o.value} value={o.value}>{o.label}</option>
                                        ))}
                                    </select>
                                </div>

                                {filteredUsers.length > 0 ? (
                                    <div className="user-list-form">
                                        {filteredUsers.map(u => {
                                            const isEditing = editingId === u.userId
                                            return (
                                                <fieldset key={u.userId} style={{ marginBottom: 12, padding: 8, border: '1px solid #eee' }}>
                                                    <legend style={{ fontWeight: 600 }}>{u.name || 'Unnamed'}</legend>
                                                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8, alignItems: 'center' }}>
                                                        <div>
                                                            <label className="muted">Name</label>
                                                            <input type="text" value={isEditing ? editValues.name : (u.name || '')} onChange={(e) => setEditValues(v => ({ ...v, name: e.target.value }))} readOnly={!isEditing} />
                                                            {isEditing && fieldErrors?.name && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.name}</div>}
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
                                                            {isEditing && fieldErrors?.role && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.role}</div>}
                                                        </div>

                                                        <div>
                                                            <label className="muted">ID</label>
                                                            <input type="text" value={u.userId || ''} readOnly />
                                                        </div>

                                                        <div>
                                                            <label className="muted">Phone</label>
                                                            <input type="text" value={isEditing ? (editValues.phoneNumber || '') : (u.phoneNumber ?? '')} readOnly />
                                                        </div>

                                                        {isEditing && isSelectedRoleStaff && (
                                                            <>
                                                                <div>
                                                                    <label className="muted">Staff title</label>
                                                                    <input type="text" value={editValues.staffTitle || ''} onChange={(e) => setEditValues(v => ({ ...v, staffTitle: e.target.value }))} placeholder="e.g. Senior Stylist" />
                                                                    {fieldErrors?.staffTitle && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.staffTitle}</div>}
                                                                </div>
                                                                <div>
                                                                    <label className="muted">Staff bio</label>
                                                                    <input type="text" value={editValues.staffBio || ''} onChange={(e) => setEditValues(v => ({ ...v, staffBio: e.target.value }))} placeholder="Short professional bio" />
                                                                    {fieldErrors?.staffBio && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.staffBio}</div>}
                                                                </div>
                                                            </>
                                                        )}

                                                        {/* show currentPassword only when editing your own account */}
                                                        {isEditing && u.userId === user.userId && (
                                                            <>
                                                                <div>
                                                                    <label className="muted">Current password</label>
                                                                    <input type="password" value={editValues.currentPassword || ''} onChange={(e) => setEditValues(v => ({ ...v, currentPassword: e.target.value }))} />
                                                                    {isEditing && fieldErrors?.currentPassword && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.currentPassword}</div>}
                                                                </div>
                                                                <div>
                                                                    <label className="muted">New password (optional)</label>
                                                                    <input type="password" value={editValues.newPassword || ''} onChange={(e) => setEditValues(v => ({ ...v, newPassword: e.target.value }))} />
                                                                    {isEditing && fieldErrors?.newPassword && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.newPassword}</div>}
                                                                </div>
                                                            </>
                                                        )}

                                                        <div style={{ gridColumn: '1 / -1', display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                                                            {isEditing ? (
                                                                <>
                                                                    <button className="btn btn-primary" onClick={() => saveEdit(u.userId, u)} disabled={updateMutation.isLoading || (u.userId === user.userId && !(editValues.currentPassword && editValues.currentPassword.trim()))}>Save</button>
                                                                    <button className="btn btn-secondary" onClick={cancelEdit}>Cancel</button>
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
                                ) : (
                                    <p className="muted">No users match the selected role.</p>
                                )}
                            </>
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
