import { useMemo, useState } from "react";
import { useAuth } from "../hooks/useAuth";
import SectionHeader from "../components/common/SectionHeader";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import { useCreateCalendarBlock } from "../hooks/mutations/useCreateCalendarBlock";

export default function CalendarBlockPage() {
  const { staff } = useAuth();
  const staffId = useMemo(() => staff?.staffId, [staff]);

  const tomorrow = new Date(Date.now() + 24 * 60 * 60 * 1000);
  const afterTomorrow = new Date(Date.now() + 2 * 24 * 60 * 60 * 1000);
  const minDateTime1 = tomorrow.toISOString().slice(0, 10) + "T00:00";
  const minDateTime2 = afterTomorrow.toISOString().slice(0, 10) + "T00:00";

  const [title, setTitle] = useState("Time off");
  const [isAllDay, setIsAllDay] = useState(true);
  const [selectedStartDateTime, setSelectedStartDateTime] = useState(minDateTime1);
  const [selectedEndDateTime, setSelectedEndDateTime] = useState(minDateTime2);
  const { mutate: createCalendarBlock, } = useCreateCalendarBlock();
  
  const previewText = useMemo(() => {
    if (!selectedStartDateTime || !selectedEndDateTime) return "Choose a start and end time.";

    if (isAllDay) {
      return `${selectedStartDateTime.slice(0, 10)} → ${selectedEndDateTime.slice(0, 10)}`;
    }

    return `${selectedStartDateTime.replace("T", " ")} → ${selectedEndDateTime.replace("T", " ")}`;
  }, [selectedStartDateTime, selectedEndDateTime, isAllDay]);

  const handleAllDayChange = (e) => {
    const checked = e.target.checked;
    setIsAllDay(checked);

    if (checked) {
      setSelectedStartDateTime((prev) => `${prev.slice(0, 10)}T00:00`);
      setSelectedEndDateTime((prev) => `${prev.slice(0, 10)}T00:00`);
    } else {
      setSelectedStartDateTime((prev) => `${prev.slice(0, 10)}T09:00`);
      setSelectedEndDateTime((prev) => `${prev.slice(0, 10)}T17:00`);
    }
  };

  const saveBlock = async ({ title, selectedStartDateTime, selectedEndDateTime, staffId }) => {
      await createCalendarBlock(
        {
          title,
          startDateTime: selectedStartDateTime,
          endDateTime: selectedEndDateTime,
          staffId
        },
        {
          onSuccess: () => {
            alert("Calendar block created successfully!");
            setTitle("Time off");
            setIsAllDay(true);
            setSelectedStartDateTime(minDateTime1);
            setSelectedEndDateTime(minDateTime2);
          },
          onError: (error) => {
            console.error("Error creating calendar block:", error.response?.data);
            alert("Failed to create calendar block. Please try again.");
          }
        }
      )
  };

  return (
    <section className="section calendar-block-page">
      <div className="container">
        <div className="calendar-block-layout">
          <div className="calendar-block-main">
            <SectionHeader
              eyebrow="Time Off"
              title="Block time on your calendar"
              description="Mark days or hours when you are unavailable, so bookings and shifts do not clash with your personal time."
            />

            <Card className="calendar-block-card">
              <div className="card-body calendar-block-card-body">
                <div className="calendar-block-topbar">
                  <div className="calendar-block-badge">
                    <span className="calendar-block-badge-dot" />
                    Availability control
                  </div>

                  <label className="calendar-toggle">
                    <input
                      type="checkbox"
                      checked={isAllDay}
                      onChange={handleAllDayChange}
                    />
                    <span className="calendar-toggle-slider" />
                    <span className="calendar-toggle-label">All day</span>
                  </label>
                </div>

                <div className="calendar-block-form-grid">
                  <div className="calendar-block-field calendar-block-field-full">
                    <label htmlFor="block-title">Title</label>
                    <input
                      id="block-title"
                      className="calendar-block-input"
                      type="text"
                      placeholder="Time off"
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                    />
                  </div>

                  <div className="calendar-block-field">
                    <label htmlFor="block-start">Start</label>
                    <input
                      id="block-start"
                      className="calendar-block-input"
                      type={isAllDay ? "date" : "datetime-local"}
                      value={isAllDay? selectedStartDateTime.slice(0, 10) : selectedStartDateTime}
                      min={isAllDay ? minDateTime1.slice(0, 10) : minDateTime1}
                      onKeyDown={(e) => e.preventDefault()}
                      onPaste={(e) => e.preventDefault()}
                      onChange={(e) => setSelectedStartDateTime(e.target.value)}
                    />
                  </div>

                  <div className="calendar-block-field">
                    <label htmlFor="block-end">End</label>
                    <input
                      id="block-end"
                      className="calendar-block-input"
                      type={isAllDay ? "date" : "datetime-local"}
                      value={isAllDay? selectedEndDateTime.slice(0, 10) : selectedEndDateTime}
                      min={isAllDay ? minDateTime2.slice(0, 10) : minDateTime2}
                      onKeyDown={(e) => e.preventDefault()}
                      onPaste={(e) => e.preventDefault()}
                      onChange={(e) => setSelectedEndDateTime(e.target.value)}
                    />
                  </div>
                </div>

                <div className="calendar-block-preview">
                  <div className="calendar-block-preview-label">Preview</div>
                  <div className="calendar-block-preview-title">
                    {title || "Untitled block"}
                  </div>
                  <div className="calendar-block-preview-time">{previewText}</div>
                </div>

                <div className="calendar-block-actions">
                  <Button className="btn btn-secondary">Reset</Button>
                  <Button 
                  className="btn btn-primary"
                  onClick={() => saveBlock({ title, selectedStartDateTime, selectedEndDateTime, staffId })}>
                    Save block
                  </Button>
                </div>
              </div>
            </Card>
          </div>

          <div className="calendar-block-side">
            <Card className="calendar-info-card">
              <div className="card-body calendar-info-card-body">
                <h3>Booking conflict status</h3>
                <p className="muted">
                  New time blocks should be checked against existing bookings before saving.
                </p>

                <div className="calendar-info-pill neutral">
                  No conflicts checked yet
                </div>
              </div>
            </Card>

            <Card className="calendar-info-card">
              <div className="card-body calendar-info-card-body">
                <h3>Your blocked time off</h3>
                <p className="muted">
                  You currently have no blocked time off.
                </p>
              </div>
            </Card>
          </div>
        </div>
      </div>
    </section>
  );
}