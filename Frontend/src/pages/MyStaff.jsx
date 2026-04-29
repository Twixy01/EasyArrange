import React, { useState, useEffect, useContext } from 'react'
import Card from '../components/common/Card'
import { staffApi } from '../api/staffApi'
import { shiftApi } from '../api/shiftApi'
import { calendarApi } from '../api/calendarApi'
import { useAuth } from '../hooks/useAuth'
import { UIStateContext } from '../context/UIStateContext'
import { Link } from 'react-router-dom'

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

export default function MyStaff() {
  const { user } = useAuth()
  const { showError, getErrorMessage } = useContext(UIStateContext)
  const isAdmin = !!(user && user.role && String(user.role.name).toUpperCase() === 'ADMIN')

  const [staff, setStaff] = useState([])
  const [date, setDate] = useState(() => new Date().toISOString().slice(0, 10))
  const [shifts, setShifts] = useState([])
  const [loading, setLoading] = useState(false)
  const [blocksByStaff, setBlocksByStaff] = useState({})

  useEffect(() => {
    if (!isAdmin) return
    let mounted = true
    ;(async () => {
      try {
        const s = await staffApi.getStaff()
        if (mounted) setStaff(s || [])
      } catch (err) {
        showError(getErrorMessage(err, 'Failed to load staff'))
      }
    })()
    return () => { mounted = false }
  }, [isAdmin, showError, getErrorMessage])

  useEffect(() => {
    if (!isAdmin) return
    let mounted = true
    setLoading(true)
    ;(async () => {
      try {
        const allShifts = await shiftApi.getStaffShifts()
        if (!mounted) return
        const flat = (allShifts || []).map(item => {
          const staffId = item.staff?.staffId ?? item.staffId ?? null
          const day = item.shift?.day ?? item.day ?? null
          const startShift = item.shift?.startShift ?? item.startShift ?? ''
          const endShift = item.shift?.endShift ?? item.endShift ?? ''
          const status = item.shift?.status ?? item.status ?? ''
          const note = item.shift?.note ?? item.note ?? ''
          return { staffId, day, startShift, endShift, status, note }
        })
        setShifts(flat)
      } catch (err) {
        showError(getErrorMessage(err, 'Failed to load staff schedule'))
        setShifts([])
      } finally {
        if (mounted) setLoading(false)
      }
    })()
    return () => { mounted = false }
  }, [isAdmin, showError, getErrorMessage])

  useEffect(() => {
    if (!isAdmin || staff.length === 0) return
    let mounted = true
    ;(async () => {
      try {
        const promises = staff.map(s =>
          calendarApi.getCalendarBlocksByStaff(s.staffId)
            .then(data => ({ id: s.staffId, data }))
            .catch(() => ({ id: s.staffId, data: [] }))
        )
        const results = await Promise.all(promises)
        if (!mounted) return
        const map = {}
        results.forEach(r => { map[r.id] = r.data || [] })
        setBlocksByStaff(map)
      } catch {
        if (mounted) setBlocksByStaff({})
      }
    })()
    return () => { mounted = false }
  }, [isAdmin, staff])

  if (!user) {
    return (
      <section className="section">
        <div className="container">
          <Card>
            <div className="card-body">
              <h2>Not signed in</h2>
              <p className="muted">Please <Link to="/login">log in</Link> to access this page.</p>
            </div>
          </Card>
        </div>
      </section>
    )
  }

  if (!isAdmin) {
    return (
      <section className="section">
        <div className="container">
          <Card>
            <div className="card-body">
              <h2>Access denied</h2>
              <p className="muted">You do not have permission to view this page.</p>
            </div>
          </Card>
        </div>
      </section>
    )
  }

  const selectedWeekday = (() => {
    try {
      const d = new Date(date + 'T00:00:00')
      return d.toLocaleDateString('en-GB', { weekday: 'long' }).toUpperCase()
    } catch {
      return null
    }
  })()

  return (
    <section className="section">
      <div className="container">
        <Card>
          <div className="card-body">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div>
                <h2>My Staff</h2>
                <p className="muted">Choose a date to view schedule and statuses for your staff.</p>
              </div>
            </div>

            <div style={{ marginTop: 12, display: 'flex', gap: 12, alignItems: 'center' }}>
              <label className="muted">Date</label>
              <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
            </div>

            <div style={{ marginTop: 16 }}>
              {loading && <div className="muted">Loading schedule...</div>}

              {!loading && staff.length === 0 && <div className="muted">No staff found.</div>}

              {!loading && staff.length > 0 && (
                <div className="my-staff-list" style={{ display: 'grid', gap: 8 }}>
                  {staff.map(s => {
                    const entry = (shifts || []).find(sh => Number(sh.staffId) === Number(s.staffId) && (sh.day ? String(sh.day).toUpperCase() === selectedWeekday : false)) || {}

                    let status = 'off'
                    if (entry) {
                      const raw = (entry.status || entry.state || (entry.onVacation ? 'vacation' : '')) || ''
                      if (String(raw).toLowerCase().includes('vacation') || entry.onVacation) status = 'vacation'
                      else if (entry.startShift && String(entry.startShift).trim() !== '') status = 'working'
                      else status = 'off'
                    }

                    const statusLabel = String(status).charAt(0).toUpperCase() + String(status).slice(1)
                    const pillClass = status === 'working' ? 'availability-pill available' : (status === 'vacation' ? 'availability-pill off' : 'availability-pill off')

                    const note = entry.note || entry.notes || ''

                    let reasonText = ''
                    if (status === 'working' && entry.startShift && entry.endShift) {
                      reasonText = `${formatTimeString(entry.startShift)} - ${formatTimeString(entry.endShift)}`
                    } else if (status === 'vacation') {
                      reasonText = 'On vacation'
                    } else if (entry && (entry.startShift || entry.endShift)) {
                      reasonText = `${formatTimeString(entry.startShift) || '-'} - ${formatTimeString(entry.endShift) || '-'}`
                    } else {
                      reasonText = 'No working hours'
                    }

                    return (
                      <div key={s.staffId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: 8, border: '1px solid var(--border)', borderRadius: 6, background: 'transparent' }}>
                        <div>
                          <div style={{ fontWeight: 600 }}>{s.user?.name || s.user?.username || `Staff #${s.staffId}`}</div>
                          <div className="muted" style={{ fontSize: 12 }}>{s.user?.email || ''}</div>
                          {note && <div style={{ marginTop: 6, fontSize: 12 }}>{note}</div>}
                        </div>
                        <div style={{ textAlign: 'right' }}>
                          <div style={{ display: 'inline-block' }}>
                            {/* if any calendar block overlaps the selected date, show that instead */}
                            {(() => {
                              const blocks = blocksByStaff[s.staffId] || []
                              const overlapping = (blocks || []).find(b => {
                                try {
                                  const st = new Date(b.startDateTime)
                                  const ed = new Date(b.endDateTime)
                                  const pad = (n) => String(n).padStart(2, '0')
                                  const startDay = `${st.getFullYear()}-${pad(st.getMonth()+1)}-${pad(st.getDate())}`
                                  const endDay = `${ed.getFullYear()}-${pad(ed.getMonth()+1)}-${pad(ed.getDate())}`
                                  return date >= startDay && date <= endDay
                                } catch {
                                  return false
                                }
                              })
                              if (overlapping) {
                                const blkLabel = overlapping.title || 'Time off'
                                return (
                                  <>
                                    <div className={'availability-pill off'}>{'Off'}</div>
                                    <div className="muted" style={{ fontSize: 12, marginTop: 6, textAlign: 'right' }}>{blkLabel}</div>
                                  </>
                                )
                              }

                              return (
                                <>
                                  <div className={pillClass}>{statusLabel}</div>
                                  <div className="muted" style={{ fontSize: 12, marginTop: 6, textAlign: 'right' }}>{reasonText}</div>
                                </>
                              )
                            })()}
                          </div>
                          <div style={{ marginTop: 6, fontSize: 12, color: 'var(--text-soft)' }}>ID: {s.staffId}</div>
                        </div>
                      </div>
                    )
                  })}
                </div>
              )}

            </div>

          </div>
        </Card>
      </div>
    </section>
  )
}
