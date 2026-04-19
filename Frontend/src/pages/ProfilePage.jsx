import { useState, useMemo } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useAuth } from '../hooks/useAuth'
import Card from '../components/common/Card'
import Button from '../components/common/Button'
import { cancelBooking } from '../services/api'
import { useBookingsByCustomer } from '../hooks/queries/useBookingsByCustomer'
import avatarPlaceholder from '../assets/avatar-placeholder.png'

function ProfilePage() {
    const { user, logout } = useAuth()
    const navigate = useNavigate()
    const queryClient = useQueryClient()
    const [bookingSuccess, setBookingSuccess] = useState(null)
    const customerId = user?.userId

    // stable snapshot of "now" for this render to satisfy purity rule
    const now = useMemo(() => Date.now(), [])

    const {
        data: bookings = [],
        isLoading: loadingBookings,
        error: bookingsQueryError,
    } = useBookingsByCustomer(customerId)

    const cancelBookingMutation = useMutation({
        mutationFn: async (bookingId) => cancelBooking(bookingId),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['bookings', customerId] })
            setBookingSuccess('Booking cancelled')
            setTimeout(() => setBookingSuccess(null), 3000)
        }
    })

    const bookingsError = bookingsQueryError
        ? (bookingsQueryError?.payload?.detail || bookingsQueryError?.payload?.message || bookingsQueryError?.message || 'Failed to load bookings')
        : null

    if (!user) {
        return (
            <section className="section">
                <div className="container">
                    <Card>
                        <div className="card-body">
                            <h2>Not signed in</h2>
                            <p className="muted">Please <Link to="/login">log in</Link> to see your profile.</p>
                        </div>
                    </Card>
                </div>
            </section>
        )
    }

    const handleEdit = () => {
        navigate('/profile/edit')
    }

    const handleLogout = () => {
        logout()
        navigate('/')
    }


    const roleData = user.role
    const roleLabel = roleData?.name ?? (roleData?.roleId ? `Role #${roleData.roleId}` : null)

    const handleCancelBooking = async (bookingId) => {
        const ok = window.confirm('Are you sure you want to cancel this booking?')
        if (!ok) return
        try {
            await cancelBookingMutation.mutateAsync(bookingId)
        } catch (err) {
            const serverMessage = err?.payload?.detail || err?.payload?.message || err?.message || 'Failed to cancel booking'
            alert(serverMessage)
        }
    }

    return (
        <section className="section profile-section">
            <div className="container">
                <div className="page profile-page">

                    <div className="profile-grid">
                        <Card className="profile-summary">
                            <div className="card-body" style={{ display: 'flex', gap: 16, alignItems: 'center' }}>
                                <img
                                    src={user.profilePicture || avatarPlaceholder}
                                    alt={user.name}
                                    className="profile-avatar"
                                    style={{ width: 96, height: 96, borderRadius: 8, objectFit: 'cover' }}
                                />
                                <div style={{ flex: 1 }}>
                                    <h2 style={{ margin: 0 }}>{user.name}</h2>
                                    <p className="muted" style={{ marginTop: 6 }}>{user.email}</p>
                                    {/* phone is intentionally commented out until needed */}
                                    {/* {user.phone && <p>📞 {user.phone}</p>} */}
                                    {roleLabel && <p className="muted">Role: {roleLabel}</p>}

                                    <div style={{ marginTop: 12 }}>
                                        <Button onClick={handleEdit} className="btn-primary">Edit profile</Button>
                                        <Button onClick={handleLogout} className="btn-secondary"
                                            style={{ marginLeft: 8 }}>Logout
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        </Card>

                        <div>
                            <Card>
                                <div className="card-body">
                                    <h3 style={{ marginTop: 0 }}>My bookings</h3>

                                    {loadingBookings && <p className="muted">Loading bookings...</p>}
                                    {bookingsError && <div className="form-error">{bookingsError}</div>}

                                    {!loadingBookings && bookings.length === 0 && !bookingsError && (
                                        <p className="muted">You have no bookings.</p>
                                    )}

                                    {!loadingBookings && bookings.length > 0 && (
                                        <div className="booking-list">
                                            {bookings.map((b) => {
                                                //backend BookingResponse uses `bookingId` as the identifier
                                                const bookingId = b.bookingId
                                                const statusClass = b.status ? (`status-badge ${String(b.status).toLowerCase().replace(/\s+/g, '_')}`) : 'status-badge'

                                                //compute whether this booking starts within the next 24 hours
                                                const startsAt = b.startDateTime ? new Date(b.startDateTime) : null
                                                const msUntil = startsAt ? (startsAt.getTime() - now) : null
                                                const within24h = msUntil != null ? (msUntil <= 24 * 60 * 60 * 1000) : false
                                                const minutesUntil = msUntil != null ? Math.ceil(msUntil / 60000) : null
                                                const hoursUntil = minutesUntil != null ? Math.floor(minutesUntil / 60) : null

                                                //only allow cancel when booking is BOOKED and it's not within the next 24 hours
                                                const canCancel = bookingId && String(b.status).toUpperCase() === 'BOOKED' && !within24h

                                                return (
                                                    <div key={b.bookingId} className="booking-item">
                                                        <div style={{
                                                            display: 'flex',
                                                            justifyContent: 'space-between',
                                                            gap: 12
                                                        }}>
                                                            <div>
                                                                <strong>{b.startDateTime ? new Date(b.startDateTime).toLocaleString() : 'Unknown'}</strong>
                                                                <div className="muted">With: {b.staff?.user?.name || '—'}</div>
                                                                <div className="muted">Service: {b.service?.name || '—'}</div>
                                                                {startsAt && (
                                                                    <div className="muted" style={{ marginTop: 4 }}>
                                                                        Starts in: {hoursUntil != null ? `${hoursUntil}h ${minutesUntil % 60}m` : '—'}
                                                                    </div>
                                                                )}
                                                            </div>
                                                            <div style={{
                                                                alignSelf: 'center',
                                                                display: 'flex',
                                                                flexDirection: 'column',
                                                                alignItems: 'flex-end',
                                                                gap: 8
                                                            }}>
                                                                <span
                                                                    className={statusClass}>{b.status || 'Unknown'}</span>
                                                                {canCancel && (
                                                                    <Button
                                                                        className="btn-secondary"
                                                                        disabled={cancelBookingMutation.isPending && cancelBookingMutation.variables === bookingId}
                                                                        title={within24h ? 'Cannot cancel within 24 hours of the appointment' : undefined}
                                                                        onClick={() => handleCancelBooking(bookingId)}
                                                                    >
                                                                        {cancelBookingMutation.isPending && cancelBookingMutation.variables === bookingId ? 'Cancelling...' : 'Cancel'}
                                                                    </Button>
                                                                )}
                                                                {/* show hint when cancellation is blocked due to 24-hour rule */}
                                                                {!canCancel && String(b.status).toUpperCase() === 'BOOKED' && within24h && (
                                                                    <div className="muted" style={{ marginTop: 6, fontSize: '0.9rem' }}>Cannot cancel within 24 hours of the appointment.</div>
                                                                )}
                                                            </div>
                                                        </div>
                                                    </div>
                                                )
                                            })}
                                        </div>
                                    )}
                                    {bookingSuccess &&
                                        <div className="form-success" style={{ marginTop: 8 }}>{bookingSuccess}</div>}
                                </div>
                            </Card>


                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}

export default ProfilePage
