import { Link } from 'react-router-dom'
import Card from './Card'

export default function ServiceCard({ service }) {
  const {
    serviceId,
    name,
    description,
    durationMinutes,
    price
  } = service || {}

  return (
    <Card className="service-card">
      <h4>{name}</h4>
      {description && <p className="muted">{description}</p>}
      <div className="service-meta">
        {durationMinutes != null && <span>{durationMinutes} min</span>}
        {price != null && <span style={{ marginLeft: 8 }}>€{price}</span>}
      </div>

      <div style={{ marginTop: 12 }}>
        <Link
          to="/booking"
          state={{ service }}
          className="btn btn-primary"
        >
          Book
        </Link>

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

