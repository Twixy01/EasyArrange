import { Link } from "react-router-dom"

function NavBar() {
  return (
    <nav className="navbar">
        <div className="navbar-brand">
            <Link to="/">EasyArrange</Link>
        </div>
        <div className="navbar-links">
            <Link to="/services">Services</Link>
            <Link to="/bookingNow">Book Now</Link>
        </div>
    </nav>
    )
}

export default NavBar