import { Link, useNavigate } from 'react-router-dom'
import Card from './Card'

export default function ServiceCard({ service }) {
  const navigate = useNavigate()
  const {
    serviceId,
    name,
    description,
    durationMinutes,
    duration,
    price
  } = service || {}

  const displayDuration = duration ?? durationMinutes

  //if there's no service passed in show a clear error message
  if (!service) {
    return (
      <Card className="service-card">
        <h4>Service not found</h4>
        <p className="muted">We couldn't load this service.</p>
      </Card>
    )
  }

  return (
    <Card className="service-card">
      <h4>{name}</h4>
      {description && <p className="muted">{description}</p>}
      <div className="service-meta">
        {displayDuration != null && <span>{displayDuration} min</span>}
        {price != null && <span style={{ marginLeft: 8 }}>{price} Ft</span>}
      </div>

      <div style={{ marginTop: 12 }}>
        <button
          type="button"
          className="btn btn-primary"
          onClick={() => navigate('/booking', { state: { service } })}
        >
          Book
        </button>

        <Link
          to={`/staff`}
          state={{ serviceId }}
          className="btn btn-secondary"
          style={{ marginLeft: 8 }}
        >
          View staff
        </Link>
      </div>
    </Card>
  )
}
