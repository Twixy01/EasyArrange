import { useMemo, useState, useContext } from "react";
import { useAuth } from "../hooks/useAuth";
import SectionHeader from "../components/common/SectionHeader";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import { useCreateCalendarBlock } from "../hooks/mutations/useCreateCalendarBlock";
import { useDeleteCalendarBlock } from "../hooks/mutations/useDeleteCalendarBlock";
import { useOverlappingBookings } from "../hooks/queries/useOverlappingBookings";
import { useCalendarBlocksByStaff } from "../hooks/queries/useCalendarBlocksByStaff";
import { UIStateContext } from "../context/UIStateContext.jsx";

export default function CalendarBlockPage() {
  const { showSuccess, showError, showLoading, hideNotification } = useContext(UIStateContext);

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
  const [deletingCalendarBlockId, setDeletingCalendarBlockId] = useState(null);
  const { mutate: createCalendarBlock, isError: createCalendarBlockError } = useCreateCalendarBlock();
  const { mutateAsync: deleteCalendarBlock, isError: deleteCalendarBlockError, isLoading: isDeletingCalendarBlock } = useDeleteCalendarBlock();
  const { data: overlappingBookings = [], isError: overlappingError, isLoading: overlappingLoading } = useOverlappingBookings(staffId, selectedStartDateTime, selectedEndDateTime)
  const { data: calendarBlocks = [], isError: calendarBlockError } = useCalendarBlocksByStaff(staffId)

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

  const handleOnChange = (value, setDateTime) => {
    setDateTime(value.includes('T') ? value : `${value}T00:00`)
  }

  const formatDisplayDate = (iso) => {
    if (!iso) return 'Unknown'
    const date = new Date(iso)
    if (Number.isNaN(date.getTime())) return 'Unknown'

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')

    return `${year}.${month}.${day}. ${hours}:${minutes}`
  }

  const saveDisabled = !staffId || overlappingLoading || overlappingError || overlappingBookings.length > 0

  const resetDateTime = () => {
    setSelectedStartDateTime(minDateTime1);
  }

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
          onSuccess("Calendar block created successfully!");
          setTitle("Time off");
          setIsAllDay(true);
          setSelectedStartDateTime(minDateTime1);
          setSelectedEndDateTime(minDateTime2);
        },
        onError: (error) => {
          showError(error.response?.data?.detail || error.response?.data?.message || "Failed to create calendar block. Please try again.");
        }
      }
    )
  };

  const handleDeleteCalendarBlock = async (calendarBlockId) => {
    if (!calendarBlockId) {
      alert('Calendar block id missing, cannot remove')
      return
    }

    const ok = window.confirm('Are you sure you want to remove this time off block?')
    if (!ok) return

    try {
      setDeletingCalendarBlockId(calendarBlockId)
      await deleteCalendarBlock(calendarBlockId)
    } catch (err) {
      console.error('Failed to delete calendar block', err?.response ?? err)
      const serverMessage = err?.payload?.detail || err?.payload?.message || err?.message || 'Failed to remove time off block'
      alert(serverMessage)
    } finally {
      setDeletingCalendarBlockId(null)
    }
  }

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
                      value={isAllDay ? selectedStartDateTime.slice(0, 10) : selectedStartDateTime}
                      min={isAllDay ? minDateTime1.slice(0, 10) : minDateTime1}
                      onKeyDown={(e) => e.preventDefault()}
                      onPaste={(e) => e.preventDefault()}
                      onChange={(e) => handleOnChange(e.target.value, setSelectedStartDateTime)}
                    />
                  </div>

                  <div className="calendar-block-field">
                    <label htmlFor="block-end">End</label>
                    <input
                      id="block-end"
                      className="calendar-block-input"
                      type={isAllDay ? "date" : "datetime-local"}
                      value={isAllDay ? selectedEndDateTime.slice(0, 10) : selectedEndDateTime}
                      min={isAllDay ? minDateTime2.slice(0, 10) : minDateTime2}
                      onKeyDown={(e) => e.preventDefault()}
                      onPaste={(e) => e.preventDefault()}
                      onChange={(e) => handleOnChange(e.target.value, setSelectedEndDateTime)}
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
                  <Button className="btn btn-secondary" onClick={resetDateTime}>Reset</Button>
                  <Button
                    disabled={saveDisabled}
                    className="btn btn-primary"
                    title={saveDisabled ? 'Resolve booking conflicts before saving this block' : undefined}
                    onClick={() => saveBlock({ title, selectedStartDateTime, selectedEndDateTime, staffId })}>
                    Save block
                  </Button>
                </div>

                {createCalendarBlockError && showError("Failed to create calendar block. Please try again.")}
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
                {overlappingLoading && showLoading("Checking for conflicts...")}

                {!overlappingLoading && overlappingError && showError("Failed to load booking conflicts.")}


                {
                  !overlappingLoading &&
                  !overlappingError &&
                  overlappingBookings.length === 0 &&
                  showError("No booking conflicts found for the selected time.")
                }

                {!overlappingLoading && !overlappingError && overlappingBookings.length > 0 && (
                  <div>
                    {overlappingBookings.map((b) => {
                      const bookingId = b.bookingId ?? b.id;

                      return (
                        <div key={bookingId} className="booking-item">
                          <div style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            gap: 12
                          }}>
                            <div>
                              <strong>{formatDisplayDate(b.startDateTime)}</strong>
                              <div className="muted">Service: {b.service?.name || '—'}</div>
                              <div className="muted">Customer: {b.customer?.name || '—'}</div>
                              <div className="muted">Customer phone: {b.customer?.phoneNumber || '—'}</div>
                            </div>
                            <div style={{
                              alignSelf: 'center',
                              display: 'flex',
                              flexDirection: 'column',
                              alignItems: 'flex-end',
                              gap: 8
                            }}>
                            </div>
                          </div>
                        </div>
                      )
                    })}
                  </div>
                )}

              </div>
            </Card>

            <Card className="calendar-info-card">
              <div className="card-body calendar-info-card-body">
                <h3>Your blocked time off</h3>
                {calendarBlockError && (
                  <div className="form-error">
                    Failed to load blocked time off.
                  </div>
                )}

                {!calendarBlockError && calendarBlocks.length > 0 ? (
                  <div>
                    {calendarBlocks.map((block) => {
                      return (
                        <div key={block.calendarBlockId} className="booking-item">
                          <div style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            gap: 12
                          }}>
                            <div>
                              <strong>{block.title}</strong>
                              <div className="muted">start: {formatDisplayDate(block.startDateTime)}</div>
                              <div className="muted">end: {formatDisplayDate(block.endDateTime)}</div>
                            </div>
                            <div style={{
                              alignSelf: 'center',
                              display: 'flex',
                              flexDirection: 'column',
                              alignItems: 'flex-end',
                              gap: 8
                            }}>
                              <Button
                                className="btn-secondary"
                                disabled={isDeletingCalendarBlock && deletingCalendarBlockId === block.calendarBlockId}
                                onClick={() => handleDeleteCalendarBlock(block.calendarBlockId)}
                              >
                                {isDeletingCalendarBlock && deletingCalendarBlockId === block.calendarBlockId ? 'Removing...' : 'Remove'}
                              </Button>
                            </div>
                          </div>
                        </div>
                      )
                    })}
                  </div>
                ) : (
                  <p className="muted">
                    You currently have no blocked time off.
                  </p>
                )}
              </div>
            </Card>
          </div>
        </div>
      </div >
    </section >
  );
}