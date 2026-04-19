import React from 'react'
import { Link } from 'react-router-dom'
import Card from '../components/common/Card'
import { useAuth } from '../hooks/useAuth'

function ManageServices() {
    const { user } = useAuth()
    const isAdmin = !!(user && user.role && String(user.role.name).toUpperCase() === 'ADMIN')

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

    return (
        <section className="section">
            <div className="container">
                <div className="page">
                    <Card>
                        <div className="card-body">
                            <h2>Manage Services</h2>
                            <p className="muted">(Admin only) This page will show service management tools — currently empty.</p>

                            <div className="placeholder-list" style={{marginTop: 12}}>
                                <em>No service entries yet.</em>
                            </div>
                        </div>
                    </Card>
                </div>
            </div>
        </section>
    )
}

export default ManageServices

