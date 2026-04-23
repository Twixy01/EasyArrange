import { Link } from "react-router-dom";
import SectionHeader from "../components/common/SectionHeader";
import Card from "../components/common/Card";
import { useStaff } from "../hooks/queries/useStaff";
import { resolveMediaUrl } from '../services/api'
import { useState, useMemo } from 'react'
// helper: format time-like strings to HH:mm (copied from MyStaff)
function formatTimeString(raw) {
  if (!raw) return ''
  const asDate = new Date(raw)
  if (!isNaN(asDate.getTime())) {
    const h = String(asDate.getHours()).padStart(2, '0')
    const m = String(asDate.getMinutes()).padStart(2, '0')
    return `${h}:${m}`
  }
  const m = /^(\d{1,2}):(\d{2})/.exec(String(raw).trim())
  if (m) return `${String(m[1]).padStart(2,'0')}:${m[2]}`
  return String(raw)
}

import { useShiftsByStaff } from '../hooks/queries/useShiftsByStaff'
import { useCalendarBlocksByStaff } from '../hooks/queries/useCalendarBlocksByStaff'

function StaffCard({ member }) {
  const [selectedDate, setSelectedDate] = useState(() => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  });

  const { data: shifts = [], isLoading: _shiftsLoading } = useShiftsByStaff(member.staffId);
  const { data: calendarBlocks = [], isLoading: _cbLoading } = useCalendarBlocksByStaff(member.staffId);

  const availability = useMemo(() => {
    // if no shift records at all, treat as Off
    if (!shifts || shifts.length === 0) return { label: 'Off', reason: 'No scheduled shift' };

    // determine day name e.g. MONDAY
    const day = new Date(selectedDate).toLocaleDateString('en-GB', { weekday: 'long' }).toUpperCase();

    // find shift for the day
    const shift = shifts.find(s => String(s.day).toUpperCase() === day);
    if (!shift || !shift.startShift || !shift.endShift || shift.startShift === '' || shift.endShift === '') {
      return { label: 'Off', reason: 'No working hours for this day' };
    }

    // check calendar blocks that overlap the selected date (any block whose date range touches the date)
    const startOfDay = new Date(selectedDate + 'T00:00:00');
    const endOfDay = new Date(selectedDate + 'T23:59:59');

    const hasBlock = (calendarBlocks || []).some(block => {
      const start = new Date(block.startDateTime);
      const end = new Date(block.endDateTime);
      return !(end < startOfDay || start > endOfDay);
    });

    if (hasBlock) return { label: 'Off', reason: 'Marked time off' };

    return { label: 'Available', reason: 'Has shift and not blocked' };
  }, [selectedDate, shifts, calendarBlocks]);

  const memberName = member.user?.name;
  const memberImage = resolveMediaUrl(member.user?.profilePicture) || member.user?.profilePicture;

  return (
    <Card className="staff-card">
      <img src={memberImage} alt={memberName} />
      <div className="card-body">
        <h3>{memberName}</h3>
        <p className="muted">{member.title}</p>
        <p>{member.bio}</p>

        <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginTop: 8 }}>
          <div style={{ flex: 1 }}>
            <div className="pill-wrap" style={{ margin: 0 }}>
              {member.services?.map((item) => (
                <span key={item.serviceId} className="pill">
                  {item.name}
                </span>
              ))}
            </div>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: 8, alignItems: 'flex-end' }}>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              className="calendar-block-input"
              style={{ maxWidth: 160 }}
            />

            <div style={{ textAlign: 'right' }}>
              <span className={`availability-pill ${availability.label === 'Available' ? 'available' : 'off'}`}>
                {availability.label}
              </span>
              <div className="muted" style={{ fontSize: 12, marginTop: 6 }}>
                {availability.label === 'Available' && (() => {
                  const day = new Date(selectedDate).toLocaleDateString('en-GB', { weekday: 'long' }).toUpperCase();
                  const shift = shifts.find(s => String(s.day).toUpperCase() === day);
                  if (shift && shift.startShift && shift.endShift) return `${formatTimeString(shift.startShift)} - ${formatTimeString(shift.endShift)}`;
                  return availability.reason || '';
                })()}
                {availability.label !== 'Available' && (() => {
                  // prefer to show overlapping calendar block title (reason) rather than raw times
                  const blocks = calendarBlocks || []
                  const overlapping = (blocks || []).find(b => {
                    try {
                      const st = new Date(b.startDateTime)
                      const ed = new Date(b.endDateTime)
                      const pad = (n) => String(n).padStart(2, '0')
                      const startDay = `${st.getFullYear()}-${pad(st.getMonth()+1)}-${pad(st.getDate())}`
                      const endDay = `${ed.getFullYear()}-${pad(ed.getMonth()+1)}-${pad(ed.getDate())}`
                      return selectedDate >= startDay && selectedDate <= endDay
                    } catch {
                      return false
                    }
                  })
                  if (overlapping) return overlapping.title || 'Time off'
                  return availability.reason || ''
                })()}
              </div>
            </div>
          </div>
        </div>

        <Link
          to={`/booking`}
          state={{ staffId: member.staffId }}
          className="btn btn-primary full-width"
          style={{ marginTop: 12 }}
        >
          Book with {memberName?.split(" ")[0] || "Staff"}
        </Link>
      </div>
    </Card>
  );
}

export default function StaffPage() {
  const {data: staff = [], isLoading: staffLoading, error: staffError} = useStaff();

  if (staffLoading) {
    return (
      <section className="section">
        <div className="container">
          <p>Loading staff...</p>
        </div>
      </section>
    );
  }

  if (staffError) {
    return (
      <section className="section">
        <div className="container">
          <p>{staffError?.message || String(staffError)}</p>
        </div>
      </section>
    );
  }

  return (
    <section className="section">
      <div className="container">
        <SectionHeader
          eyebrow="Staff"
          title="Professionals matched to the right services"
          description="Each staff member is assigned to supported services through the Staff_Service relationship."
        />

        <div className="grid cards-3">
          {staff.map((member) => (
            <StaffCard key={member.staffId} member={member} />
          ))}
        </div>
      </div>
    </section>
  );
}