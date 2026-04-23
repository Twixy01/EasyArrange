import { useEffect, useContext } from 'react'
import { useServices } from '../hooks/queries/useServices'
import { useStaff } from '../hooks/queries/useStaff'
import { Link } from 'react-router-dom'
import SectionHeader from '../components/common/SectionHeader'
import { motion } from 'framer-motion'
import Card from '../components/common/Card'
import { UIStateContext } from '../context/UIStateContext'
import { resolveMediaUrl } from '../services/api'

export default function Home() {
  const { showSuccess, showError, showLoading, hideNotification } = useContext(UIStateContext);
  const { data: services = [], error: servicesError } = useServices();
  const { data: staff = [], error: staffError } = useStaff();

  useEffect(() => {
    if (servicesError){
      showError(servicesError?.message ?? "Failed to load services.");
    }
    if (staffError){
      showError(staffError?.message ?? "Failed to load staff.");
    }
  }, [servicesError, staffError]);

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
                src="https://images.unsplash.com/photo-1633681926035-ec1ac984418a?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
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
                <img src={resolveMediaUrl(service.image) || service.image} alt={service.name} />
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
                  src={resolveMediaUrl(member.user?.profilePicture) || member.user?.profilePicture}
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