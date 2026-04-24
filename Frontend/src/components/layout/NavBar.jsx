import { Link, NavLink, useNavigate } from "react-router-dom"
import { useEffect, useRef, useState } from "react";
import { useAuth } from '../../hooks/useAuth'

function NavBar() {
    const [open, setOpen] = useState(false);
    const { isLoggedIn, user, logout } = useAuth()

    const roleNameRaw = typeof user?.role === "string" ? user.role : user?.role?.name;
    const roleNameUpper = roleNameRaw ? String(roleNameRaw).toUpperCase() : null;
    const canManageStaffSchedule = roleNameUpper === 'STAFF' || roleNameUpper === 'ADMIN';
    const isAdmin = roleNameUpper === 'ADMIN';

    const staffMenuDesktopRef = useRef(null);
    const staffMenuMobileRef = useRef(null);
    const adminMenuDesktopRef = useRef(null);
    const adminMenuMobileRef = useRef(null);

    const closeMenu = () => setOpen(false);

    const closeRoleMenus = () => {
        [
            staffMenuDesktopRef.current,
            staffMenuMobileRef.current,
            adminMenuDesktopRef.current,
            adminMenuMobileRef.current,
        ].forEach((menu) => menu?.removeAttribute("open"));
    };

    const closeOtherRoleMenus = (activeMenu) => {
        if (activeMenu !== 'staff') {
            staffMenuDesktopRef.current?.removeAttribute("open");
            staffMenuMobileRef.current?.removeAttribute("open");
        }

        if (activeMenu !== 'admin') {
            adminMenuDesktopRef.current?.removeAttribute("open");
            adminMenuMobileRef.current?.removeAttribute("open");
        }
    };

    const handleRoleMenuToggle = (menuName) => (event) => {
        if (event.currentTarget.open) {
            closeOtherRoleMenus(menuName);
        }
    };

    const closeAllMenus = () => {
        closeMenu();
        closeRoleMenus();
    };

    const navigate = useNavigate()

    useEffect(() => {
        const handlePointerDown = (event) => {
            const target = event.target;
            const menus = [
                staffMenuDesktopRef.current,
                staffMenuMobileRef.current,
                adminMenuDesktopRef.current,
                adminMenuMobileRef.current,
            ].filter(Boolean);

            const clickInsideAnyMenu = menus.some((menu) => menu.contains(target));
            if (!clickInsideAnyMenu) {
                closeRoleMenus();
            }
        };

        const handleEscape = (event) => {
            if (event.key === "Escape") {
                closeRoleMenus();
            }
        };

        document.addEventListener("mousedown", handlePointerDown);
        document.addEventListener("touchstart", handlePointerDown);
        document.addEventListener("keydown", handleEscape);

        return () => {
            document.removeEventListener("mousedown", handlePointerDown);
            document.removeEventListener("touchstart", handlePointerDown);
            document.removeEventListener("keydown", handleEscape);
        };
    }, []);

    const handleLogout = (e) => {
        e.preventDefault()
        logout()
        closeAllMenus()
        navigate('/')
    }

    return (
        <header className="navbar">
            <div className="container navbar-inner">

                <Link to="/" className="brand" onClick={closeAllMenus}>
                    <span className="brand-mark">✦</span>
                    <span>EasyArrange</span>
                </Link>

                <nav className="desktop-nav">
                    <NavLink to="/staff" onClick={closeRoleMenus}> Staff Members </NavLink>
                    <NavLink to="/services" onClick={closeRoleMenus}> Services </NavLink>
                    <NavLink to="/about" onClick={closeRoleMenus}> About Us </NavLink>
                    <NavLink to="/contact" onClick={closeRoleMenus}> Contact </NavLink>
                    <NavLink to="/booking" onClick={closeRoleMenus}> Book Now </NavLink>

                    {canManageStaffSchedule ? (
                        <details className="admin-menu" ref={staffMenuDesktopRef} onToggle={handleRoleMenuToggle('staff')}>
                            <summary>Staff</summary>
                            <div className="admin-menu-list">
                                <NavLink to="/shifts" onClick={closeRoleMenus}>My Shifts</NavLink>
                                <NavLink to="/staff/bookings" onClick={closeRoleMenus}>My Bookings</NavLink>
                                <NavLink to="/time-off" onClick={closeRoleMenus}>Time Off</NavLink>
                            </div>
                        </details>
                    ) : null}

                    {isAdmin && (
                        <details className="admin-menu" ref={adminMenuDesktopRef} onToggle={handleRoleMenuToggle('admin')}>
                            <summary>Admin</summary>
                            <div className="admin-menu-list">
                                <NavLink to="/admin/users" onClick={closeRoleMenus}>Manage Users</NavLink>
                                <NavLink to="/admin/services" onClick={closeRoleMenus}>Manage Services</NavLink>
                            </div>
                        </details>
                    )}
                </nav>

                <div className="navbar-actions">
                    {isLoggedIn ? (
                        <>
                            <Link to="/profile" className="nav-user" onClick={closeAllMenus}>
                                {user?.name?.split(" ")[0]}
                            </Link>
                            <button className="ghost-link" onClick={handleLogout}>
                                Logout
                            </button>
                        </>
                    ) : (
                        <div className="auth-links desktop-auth">
                            <NavLink to="/login" onClick={closeAllMenus}>Login</NavLink>
                            <NavLink to="/register" className="accent-link" onClick={closeAllMenus}>
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
                    <NavLink to="/staff" onClick={closeAllMenus}>
                        Staff Members
                    </NavLink>
                    <NavLink to="/services" onClick={closeAllMenus}>
                        Services
                    </NavLink>
                    <NavLink to="/about" onClick={closeAllMenus}>
                        About Us
                    </NavLink>
                    <NavLink to="/contact" onClick={closeAllMenus}>
                        Contact
                    </NavLink>
                    <NavLink to="/booking" onClick={closeAllMenus}>
                        Book Now
                    </NavLink>

                    {canManageStaffSchedule ? (
                        <details className="admin-menu mobile-admin-menu" ref={staffMenuMobileRef} onToggle={handleRoleMenuToggle('staff')}>
                            <summary>Staff</summary>
                            <div className="admin-menu-list">
                                <NavLink to="/shifts" onClick={closeAllMenus}>My Shifts</NavLink>
                                <NavLink to="/staff/bookings" onClick={closeAllMenus}>My Bookings</NavLink>
                                <NavLink to="/time-off" onClick={closeAllMenus}>Time Off</NavLink>
                            </div>
                        </details>
                    ) : null}

                    {isAdmin && (
                        <details className="admin-menu mobile-admin-menu" ref={adminMenuMobileRef} onToggle={handleRoleMenuToggle('admin')}>
                            <summary>Admin</summary>
                            <div className="admin-menu-list">
                                <NavLink to="/admin/users" onClick={closeAllMenus}>Manage Users</NavLink>
                                <NavLink to="/admin/services" onClick={closeAllMenus}>Manage Services</NavLink>
                            </div>
                        </details>
                    )}
                    {!isLoggedIn ? (
                        <>
                            <NavLink to="/login" onClick={closeAllMenus}>
                                Login
                            </NavLink>
                            <NavLink to="/register" onClick={closeAllMenus}>
                                Register
                            </NavLink>
                        </>
                    ) : (
                        <>
                            <NavLink to="/profile" onClick={closeAllMenus}>
                                Profile
                            </NavLink>
                            <button
                                className="mobile-logout"
                                onClick={() => {
                                    logout();
                                    closeAllMenus();
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
