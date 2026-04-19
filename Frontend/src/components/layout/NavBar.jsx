import { Link, NavLink, useNavigate } from "react-router-dom"
import { useState } from "react";
import { useAuth } from '../../hooks/useAuth'

function NavBar() {
    const [open, setOpen] = useState(false);
    const { isLoggedIn, user, logout } = useAuth()

    const roleNameRaw = typeof user?.role === "string" ? user.role : user?.role?.name;
    const roleNameUpper = roleNameRaw ? String(roleNameRaw).toUpperCase() : null;
    const canManageStaffSchedule = roleNameUpper === 'STAFF' || roleNameUpper === 'ADMIN';
    const isAdmin = roleNameUpper === 'ADMIN';

    const closeMenu = () => setOpen(false);

    const navigate = useNavigate()

    const handleLogout = (e) => {
        e.preventDefault()
        logout()
        navigate('/')
    }

    return (
        <header className="navbar">
            <div className="container navbar-inner">

                <Link to="/" className="brand" onClick={closeMenu}>
                    <span className="brand-mark">✦</span>
                    <span>EasyArrange</span>
                </Link>

                <nav className="desktop-nav">
                    <NavLink to="/staff"> Staff Members </NavLink>
                    <NavLink to="/services"> Services </NavLink>
                    <NavLink to="/about"> About Us </NavLink>
                    <NavLink to="/contact"> Contact </NavLink>
                    <NavLink to="/booking"> Book Now </NavLink>

                    {canManageStaffSchedule ? (
                        <>
                            <NavLink to="/shifts"> My Shifts </NavLink>
                            <NavLink to="/time-off"> Time Off </NavLink>
                        </>
                    ) : null}

                    {isAdmin && (
                        <div className="admin-links" style={{ marginLeft: 'auto', display: 'flex', gap: 12, alignItems: 'center' }}>
                            <NavLink to="/admin/users" style={{ color: 'gold', fontWeight: 600 }}>Manage Users</NavLink>
                            <NavLink to="/admin/services" style={{ color: 'gold', fontWeight: 600 }}>Manage Services</NavLink>
                        </div>
                    )}
                </nav>

                <div className="navbar-actions">
                    {isLoggedIn ? (
                        <>
                            <Link to="/profile" className="nav-user">
                                {user?.name?.split(" ")[0]}
                            </Link>
                            <button className="ghost-link" onClick={handleLogout}>
                                Logout
                            </button>
                        </>
                    ) : (
                        <div className="auth-links desktop-auth">
                            <NavLink to="/login">Login</NavLink>
                            <NavLink to="/register" className="accent-link">
                                Register
                            </NavLink>
                        </div>
                    )}

                    <button
                        className="mobile-menu-btn"
                        onClick={() => setOpen((prev) => !prev)}
                        aria-label="Toggle menu"
                    >
                        ☰
                    </button>
                </div>
            </div>

            {open && (
                <div className="mobile-drawer">
                    <NavLink to="/staff" onClick={closeMenu}>
                        Staff Members
                    </NavLink>
                    <NavLink to="/services" onClick={closeMenu}>
                        Services
                    </NavLink>
                    <NavLink to="/about" onClick={closeMenu}>
                        About Us
                    </NavLink>
                    <NavLink to="/contact" onClick={closeMenu}>
                        Contact
                    </NavLink>
                    <NavLink to="/booking" onClick={closeMenu}>
                        Book Now
                    </NavLink>

                    {isAdmin && (
                        <>
                            <NavLink to="/admin/users" onClick={closeMenu} style={{ color: 'gold', fontWeight: 600 }}> Manage Users </NavLink>
                            <NavLink to="/admin/services" onClick={closeMenu} style={{ color: 'gold', fontWeight: 600 }}> Manage Services </NavLink>
                        </>
                    )}

                    {canManageStaffSchedule ? (
                        <>
                            <NavLink to="/shifts" onClick={closeMenu}> My Shifts </NavLink>
                            <NavLink to="/time-off" onClick={closeMenu}> Time Off </NavLink>
                        </>
                    ) : null}

                    {!isLoggedIn ? (
                        <>
                            <NavLink to="/login" onClick={closeMenu}>
                                Login
                            </NavLink>
                            <NavLink to="/register" onClick={closeMenu}>
                                Register
                            </NavLink>
                        </>
                    ) : (
                        <>
                            <NavLink to="/profile" onClick={closeMenu}>
                                Profile
                            </NavLink>
                            <button
                                className="mobile-logout"
                                onClick={() => {
                                    logout();
                                    closeMenu();
                                }}
                            >
                                Logout
                            </button>
                        </>
                    )}
                </div>
            )}
        </header>
    )
}

export default NavBar