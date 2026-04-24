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
      <div className="card-body service-card-body">
        <h4>{name}</h4>
        {description && <p className="muted service-card-description">{description}</p>}
        <div className="service-meta">
          {displayDuration != null && <span>{displayDuration} min</span>}
          {price != null && <span>{price} Ft</span>}
        </div>

        <div className="service-card-actions">
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
          >
            View staff
          </Link>
        </div>
      </div>
    </Card>
  )
}
