import { useState, useMemo } from 'react'
import { useServices } from '../hooks/queries/useServices'
import ServiceCard from '../components/common/ServiceCard'
import SectionHeader from '../components/common/SectionHeader'

function ServicesPage() {
  const { data: services = [], isLoading, error } = useServices()
  const [query, setQuery] = useState('')

  const filtered = useMemo(() => {
    if (!query) return services || []
    const q = query.toLowerCase()
    return (services || []).filter(s => (s.name || '').toLowerCase().includes(q))
  }, [services, query])


  const demoServices = [
    { serviceId: 'demo-1', name: 'Signature Haircut', description: 'Classic men\'s haircut with styling', durationMinutes: 30, price: 25 },
    { serviceId: 'demo-2', name: 'Beard Trim', description: 'Precise beard shaping and trim', durationMinutes: 20, price: 15 },
    { serviceId: 'demo-3', name: 'Full Colour', description: 'Professional hair colouring session', durationMinutes: 90, price: 60 }
  ]

  const showServices = (error || (!services || services.length === 0)) ? demoServices : filtered

  return (
    <section className="section">
      <div className="container">
        <SectionHeader
          eyebrow="Our services"
          title="What we offer"
          description="Choose a service to see available staff and book a slot."
          center
        />

        <div style={{ marginBottom: 12 }}>
          <input
            placeholder="Search services"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
          />
        </div>

        {isLoading ? (
          <p>Loading services...</p>
        ) : (
          <div className="grid cards-3">
            {showServices.map((s) => (
              <ServiceCard key={s.serviceId} service={s} />
            ))}
          </div>
        )}

        {error && (
          <p className="muted">Failed to load services from backend; showing demo data.</p>
        )}
      </div>
    </section>
  )
}

export default ServicesPage