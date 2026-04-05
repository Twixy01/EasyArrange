import { Link, NavLink, useNavigate } from "react-router-dom"
import { useEffect } from "react";
import { useAuth } from '../../hooks/UseAuth'

function NavBar() {

    const { isLoggedIn, user, logout } = useAuth()
    const navigate = useNavigate()

    useEffect(() => {
        console.debug('[NavBar] auth changed - isLoggedIn:', isLoggedIn, 'user:', user)
    }, [isLoggedIn, user])

    const handleLogout = (e) => {
        e.preventDefault()
        logout()
        navigate('/')
    }

    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">EasyArrange</Link>
            </div>
            <div className="navbar-links">
                <NavLink to="/staff"> Staff Members </NavLink>
                <span className="sep">|</span>
                <NavLink to="/services"> Services </NavLink>
                <span className="sep">|</span>
                <NavLink to="/about"> About Us </NavLink>
                <span className="sep">|</span>
                <NavLink to="/contact"> Contact </NavLink>
                <span className="sep">|</span>
                <NavLink to="/booking"> Book Now </NavLink>
                <span className="sep">|</span>

                {isLoggedIn ? (
                    <>
                        <NavLink to="/profile"> My profile </NavLink>
                        <span className="sep">|</span>
                        <NavLink to="/" onClick={handleLogout}> Logout </NavLink>
                    </>
                ) : (
                    <>
                        <NavLink to="/login"> Login </NavLink>
                        <span className="sep">|</span>
                        <NavLink to="/register"> Register </NavLink>
                    </>
                )}

            </div>
        </nav>
    )
}

export default NavBar