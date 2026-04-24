import React, { useState, useContext, useEffect, useRef } from 'react'
import Card from '../components/common/Card'
import { useServices } from '../hooks/queries/useServices'
import { useAuth } from '../hooks/useAuth'
import { Link } from 'react-router-dom'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { createService, updateService, deleteService, createStaffService, deleteStaffService } from '../services/api'
import { salonApi } from '../api/salonApi'
import { UIStateContext } from '../context/UIStateContext'

function ManageServices() {
  const { showSuccess, showError, getErrorMessage } = useContext(UIStateContext)

   const { user } = useAuth()
   const isAdmin = !!(user && user.role && String(user.role.name).toUpperCase() === 'ADMIN')

   const { data: services = [], isLoading, error } = useServices()
   // show load error only once when the services hook reports an error
   useEffect(() => {
     if (error) {
       const message = getErrorMessage(error, "Failed to load services. Please try again later.")
       showError(message)
     }
   }, [error, getErrorMessage, showError])
   const queryClient = useQueryClient()

   const [editingId, setEditingId] = useState(null)
   const [editValues, setEditValues] = useState({})
   const [newService, setNewService] = useState({ name: '', description: '', duration: '', price: '', image: '' })
   const [fieldErrors, setFieldErrors] = useState(null)

   // staff-management state
   const [serviceStaffs, setServiceStaffs] = useState({}) // map serviceId -> [staff]
   const [allStaff, setAllStaff] = useState(null)
   const [recentlyAdded, setRecentlyAdded] = useState({}) // map serviceId -> [staffId]

   // ref for create section so the top button can scroll to it
   const createRef = useRef(null)
   const goToCreate = () => {
     createRef.current?.scrollIntoView({ behavior: 'smooth', block: 'start' })
     setTimeout(() => createRef.current?.querySelector('input')?.focus(), 300)
   }

   const createMutation = useMutation({
     mutationFn: (payload) => createService(payload),
     onSuccess: async () => {
       await queryClient.invalidateQueries({ queryKey: ['services'] })
       showSuccess("Service created successfully.")
       setEditValues({})
       setNewService({ name: '', description: '', duration: '', price: '', image: '' })
       setFieldErrors(null)
     },
     onError: (err) => {
       console.error('Create service error', err)
       const payload = err?.payload || err?.response?.data
       if (payload) {
         showError(getErrorMessage(err, "Failed to create service. Please check your input and try again."))
         setFieldErrors(payload.fieldErrors || null)
       } else {
         showError(getErrorMessage(err, "Failed to create service. Please check your input and try again."))
       }
     }
   })

   const updateMutation = useMutation({
     mutationFn: ({ serviceId, payload }) => updateService(serviceId, payload),
     onSuccess: async () => {
       await queryClient.invalidateQueries({ queryKey: ['services'] })
       showSuccess("Service updated successfully.")
       setEditingId(null)
       setEditValues({})
       setFieldErrors(null)
     },
     onError: (err) => {
       console.error('Update service error', err)
       const payload = err?.payload || err?.response?.data
       if (payload) {
         showError(getErrorMessage(err, "Failed to update service. Please check your input and try again."))
         setFieldErrors(payload.fieldErrors || null)
       } else {
         showError(getErrorMessage(err, "Failed to update service. Please check your input and try again."))
       }
     }
   })

   const deleteMutation = useMutation({
     mutationFn: (serviceId) => deleteService(serviceId),
     onSuccess: async () => {
       await queryClient.invalidateQueries({ queryKey: ['services'] })
       showSuccess("Service deleted successfully.")
     },
     onError: (err) => {
       showError(getErrorMessage(err, "Failed to delete service. Please try again."))
     }
   })

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

   const startEdit = (s) => {
     setEditingId(s.serviceId)
     setEditValues({
       name: s.name || '',
       description: s.description || '',
       duration: s.duration ?? s.durationMinutes ?? 30,
       price: s.price || 0,
       image: s.image || ''
     })
     setFieldErrors(null)

      ; (async () => {
         try {
           const assigned = await salonApi.getStaffByService(s.serviceId)
           setServiceStaffs((m) => ({ ...m, [s.serviceId]: assigned || [] }))
         } catch (err) {
           console.error('Failed to load staff for service', err)
           setServiceStaffs((m) => ({ ...m, [s.serviceId]: [] }))
         }

         if (!allStaff) {
           try {
             const fetchedAll = await salonApi.getStaff()
             setAllStaff(fetchedAll || [])
           } catch (err) {
             console.error('Failed to load all staff', err)
             setAllStaff([])
           }
         }
       })()
   }

   const cancelEdit = () => {
     setEditingId(null)
     setEditValues({})
     setFieldErrors(null)
   }

   const saveEdit = async (serviceId) => {
     const name = (editValues.name || '').trim()
     if (!name) {
       setFieldErrors({ ...(fieldErrors || {}), name: 'Name must not be blank' })
       return
     }

     const duration = Number(editValues.duration) || 0
     if (duration <= 0) {
       setFieldErrors({ ...(fieldErrors || {}), duration: 'Duration must be a positive number' })
       return
     }

     const price = Number(editValues.price) || 0
     const image = (editValues.image || '').trim()

     const payload = { name, description: editValues.description || '', duration: duration, price, image }

     setFieldErrors(null)

     try {
       if (editingId) {
         await updateMutation.mutateAsync({ serviceId, payload })
       } else {
         await createMutation.mutateAsync(payload)
       }
     } catch (err) {
       console.error('Failed to save service', err)
       const payloadErr = err?.payload || err?.response?.data
       if (payloadErr) {
         showError(getErrorMessage(err, "Failed to save service. Please check your input and try again."))
         setFieldErrors(payloadErr.fieldErrors || null)
       } else {
         showError(getErrorMessage(err, "Failed to save service. Please try again."))
       }
     }
   }

   const createNew = async () => {
     const name = (newService.name || '').trim()
     if (!name) {
       setFieldErrors({ ...(fieldErrors || {}), name: 'Name must not be blank' })
       return
     }
     const duration = Number(newService.duration) || 0
     if (duration <= 0) {
       setFieldErrors({ ...(fieldErrors || {}), duration: 'Duration must be a positive number' })
       return
     }
     const price = Number(newService.price) || 0
     const image = (newService.image || '').trim()
     const payload = { name, description: newService.description || '', duration, price }
     if (image) payload.image = image
     try {
       await createMutation.mutateAsync(payload)
       showSuccess("Service created successfully.")
     } catch (err) {
       showError(getErrorMessage(err, "Failed to create service. Please try again."))
     }
   }

   const handleDelete = async (serviceId) => {
     if (!window.confirm('Are you sure you want to delete this service?')) return
     try {
       await deleteMutation.mutateAsync(serviceId)
       showSuccess("Service deleted successfully.")
     } catch (err) {
       showError(getErrorMessage(err, "Failed to delete service. Please try again."))
     }
   }

   const handleAddStaff = async (serviceId, staffId) => {
     try {
       const staffIdNum = Number(staffId)
       const serviceIdNum = Number(serviceId)
       await createStaffService({ staffId: staffIdNum, serviceId: serviceIdNum })
       const assigned = await salonApi.getStaffByService(serviceIdNum)
       setServiceStaffs((m) => ({ ...m, [serviceIdNum]: assigned || [] }))
       await queryClient.invalidateQueries({ queryKey: ['services'] })
       setRecentlyAdded(prev => {
         const arr = prev[serviceIdNum] ? [...prev[serviceIdNum]] : []
         if (!arr.includes(staffIdNum)) arr.push(staffIdNum)
         return { ...prev, [serviceIdNum]: arr }
       })
       setTimeout(() => {
         setRecentlyAdded(prev => {
           const arr = (prev[serviceIdNum] || []).filter(id => id !== staffIdNum)
           return { ...prev, [serviceIdNum]: arr }
         })
       }, 10000)
     } catch (err) {
       showError(getErrorMessage(err, "Failed to add staff to service. Please try again."))
     }
   }

   const handleRemoveStaff = async (serviceId, staffId) => {
     if (!window.confirm('Remove this staff from the service?')) return
     try {
       const staffIdNum = Number(staffId)
       const serviceIdNum = Number(serviceId)
       await deleteStaffService(staffIdNum, serviceIdNum)
       const assigned = await salonApi.getStaffByService(serviceIdNum)
       setServiceStaffs((m) => ({ ...m, [serviceIdNum]: assigned || [] }))
       await queryClient.invalidateQueries({ queryKey: ['services'] })
       setRecentlyAdded(prev => {
         const arr = (prev[serviceIdNum] || []).filter(id => id !== staffIdNum)
         return { ...prev, [serviceIdNum]: arr }
       })
       showSuccess("Staff removed from service successfully.")
     } catch (err) {
       showError(getErrorMessage(err, "Failed to remove staff from service. Please try again."))
     }
   }

   return (
     <section className="section">
       <div className="container">
         <Card>
           <div className="card-body">
             <div className="section-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 12 }}>
               <div>
                 <h2>Manage Services</h2>
                 <p className="muted" style={{ marginTop: 6 }}>Below is a simple listing of services. You can create, edit and delete services here.</p>
               </div>
               <div>
                 <Link to="/admin/my-staff" className="btn" style={{ marginRight: 8 }}>MY STAFF</Link>
                 <button className="btn btn-primary" onClick={goToCreate} aria-label="Create new service" title="Create new service" style={{ padding: '0.6rem 1rem', display: 'inline-flex', alignItems: 'center', gap: 8 }}>
                   <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                     <path d="M12 5v14M5 12h14" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                   </svg>
                   CREATE NEW SERVICE
                 </button>
               </div>
             </div>

             {isLoading && <p className="muted">Loading services...</p>}

             {!isLoading && services && (
               <div className="service-list-form">
                 {services.map(s => {
                   const isEditing = editingId === s.serviceId
                   const displayDuration = s.duration ?? s.durationMinutes ?? 0
                   return (
                     <fieldset key={s.serviceId} className="service-card" style={{ marginBottom: 12, padding: 8, border: '1px solid #eee' }}>
                       <legend style={{ fontWeight: 600 }}>{s.name || 'Unnamed'}</legend>

                       <div className="service-grid" style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: 12, alignItems: 'start' }}>

                        <div>
                          <div style={{ marginBottom: 8 }}>
                            <label className="muted">Name</label>
                            <input
                              type="text"
                              value={isEditing ? editValues.name : (s.name || '')}
                              onChange={(e) => setEditValues(v => ({ ...v, name: e.target.value }))}
                              readOnly={!isEditing}
                            />
                            {isEditing && fieldErrors?.name && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.name}</div>}
                          </div>

                           <div>
                             <label className="muted">Description</label>
                             <textarea
                               value={isEditing ? editValues.description : (s.description || '')}
                               onChange={(e) => setEditValues(v => ({ ...v, description: e.target.value }))}
                               readOnly={!isEditing}
                             />
                           </div>
                         </div>

                         <div className="service-side" style={{ display: 'grid', gap: 8 }}>

                           <div>
                             <label className="muted">Price (numeric)</label>
                             <input
                               type="number"
                               value={isEditing ? editValues.price : (s.price || 0)}
                               onChange={(e) => setEditValues(v => ({ ...v, price: e.target.value }))}
                               readOnly={!isEditing}
                             />
                           </div>

                           <div>
                             <label className="muted">Image URL</label>
                             {isEditing ? (
                               <input type="text" value={editValues.image || ''} onChange={(e) => setEditValues(v => ({ ...v, image: e.target.value }))} />
                             ) : (
                               <input type="text" value={s.image || ''} readOnly />
                             )}
                             {(isEditing ? (editValues.image) : s.image) ? (
                               <div style={{ marginTop: 6 }}>
                                 <img src={(isEditing ? editValues.image : s.image) || ''} alt={s.name} className="service-thumb" style={{ maxWidth: 120, maxHeight: 80, objectFit: 'cover', borderRadius: 6 }} onError={(e) => { e.target.style.display = 'none' }} />
                               </div>
                             ) : null}
                           </div>

                          <div>
                            <label className="muted">Duration (minutes)</label>
                            <input
                              type="number"
                              value={isEditing ? editValues.duration : displayDuration}
                              onChange={(e) => setEditValues(v => ({ ...v, duration: e.target.value }))}
                              readOnly={!isEditing}
                            />
                            {isEditing && fieldErrors?.duration && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.duration}</div>}
                          </div>

                           <div>
                             <label className="muted">ID</label>
                             <input type="text" value={s.serviceId || ''} readOnly />
                           </div>

                         </div>

                       </div>

                       <div className="service-actions" style={{ gridColumn: '1 / -1', display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 8 }}>
                         {isEditing ? (
                           <>
                             <button className="btn-primary" onClick={() => saveEdit(s.serviceId)} disabled={updateMutation.isLoading}>Save</button>
                             <button className="btn-secondary" onClick={cancelEdit}>Cancel</button>
                           </>
                         ) : (
                           <>
                             <button className="btn" onClick={() => startEdit(s)}>Edit</button>
                             <button className="btn-danger" onClick={() => handleDelete(s.serviceId)} disabled={deleteMutation.isLoading}>Delete</button>
                           </>
                         )}
                       </div>

                       {isEditing && (
                         <div className="staff-section" style={{ gridColumn: '1 / -1', marginTop: 12, padding: 8, borderTop: '1px solid var(--border)' }}>
                           <h4 style={{ margin: 0 }}>Manage staff for this service</h4>
                           <div style={{ marginTop: 8 }}>
                             <div className="staff-list" style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                               {(serviceStaffs[s.serviceId] || []).length === 0 && <div className="muted">No staff assigned</div>}
                               {(serviceStaffs[s.serviceId] || []).map(st => {
                                 const isRecent = (recentlyAdded[s.serviceId] || []).includes(st.staffId)
                                 const chipClass = isRecent ? 'staff-chip recent' : 'staff-chip'
                                 return (
                                   <div key={st.staffId} className={chipClass}>
                                     <div className="staff-name">{st.user?.name || st.user?.username || `Staff #${st.staffId}`}</div>
                                     <button className="btn-danger" onClick={() => handleRemoveStaff(s.serviceId, st.staffId)}>Remove</button>
                                   </div>
                                 )
                               })}

                             </div>

                             <div style={{ marginTop: 10, display: 'flex', gap: 8, alignItems: 'center' }}>
                               <label className="muted">Add staff:</label>
                               <select className="staff-select" defaultValue="" onChange={(e) => { const staffId = e.target.value; if (staffId) handleAddStaff(s.serviceId, staffId); e.target.value = '' }}>
                                 <option value="" disabled>Select staff</option>
                                 {(allStaff || []).map(st => (
                                   <option key={st.staffId} value={st.staffId}>{st.user?.name || st.user?.username || `Staff #${st.staffId}`}</option>
                                 ))}
                               </select>
                             </div>

                           </div>
                         </div>
                       )}

                     </fieldset>
                   )
                 })}

                <fieldset style={{ marginTop: 16, padding: 8, border: '1px dashed #ddd' }}>
                  <legend style={{ fontWeight: 600 }}>Create new service</legend>
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8 }}>
                    <div>
                      <label className="muted">Name</label>
                      <input type="text" value={newService.name} onChange={(e) => setNewService(v => ({ ...v, name: e.target.value }))} />
                      {!editingId && fieldErrors?.name && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.name}</div>}
                    </div>

                    <div>
                      <label className="muted">Duration (minutes)</label>
                      <input type="number" value={newService.duration} onChange={(e) => setNewService(v => ({ ...v, duration: e.target.value }))} />
                      {!editingId && fieldErrors?.duration && <div className="form-error" style={{ marginTop: 6 }}>{fieldErrors.duration}</div>}
                    </div>

                     <div style={{ gridColumn: '1 / -1' }}>
                       <label className="muted">Description</label>
                       <textarea value={newService.description} onChange={(e) => setNewService(v => ({ ...v, description: e.target.value }))} />
                     </div>

                     <div>
                       <label className="muted">Price (numeric)</label>
                       <input type="number" value={newService.price} onChange={(e) => setNewService(v => ({ ...v, price: e.target.value }))} />
                     </div>

                     <div>
                       <label className="muted">Image URL</label>
                       <input type="text" value={newService.image} onChange={(e) => setNewService(v => ({ ...v, image: e.target.value }))} />
                     </div>

                     <div style={{ gridColumn: '1 / -1', display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                       <button className="btn-primary" onClick={() => createNew()} disabled={createMutation.isLoading}>Create</button>
                     </div>

                   </div>
                 </fieldset>

               </div>
             )}

             {!isLoading && (!services || services.length === 0) && (
               <p className="muted">No services found.</p>
             )}

           </div>
         </Card>
       </div>
     </section>
   )
}

export default ManageServices
