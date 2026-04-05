import { useEffect, useState } from "react";
import Card from "../components/common/Card";
import { useServices } from "../hooks/queries/useServices";
import { useAvailableStaff } from "../hooks/queries/useAvailableStaff";
import { useAvailableSlots } from "../hooks/queries/useAvailableSlots";

function BookingPage() {
  const today = new Date().toISOString().split("T")[0];

  const [selectedService, setSelectedService] = useState(null);
  const [selectedStaff, setSelectedStaff] = useState(null);
  const [selectedDate, setSelectedDate] = useState(today);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [success, setSuccess] = useState("");

  const {
    data: services = [],
    isLoading: servicesLoading,
    error: servicesError,
  } = useServices();

  const {
    data: availableStaff = [],
    isLoading: staffLoading,
    error: staffError,
  } = useAvailableStaff(selectedService?.serviceId);

  const {
    data: slots = [],
    isLoading: slotsLoading,
    error: slotsError,
  } = useAvailableSlots(selectedStaff?.staffId, selectedDate);

  useEffect(() => {
    setSelectedStaff(null);
    setSelectedSlot(null);
  }, [selectedService]);

  useEffect(() => {
    setSelectedSlot(null);
  }, [selectedStaff, selectedDate]);

  if (servicesLoading) {
    return <p>Loading services...</p>;
  }

  if (servicesError) {
    return <p>Failed to load services.</p>;
  }

  return (
    <>
      <div className="booking-step">
        <h3>1. Select a service</h3>
        <div className="grid cards-3">
          {services.map((service) => (
            <Card
              key={service.serviceId}
              className={`select-card ${
                String(selectedService?.serviceId) === String(service.serviceId)
                  ? "selected"
                  : ""
              }`}
            >
              <button
                type="button"
                className="select-card-button"
                onClick={() => {
                  setSelectedService(service);
                  setSuccess("");
                }}
              >
                <div>
                  <h4>{service.name}</h4>
                  <p>{service.description}</p>
                </div>
                <div className="service-meta">
                  <span>{service.price} HUF</span>
                  <span>{service.duration} min</span>
                </div>
              </button>
            </Card>
          ))}
        </div>
      </div>

      <div className="booking-step">
        <h3>2. Choose staff member</h3>

        {!selectedService ? (
          <p className="muted">Select a service first.</p>
        ) : staffLoading ? (
          <p className="muted">Loading available staff...</p>
        ) : staffError ? (
          <p className="muted">Failed to load staff.</p>
        ) : availableStaff.length === 0 ? (
          <p className="muted">No staff available for this service.</p>
        ) : (
          <div className="grid cards-3">
            {availableStaff.map((member) => (
              <Card
                key={member.staffId}
                className={`select-card ${
                  Number(selectedStaff?.staffId) === Number(member.staffId)
                    ? "selected"
                    : ""
                }`}
              >
                <button
                  type="button"
                  className="select-card-button"
                  onClick={() => {
                    setSelectedStaff(member);
                    setSuccess("");
                  }}
                >
                  <div className="staff-inline">
                    <img
                      src={member.user?.profilePicture}
                      alt={member.user?.name || "Staff member"}
                    />
                    <div>
                      <h4>{member.user?.name}</h4>
                      <p>{member.title}</p>
                    </div>
                  </div>
                </button>
              </Card>
            ))}
          </div>
        )}
      </div>

      <div className="booking-step">
        <h3>3. Select date</h3>
        <div className="date-picker-wrap">
          <input
            className="date-input"
            type="date"
            value={selectedDate}
            min={today}
            onChange={(e) => {
              setSelectedDate(e.target.value);
              setSuccess("");
            }}
          />
        </div>
      </div>

      <div className="booking-step">
        <h3>4. Choose available time slot</h3>

        {!selectedStaff ? (
          <p className="muted">Select a staff member first.</p>
        ) : slotsLoading ? (
          <p className="muted">Loading available slots...</p>
        ) : slotsError ? (
          <p className="muted">Failed to load time slots.</p>
        ) : slots.length === 0 ? (
          <p className="muted">No available slots for the current selection.</p>
        ) : (
          <div className="slots-grid">
            {slots.map((slot) => (
              <button
                type="button"
                key={slot.start}
                className={`slot-btn ${
                  selectedSlot?.start === slot.start ? "active" : ""
                }`}
                onClick={() => {
                  setSelectedSlot(slot);
                  setSuccess("");
                }}
              >
                {slot.label}
              </button>
            ))}
          </div>
        )}
      </div>

      {success && <p className="success-message">{success}</p>}
    </>
  );
}

export default BookingPage;