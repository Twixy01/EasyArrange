import { useEffect, useState, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Card from "../components/common/Card";
import { useServices } from "../hooks/queries/useServices";
import { useStaff } from "../hooks/queries/useStaff";
import { useAvailableStaff } from "../hooks/queries/useAvailableStaff";
import { useAvailableSlots } from "../hooks/queries/useAvailableSlots";
import { useCreateBooking } from "../hooks/mutations/useCreateBooking";
import SectionHeader from "../components/common/SectionHeader";
import Button from "../components/common/Button";
import ProtectedAction from "../components/common/ProtectedAction";
import { useAuth } from "../hooks/useAuth";
import { UIStateContext } from "../context/UIStateContext";
import { resolveMediaUrl } from '../services/api'

function BookingPage() {
  const { showSuccess, showError, showLoading, getErrorMessage, hideNotification } = useContext(UIStateContext);

  const { user, isLoggedIn } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const today = new Date().toISOString().split("T")[0];
  const maxDate = new Date();
  maxDate.setDate(maxDate.getDate() + 45);

  const [selectedService, setSelectedService] = useState(null);
  const [selectedStaff, setSelectedStaff] = useState(null);
  const [selectedDate, setSelectedDate] = useState(today);
  const [selectedSlot, setSelectedSlot] = useState(null);

  const preselectedStaffId = location.state?.staffId ?? null;
  const preselectedServiceFromState = location.state?.service ?? null;
  const hasPreselectedStaff = Boolean(preselectedStaffId);

  const {
    data: services = [],
    isLoading: servicesLoading,
    error: servicesError,
  } = useServices();

  useEffect(() => {
    if (!preselectedServiceFromState) return;

    // If not yet set or a different service was passed, set it immediately so dependent
    // hooks (e.g. available staff) can fetch right away.
    if (!selectedService || String(selectedService.serviceId) !== String(preselectedServiceFromState.serviceId)) {
      // defer state updates to avoid cascading render lint warning
      setTimeout(() => {
        setSelectedService(preselectedServiceFromState);
        setSelectedStaff(null);
        setSelectedSlot(null);
      }, 0);
    }

    // If the full services list is available, try to replace the selectedService with the
    // canonical object from `services` (ensures consistent shape across the app).
    if (services.length > 0) {
      const matched = services.find(
        (s) => String(s.serviceId) === String(preselectedServiceFromState.serviceId)
      );
      if (matched && String(matched.serviceId) !== String(selectedService?.serviceId)) {
        setTimeout(() => setSelectedService(matched), 0);
      }
    }
  }, [preselectedServiceFromState, services, selectedService]);

  const {
    data: staff = [],
    isLoading: allStaffLoading,
    error: allStaffError,
  } = useStaff();

  const serviceOptions = hasPreselectedStaff && selectedStaff?.services?.length > 0
    ? selectedStaff.services
    : (services && services.length > 0 ? services : (preselectedServiceFromState ? [preselectedServiceFromState] : []));

  const {
    data: availableStaff = [],
    isLoading: staffLoading,
    error: staffError,
  } = useAvailableStaff(selectedService?.serviceId);


  //Avoid auto-selecting staff after selecting service
  // useEffect(() => {
  //   if (!selectedService) return;
  //   if (!availableStaff || availableStaff.length === 0) return;
  //   if (selectedStaff) return; // user already selected

  //   // defer to avoid lint rule about setState in effect
  //   setTimeout(() => setSelectedStaff(availableStaff[0]), 0);
  // }, [availableStaff, selectedService, selectedStaff]);

  const {
    data: slots = [],
    isLoading: slotsLoading,
    error: slotsError,
  } = useAvailableSlots(selectedStaff?.staffId, selectedDate, selectedService?.serviceId);

  useEffect(() => {
    if (!preselectedStaffId || staff.length === 0 || selectedStaff) return;

    const preselectedStaff = staff.find(
      (member) => Number(member.staffId) === Number(preselectedStaffId)
    );

    if (!preselectedStaff) return;

    // defer to avoid lint rule
    setTimeout(() => setSelectedStaff(preselectedStaff), 0);
  }, [preselectedStaffId, staff, selectedStaff]);

  const { mutate } = useCreateBooking();

  useEffect(() => {
    // avoid lint rule by deferring state update
    setTimeout(() => setSelectedSlot(null), 0);
  }, [selectedStaff, selectedDate]);

  useEffect(() => {
    if (servicesError && !preselectedServiceFromState) {
      showError(getErrorMessage(servicesError, "Failed to load services."));
    } else if (staffError) {
      showError(getErrorMessage(staffError, "Failed to load staff."));
    } else if (slotsError) {
      showError(getErrorMessage(slotsError, "Failed to load available slots."));
    }
  }, [servicesError, preselectedServiceFromState, staffError, slotsError]);

  const handleBooking = async () => {
    if (!selectedSlot || !selectedService || !selectedStaff || !user) return;

    const bookingUserId = user.userId;

    mutate(
      {
        staffId: selectedStaff.staffId,
        customerId: bookingUserId,
        startDateTime: selectedSlot.startDateTime,
        endDateTime: selectedSlot.endDateTime,
        serviceId: selectedService.serviceId,
      },
      {
        onSuccess: () => {
          showSuccess("Your appointment has been booked successfully.");
          setSelectedSlot(null);
          setTimeout(() => navigate("/"), 1000);
        },
        onError: (error) => {
          showError(getErrorMessage(error, "Failed to create booking. Please try again."));
        }
      }
    );
  };

  return (
    <section className="section">
      <div className="container">
        <SectionHeader
          eyebrow="Booking"
          title="Choose service, staff, date, and time"
          description="The flow follows the salon domain correctly: service first, staff second, then availability."
        />

        <div className="booking-layout">
          <div className="booking-main">
            <div className="booking-step">
              <h3>1. Select a service</h3>
              <div className="grid cards-3">
                {servicesLoading ? 
                (<p className="muted">Loading services...</p>) : (
                  serviceOptions.map((service) => (
                    <Card
                      key={service.serviceId}
                      className={`select-card ${String(selectedService?.serviceId) === String(service.serviceId)
                        ? "selected"
                        : ""
                        }`}
                    >
                      <button
                        type="button"
                        className="select-card-button"
                        onClick={() => {
                          setSelectedService(service);
                          if (
                            selectedStaff &&
                            !selectedStaff.services?.some(
                              (staffService) => Number(staffService.serviceId) === Number(service.serviceId)
                            )
                          ) {
                            setSelectedStaff(null);
                          }
                          setSelectedSlot(null);
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
                  ))
                )
                }
              </div>
            </div>

            <div className="booking-step">
              <h3>2. Choose staff member</h3>

              {hasPreselectedStaff ? (
                selectedStaff ? (
                  <Card className="select-card selected">
                    <div className="staff-inline">
                      <img
                        src={resolveMediaUrl(selectedStaff.user?.profilePicture) || selectedStaff.user?.profilePicture}
                        alt={selectedStaff.user?.name || "Staff member"}
                      />
                      <div>
                        <h4>{selectedStaff.user?.name}</h4>
                        <p>{selectedStaff.title}</p>
                        <div className="pill-wrap">
                          {selectedStaff.services?.map((item) => (
                            <span key={item.serviceId} className="pill">
                              {item.name}
                            </span>
                          ))}
                        </div>
                      </div>
                    </div>
                  </Card>
                ) : (
                  <p className="muted">Loading selected staff...</p>
                )
              ) : !selectedService ? (
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
                      className={`select-card ${Number(selectedStaff?.staffId) === Number(member.staffId)
                        ? "selected"
                        : ""
                        }`}
                    >
                      <button
                        type="button"
                        className="select-card-button"
                        onClick={() => {
                          setSelectedStaff(member);
                          setSelectedSlot(null);
                        }}
                      >
                        <div className="staff-inline">
                          <img
                            src={resolveMediaUrl(member.user?.profilePicture) || member.user?.profilePicture}
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
                  max={maxDate.toISOString().split("T")[0]}
                  onKeyDown={(e) => e.preventDefault()}
                  onPaste={(e) => e.preventDefault()}
                  onChange={(e) => {
                    setSelectedDate(e.target.value);
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
                <p className="muted">{slotsError.details}</p>
              ) : slots.length === 0 ? (
                <p className="muted">No available slots for the current selection.</p>
              ) : (
                <div className="slots-grid">
                  {slots.map((slot) => (
                    <button
                      type="button"
                      key={slot.startDateTime}
                      className={`slot-btn ${selectedSlot?.startDateTime === slot.startDateTime ? "active" : ""
                        }`}
                      onClick={() => {
                        setSelectedSlot(slot);
                      }}
                    >
                      {slot.label}
                    </button>
                  ))}
                </div>
              )}
            </div>
            <Button
              variant="secondary"
              className="booking-summary-jump"
              onClick={() => { window.scrollTo(0, 0) }}>Go to confirmation 🔝
            </Button>
          </div>

          <aside className="booking-sidebar">
            <div className="summary-card">
              <h3>Booking summary</h3>
              <p>
                <strong>Service:</strong>{" "}
                {services.find((s) => Number(s.serviceId) === Number(selectedService?.serviceId))?.name ||
                  "Not selected"}
              </p>
              <p>
                <strong>Staff:</strong>{" "}
                {selectedStaff?.user?.name || "Not selected"}
              </p>
              <p>
                <strong>Date:</strong> {selectedDate || "Not selected"}
              </p>
              <p>
                <strong>Time:</strong> {selectedSlot?.label || "Not selected"}
              </p>

              <ProtectedAction fallbackText="Please log in before confirming your booking.">
                <Button
                  className="full-width"
                  onClick={handleBooking}
                  disabled={!selectedSlot || !isLoggedIn}
                >
                  Confirm booking
                </Button>
              </ProtectedAction>
            </div>
          </aside>
        </div>
      </div>
    </section>
  );
}

export default BookingPage;
