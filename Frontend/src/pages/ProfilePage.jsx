import { useState, useContext, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useAuth } from '../hooks/useAuth'
import Card from '../components/common/Card'
import Button from '../components/common/Button'
import {cancelBooking} from '../services/api'
import {useBookingsByCustomer} from '../hooks/queries/useBookingsByCustomer'
import avatarPlaceholder from '../assets/avatar-placeholder.png'
import { UIStateContext } from '../context/UIStateContext'

function ProfilePage() {
    const { showLoading, showError, showSuccess, getErrorMessage } = useContext(UIStateContext)

    const {user, logout} = useAuth()
    const navigate = useNavigate()
    const queryClient = useQueryClient()

    const customerId = user?.userId

    const {
        data: bookings = [],
        isLoading: loadingBookings,
        error: bookingsQueryError,
    } = useBookingsByCustomer(customerId)

    const bookingsError = bookingsQueryError
        ? (bookingsQueryError?.payload?.detail || bookingsQueryError?.payload?.message || bookingsQueryError?.message || 'Failed to load bookings')
        : null

    const [showDebug] = useState(false)

    // track which booking is currently being cancelled so we can show per-item loading
    const [cancellingId, setCancellingId] = useState(null)

    // capture current time once per render cycle via state factory to satisfy purity lint rules
    const [now] = useState(() => Date.now())

    const cancelBookingMutation = useMutation({
        mutationFn: async (bookingId) => cancelBooking(bookingId),
        onSuccess: async () => {
            await queryClient.invalidateQueries({queryKey: ['bookings', customerId]})
            showSuccess('Booking cancelled')
        },
        onError: (error) => {
            const serverMessage = getErrorMessage(error, 'Failed to cancel booking. Please try again.')
            showError(serverMessage)
        },
        onSettled: () => {
            setCancellingId(null)
        }
    })

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

    // navigate to dedicated change-password page
    const goToChangePassword = () => navigate('/profile/change-password')

    const roleData = user.role
    const roleLabel = roleData?.name ? String(roleData.name).toUpperCase() : (roleData?.roleId ? `Role #${roleData.roleId}` : null)

    const handleCancelBooking = async (bookingId) => {
        if (!bookingId) {
            showError('Booking id missing, cannot cancel')
            return
        }
        const ok = window.confirm('Are you sure you want to cancel this booking?')
        if (!ok) return
        try {
            setCancellingId(bookingId)
            await cancelBookingMutation.mutateAsync(bookingId)
        } catch (err) {
            const serverMessage = getErrorMessage(err, 'Failed to cancel booking')
            showError(serverMessage)
        }
    }

    const sortedBookings = Array.isArray(bookings) ? [...bookings].sort((a, b) => {
        const aBooked = String(a.status || '').toUpperCase() === 'BOOKED'
        const bBooked = String(b.status || '').toUpperCase() === 'BOOKED'
        if (aBooked !== bBooked) return bBooked - aBooked

        const ta = a.startDateTime ? new Date(a.startDateTime).getTime() : Number.NEGATIVE_INFINITY
        const tb = b.startDateTime ? new Date(b.startDateTime).getTime() : Number.NEGATIVE_INFINITY
        return tb - ta
    }) : []

    const formatTimeUntil = (msDiff) => {
        if (msDiff == null) return '—'
        //if already started (negative) show Started
        if (msDiff < 0) return 'Started'
        const totalMinutes = Math.ceil(msDiff / 60000)
        const totalHours = Math.floor(totalMinutes / 60)
        const days = Math.floor(totalHours / 24)
        const hours = totalHours % 24
        const minutes = totalMinutes % 60
        if (days > 0) return `${days}d ${hours}h`
        return `${hours}h ${minutes}m`
    }

    //format date as YYYY.MM.DD HH:MM (local time)
    const formatDisplayDate = (iso) => {
        if (!iso) return 'Unknown'
        const d = new Date(iso)
        if (Number.isNaN(d.getTime())) return 'Unknown'
        const yyyy = d.getFullYear()
        const mm = String(d.getMonth() + 1).padStart(2, '0')
        const dd = String(d.getDate()).padStart(2, '0')
        const hh = String(d.getHours()).padStart(2, '0')
        const min = String(d.getMinutes()).padStart(2, '0')
        return `${yyyy}.${mm}.${dd} ${hh}:${min}`
    }

    useEffect(() => {
        if (bookingsError) {
            showError(bookingsError, "Failed to load the bookings.")
        }
    }, [bookingsError])

    return (
        <section className="section profile-section">
            <div className="container">
                <div className="page profile-page">

                    <div className="profile-grid">
                        <Card className="profile-summary">
                            <div className="card-body" style={{display: 'flex', gap: 16, alignItems: 'center'}}>
                                <img
                                    src={user.profilePicture || avatarPlaceholder}
                                    alt={user.name}
                                    className="profile-avatar"
                                    style={{width: 96, height: 96, borderRadius: 8, objectFit: 'cover'}}
                                />
                                <div style={{flex: 1}}>
                                    <h2 style={{margin: 0}}>{user.name}</h2>
                                    <p className="muted" style={{marginTop: 6}}>{user.email}</p>
                                    {user.phoneNumber && <p>📞 {user.phoneNumber}</p>}
                                    {roleLabel && <p className="muted">Role: {roleLabel}</p>}

                                    <div style={{marginTop: 12}}>
                                        <Button onClick={handleEdit} className="btn-primary">Edit profile</Button>
                                        <Button onClick={goToChangePassword} className="btn" style={{marginLeft: 8}}>Change password</Button>
                                        <Button onClick={handleLogout} className="btn-secondary"
                                                style={{marginLeft: 8}}>Logout
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        </Card>

                        <div>
                            <Card>
                                <div className="card-body">
                                    <h3 style={{marginTop: 0}}>My bookings</h3>

                                    {loadingBookings && <p className="muted">Loading bookings...</p>}

                                    {!loadingBookings && bookings.length === 0 && !bookingsError && (
                                        <p className="muted">You have no bookings.</p>
                                    )}

                                    {!loadingBookings && sortedBookings.length > 0 && (
                                        <div className="booking-list">
                                            {sortedBookings.map((b, idx) => {
                                                // prefer bookingId but fall back to id
                                                const bookingId = b.bookingId ?? b.id
                                                const statusClass = b.status ? (`status-badge ${String(b.status).toLowerCase().replace(/\s+/g, '_')}`) : 'status-badge'

                                                //compute whether this booking starts within the next 24 hours
                                                const startsAt = b.startDateTime ? new Date(b.startDateTime) : null
                                                const msUntil = startsAt ? (startsAt.getTime() - now) : null
                                                const within24h = msUntil != null ? (msUntil <= 24 * 60 * 60 * 1000) : false
                                                // minutes/hours are computed inside formatTimeUntil when needed

                                                //only allow cancel when booking is BOOKED and it's not within the next 24 hours
                                                const canCancel = bookingId && String(b.status).toUpperCase() === 'BOOKED' && !within24h

                                                return (
                                                    <div key={bookingId ?? `bk-${idx}`} className="booking-item">
                                                        <div style={{
                                                            display: 'flex',
                                                            justifyContent: 'space-between',
                                                            gap: 12
                                                        }}>
                                                            <div>
                                                                <strong>{b.startDateTime ? formatDisplayDate(b.startDateTime) : 'Unknown'}</strong>
                                                                <div
                                                                    className="muted">With: {b.staff?.user?.name || '—'}</div>
                                                                <div
                                                                    className="muted">Phone: {b.staff?.user?.phoneNumber || '—'}</div>
                                                                <div
                                                                    className="muted">Service: {b.service?.name ? `${b.service.name} | ${b.service?.price != null ? ` ${b.service.price} Ft` : ''}` : '—'}</div>
                                                                {startsAt && String(b.status).toUpperCase() === 'BOOKED' && (
                                                                    <div className="muted" style={{marginTop: 4}}>
                                                                        Starts in: {formatTimeUntil(msUntil)}
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
                                                                        disabled={cancelBookingMutation.isLoading && cancellingId === bookingId}
                                                                        title={within24h ? 'Cannot cancel within 24 hours of the appointment' : undefined}
                                                                        onClick={() => handleCancelBooking(bookingId)}
                                                                    >
                                                                        {cancelBookingMutation.isLoading && cancellingId === bookingId ? 'Cancelling...' : 'Cancel'}
                                                                    </Button>
                                                                )}
                                                                {/* show hint when cancellation is blocked due to 24-hour rule */}
                                                                {!canCancel && String(b.status).toUpperCase() === 'BOOKED' && within24h && (
                                                                    <div className="muted" style={{
                                                                        marginTop: 6,
                                                                        fontSize: '0.9rem'
                                                                    }}>
                                                                        Cannot cancel within 24 hours of the appointment.
                                                                    </div>
                                                                )}

                                                            </div>
                                                        </div>
                                                    </div>
                                                )
                                            })}
                                        </div>
                                    )}
                                </div>
                            </Card>


                        </div>
                    </div>


                    {showDebug && (
                        <Card className="debug-card" style={{marginTop: 12}}>
                            <div className="card-body">
                                <h4>Debug</h4>
                                <pre style={{maxHeight: 200, overflow: 'auto'}}>{JSON.stringify({
                                    user,
                                    servicesCount: 'n/a',
                                    bookingsCount: bookings.length,
                                    bookings
                                }, null, 2)}</pre>
                            </div>
                        </Card>
                    )}
                </div>
            </div>
        </section>
    )
}

export default ProfilePage
