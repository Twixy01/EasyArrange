import { } from 'react'
import { useServices } from '../hooks/queries/useServices'
import { useStaff } from '../hooks/queries/useStaff'
import { Link } from 'react-router-dom'
import SectionHeader from '../components/common/SectionHeader'
import { motion } from 'framer-motion'
import Card from '../components/common/Card'

export default function Home() {

  const { data: services = [], isLoading: servicesLoading, error: servicesError } = useServices();
  const { data: staff = [], isLoading: staffLoading, error: staffError } = useStaff();

  if (servicesLoading || staffLoading) {
    return (
      <section className="section">
        <div className="container">
          <p>Loading home content...</p>
        </div>
      </section>
    );
  }

  if (servicesError || staffError) {
    return (
      <section className="section">
        <div className="container">
          <p>{servicesError || staffError}</p>
        </div>
      </section>
    );
  }

  return (
    <>
      <section className="hero">
        <div className="container hero-grid">
          <motion.div
            className="hero-content"
            initial={{ opacity: 0, y: 24 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.7 }}
          >
            <p className="eyebrow">Luxury grooming experience</p>
            <h1>Book premium salon services with elegance and ease.</h1>
            <p className="hero-text">
              Haircuts, beard trims, nail care, color services, and more —
              matched with the right specialist and available in a smooth booking flow.
            </p>
            <div className="hero-actions">
              <Link to="/booking" className="btn btn-primary">
                Start Booking
              </Link>
              <Link to="/services" className="btn btn-secondary">
                Explore Services
              </Link>
            </div>
          </motion.div>

          <motion.div
            className="hero-visual"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.8 }}
          >
            <div className="glow-card">
              <img
                src="https://images.unsplash.com/photo-1521590832167-7bcbfaa6381f?auto=format&fit=crop&w=1200&q=80"
                alt="Salon interior"
              />
            </div>
          </motion.div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <SectionHeader
            eyebrow="Signature services"
            title="Designed for style, comfort, and precision"
            description="Choose your desired service first, then pick the staff member who specializes in it."
            center
          />
          <div className="grid cards-3">
            {services.map((service) => (
              <Card key={service.serviceId} className="service-card">
                <img src={service.image} alt={service.name} />
                <div className="card-body">
                  <h3>{service.name}</h3>
                  <p>{service.description}</p>
                  <div className="service-meta">
                    <span>{service.price} HUF</span>
                    <span>{service.duration} min</span>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>
      </section>

      <section className="section muted-section">
        <div className="container">
          <SectionHeader
            eyebrow="Meet the team"
            title="Specialists with real craft"
            description="Each staff member is connected to specific services to reflect your backend domain model properly."
            center
          />
          <div className="grid cards-3">
            {staff.map((member) => (
              <Card key={member.staffId} className="staff-card">
                <img
                  src={member.user?.profilePicture}
                  alt={member.user?.name}
                />
                <div className="card-body">
                  <h3>{member.user?.name}</h3>
                  <p className="muted">{member.title}</p>
                  <p>{member.bio}</p>
                  <div className="rating-row">
                    <span>★ 5.0</span>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}