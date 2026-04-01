import { Link, NavLink } from "react-router-dom"
import {useState} from "react";
// import { useAuth } from "../../hooks/UseAuth.js";

function NavBar() {
    let {isLoggedIn} = useState(false)

    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">EasyArrange</Link>
            </div>
            <div className="navbar-links">
                <NavLink to="/staff"> Staff Members </NavLink>
                |
                <NavLink to="/services"> Services </NavLink>
                |
                <NavLink to="/about"> About Us </NavLink>
                |
                <NavLink to="/contact"> Contact </NavLink>
                |
                <NavLink to="/booking"> Book Now </NavLink>
                |

                { isLoggedIn ? (
                    <NavLink to="/profile">My profile</NavLink>
                    |
                    <NavLink to="/logout"> Logout</NavLink>
                ) : (
                     <NavLink to="/login"> Login</NavLink>
                    |
                    <NavLink to="/register"> Register</NavLink>
                    )
                }

            </div>
        </nav>
    )
}

export default NavBar