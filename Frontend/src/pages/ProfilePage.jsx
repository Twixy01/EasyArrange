import { useEffect, useState, useContext } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import Card from '../components/common/Card'
import { getBookingsByCustomer, updateBooking } from '../services/api'
import { UIStateContext } from '../context/UIStateContext'

function ProfilePage() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const { services } = useContext(UIStateContext)
  const [bookings, setBookings] = useState([])
  const [loadingBookings, setLoadingBookings] = useState(false)
  const [bookingsError, setBookingsError] = useState(null)
  const [cancelingIds, setCancelingIds] = useState(new Set())
  const [bookingSuccess, setBookingSuccess] = useState(null)
  const [showDebug, setShowDebug] = useState(false)

  // reusable loader so we can refresh after cancel
  async function loadBookings() {
    if (!user) return
    const customerId = user.userId
    if (!customerId) return

    setLoadingBookings(true)
    setBookingsError(null)
    try {
      const b = await getBookingsByCustomer(customerId)
      setBookings(Array.isArray(b) ? b : [])
    } catch (err) {
      console.warn('Failed to load bookings for user', err)
      setBookingsError('Failed to load bookings')
      setBookings([])
    } finally {
      setLoadingBookings(false)
    }
  }

  useEffect(() => {
    loadBookings()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user])

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

  const userServices = (user.serviceIds && Array.isArray(user.serviceIds) && Array.isArray(services))
    ? services.filter(s => user.serviceIds.includes(s.serviceId))
    : []

  const roleData = user.role
  const roleLabel = roleData?.name ?? (roleData?.roleId ? `Role #${roleData.roleId}` : null)

  return (
    <section className="section profile-section">
      <div className="container">
        <div className="page profile-page">
          <div style={{ position: 'absolute', right: 12, top: 12 }}>
            <button className="btn" onClick={() => setShowDebug(s => !s)} style={{ fontSize: 12 }}>{showDebug ? 'Hide debug' : 'Show debug'}</button>
          </div>

          <div className="profile-grid">
            <Card className="profile-summary">
              <div className="card-body" style={{ display: 'flex', gap: 16, alignItems: 'center' }}>
                <img
                  src={user.profilePicture || '../assets/avatar-placeholder.png'}
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
                    <button onClick={handleEdit} className="btn btn-primary">Edit profile</button>
                    <button onClick={handleLogout} className="btn btn-secondary" style={{ marginLeft: 8 }}>Logout</button>
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
                      {bookings.map(b => {
                        const statusClass = b.status ? (`status-badge ${String(b.status).toLowerCase().replace(/\s+/g, '_')}`) : 'status-badge'
                        const canCancel = String(b.status).toUpperCase() === 'BOOKED'
                        return (
                          <div key={b.id} className="booking-item">
                            <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12 }}>
                              <div>
                                <strong>{b.startDateTime ? new Date(b.startDateTime).toLocaleString() : 'Unknown'}</strong>
                                <div className="muted">With: {b.staff?.user?.name || '—'}</div>
                                <div className="muted">Service: {b.service?.name || '—'}</div>
                              </div>
                              <div style={{ alignSelf: 'center', display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 8 }}>
                                <span className={statusClass}>{b.status || 'Unknown'}</span>
                                {canCancel && (
                                  <button
                                    className="btn btn-secondary"
                                    disabled={cancelingIds.has(b.id)}
                                    onClick={async () => {
                                      setBookingsError(null)
                                      const ok = window.confirm('Are you sure you want to cancel this booking?')
                                      if (!ok) return
                                      // mark as canceling
                                      setCancelingIds(prev => new Set(prev).add(b.id))
                                      try {
                                        // prepare update payload - backend requires startDateTime, endDateTime, serviceId, status
                                        const payload = {
                                          startDateTime: b.startDateTime,
                                          endDateTime: b.endDateTime || b.startDateTime, // fallback if end missing
                                          serviceId: b.service?.serviceId || b.serviceId,
                                          status: 'CANCELLED'
                                        }
                                        await updateBooking(b.id, payload)
                                        // refresh bookings from server so UI matches server state
                                        await loadBookings()
                                        setBookingSuccess('Booking cancelled')
                                        setTimeout(() => setBookingSuccess(null), 3000)
                                      } catch (err) {
                                        console.error('Failed to cancel booking', err)
                                        setBookingsError('Failed to cancel booking')
                                      } finally {
                                        // remove from canceling set
                                        setCancelingIds(prev => {
                                          const s = new Set(prev)
                                          s.delete(b.id)
                                          return s
                                        })
                                      }
                                    }}
                                  >
                                    {cancelingIds.has(b.id) ? 'Cancelling...' : 'Cancel'}
                                  </button>
                                )}
                              </div>
                            </div>
                          </div>
                        )
                      })}
                    </div>
                  )}
                  {bookingSuccess && <div className="form-success" style={{ marginTop: 8 }}>{bookingSuccess}</div>}
                </div>
              </Card>

              <div style={{ marginTop: 20 }}>
                <h3>My services</h3>
                {(!services || services.length === 0) && <p className="muted">No services available.</p>}
                <div className="grid cards-3" style={{ marginTop: 10 }}>
                  {userServices.map(s => (
                    <Card key={s.serviceId} className="service-card">
                      <div className="card-body">
                        <h4 style={{ margin: 0 }}>{s.name}</h4>
                        <p className="muted" style={{ marginTop: 6 }}>{s.description}</p>
                      </div>
                    </Card>
                  ))}
                </div>
              </div>
            </div>
          </div>

          {showDebug && (
            <Card className="debug-card" style={{ marginTop: 12 }}>
              <div className="card-body">
                <h4>Debug</h4>
                <pre style={{ maxHeight: 200, overflow: 'auto' }}>{JSON.stringify({ user, servicesCount: Array.isArray(services) ? services.length : services, bookingsCount: bookings.length, bookings }, null, 2)}</pre>
              </div>
            </Card>
          )}
        </div>
      </div>
    </section>
  )
}

export default ProfilePage
