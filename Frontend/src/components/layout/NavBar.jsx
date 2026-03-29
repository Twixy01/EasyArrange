import { Link, NavLink } from "react-router-dom"

function NavBar() {
    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">EasyArrange</Link>
            </div>
            <div className="navbar-links">
                <NavLink to="/staff">Staff Members </NavLink>
                <NavLink to="/services">Services </NavLink>
                <NavLink to="/about">About Us </NavLink>
                <NavLink to="/contact">Contact </NavLink>
                <NavLink to="/booking">Book Now </NavLink>
            </div>
        </nav>
    )
}

export default NavBar