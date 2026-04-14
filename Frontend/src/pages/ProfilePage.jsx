import { useEffect, useState, useContext } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import Card from '../components/common/Card'
import { getBookingsByCustomer } from '../services/api'
import { UIStateContext } from '../context/UIStateContext'

function ProfilePage() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const { services } = useContext(UIStateContext)
  const [bookings, setBookings] = useState([])
  const [loadingBookings, setLoadingBookings] = useState(false)
  const [bookingsError, setBookingsError] = useState(null)
  const [showDebug, setShowDebug] = useState(false)

  useEffect(() => {
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

    loadBookings()
  }, [user])

  if (!user) {
    return (
      <div className="page profile-page">
        <Card>
          <h2>Not signed in</h2>
          <p className="muted">Please <Link to="/login">log in</Link> to see your profile.</p>
        </Card>
      </div>
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
    <div className="page profile-page">
      <div style={{ position: 'absolute', right: 12, top: 12 }}>
        <button className="btn" onClick={() => setShowDebug(s => !s)} style={{ fontSize: 12 }}>{showDebug ? 'Hide debug' : 'Show debug'}</button>
      </div>
      <Card className="profile-summary">
        <div className="profile-top">
          <img
            src={user.profilePicture || '../assets/avatar-placeholder.png'}
            alt={user.name}
            className="profile-avatar"
            style={{ width: 96, height: 96, borderRadius: 8 }}
          />
          <div style={{ marginLeft: 16 }}>
            <h2>{user.name}</h2>
            <p className="muted">{user.email}</p>
            {/* {user.phone && <p>📞 {user.phone}</p>} */}
            {roleLabel && <p className="muted">Role: {roleLabel}</p>}
            <div style={{ marginTop: 8 }}>
              <button onClick={handleEdit} className="btn">Edit profile</button>
              <button onClick={handleLogout} className="btn danger" style={{ marginLeft: 8 }}>Logout</button>
            </div>
          </div>
        </div>
      </Card>

      {showDebug && (
        <Card className="debug-card" style={{ marginTop: 12 }}>
          <h4>Debug</h4>
          <pre style={{ maxHeight: 200, overflow: 'auto' }}>{JSON.stringify({ user, servicesCount: Array.isArray(services) ? services.length : services, bookingsCount: bookings.length, bookings }, null, 2)}</pre>
        </Card>
      )}

      <div style={{ marginTop: 20 }}>
        <h3>My services</h3>
        {(!services || services.length === 0) && <p className="muted">No services available.</p>}
        <div className="grid cards-3">
          {userServices.map(s => (
            <Card key={s.serviceId} service={s} />
          ))}
        </div>
      </div>

      <div style={{ marginTop: 20 }}>
        <h3>My bookings</h3>
        {loadingBookings && <p className="muted">Loading bookings...</p>}
        {bookingsError && <p className="muted">{bookingsError}</p>}
        {!loadingBookings && bookings.length === 0 && !bookingsError && (
          <p className="muted">You have no bookings.</p>
        )}
        {!loadingBookings && bookings.length > 0 && (
          <div className="bookings-list">
            {bookings.map(b => (
              <div key={b.id} className="booking-row card" style={{ padding: 8, marginBottom: 8 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <div>
                    <strong>{b.startDateTime ? new Date(b.startDateTime).toLocaleString() : 'Unknown'}</strong>
                    <div className="muted">With: {b.staff?.user?.name}</div>
                    <div className="muted">Service: {b.service?.name}</div>
                  </div>
                  <div style={{ alignSelf: 'center' }}>
                    <span className="status">{b.status}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default ProfilePage
