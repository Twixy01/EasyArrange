import { useState, useEffect, useContext } from "react"
import { DataContext } from "../context/DataContext"
import Card from "../components/common/Card"
import { getStaffByService, getAvailableSlots } from "../services/api"

function BookingPage() {
    const { services } = useContext(DataContext);
    const [availableStaff, setAvailableStaff] = useState([]);
    const [slots, setSlots] = useState([]);

    const [selectedService, setSelectedService] = useState(null);
    const [selectedStaff, setSelectedStaff] = useState(null);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [selectedSlot, setSelectedSlot] = useState(null);
    const [success, setSuccess] = useState("");

    

    useEffect(() => {
        if (!selectedService) return;
        const loadAvailableStaff = async () => {
            try {
                const response = await getStaffByService(selectedService.serviceId);
                if (response.length === 0) return;
                setAvailableStaff(response);
            } catch (error) {
                console.error(error);
            }
        };

        loadAvailableStaff();
    }, [selectedStaff, selectedService]);

    useEffect(() => {
        if (!selectedStaff || !selectedDate) return;
        const loadAvailableSlots = async () => {
            try {
                const response = await getAvailableSlots(selectedStaff.staffId, selectedDate);
                setSlots(response);
            } catch (error) {
                console.error(error);
            }
        };

        loadAvailableSlots();
    }, [selectedStaff, selectedDate]);


    return (
        <>
            <div className="booking-step">
              <h3>1. Select a service</h3>
              <div className="grid cards-3">
                {services.map((service) => (
                  <Card
                    key={service.serviceId}
                    className={`select-card ${
                      String(selectedService) === String(service.serviceId)
                        ? "selected"
                        : ""
                    }`}
                  >
                    <button
                      className="select-card-button"
                      onClick={() => setSelectedService(service)}
                    >
                      <div>
                        <h4>{service.name}</h4>
                        <p>{service.description}</p>
                      </div>
                      <div className="service-meta">
                        <span>${service.price}</span>
                        <span>{service.duration} min</span>
                      </div>
                    </button>
                  </Card>
                ))}
              </div>
            </div>

            <div className="booking-step">
              <h3>2. Choose staff member</h3>
              <div className="grid cards-3">
                {availableStaff.length === 0 && (
                  <p className="muted">Select a service to see available staff.</p>
                )}
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
                      className="select-card-button"
                      onClick={() => setSelectedStaff(member)}
                    >
                      <div className="staff-inline">
                        <img src={member.user.profilePicture} alt={member.user.name} />
                        <div>
                          <h4>{member.user.name}</h4>
                          <p>{member.title}</p>
                        </div>
                      </div>
                    </button>
                  </Card>
                ))}
              </div>
            </div>

            <div className="booking-step">
              <h3>3. Select date</h3>
              <div className="date-picker-wrap">
                <input
                  className="date-input"
                  type="date"
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                />
              </div>
            </div>
            
            <div className="booking-step">
              <h3>4. Choose available time slot</h3>
              <div className="slots-grid">
                {slots.length === 0 ? (
                  <p className="muted">
                    No available slots for the current selection.
                  </p>
                ) : (
                  slots.map((slot) => (
                    <button
                      key={slot.start}
                      className={`slot-btn ${
                        selectedSlot?.start === slot.start ? "active" : ""
                      }`}
                      onClick={() => setSelectedSlot(slot)}
                    >
                      {slot.label}
                    </button>
                  ))
                )}
              </div>
            </div>


            

        </>
    )
}

export default BookingPage