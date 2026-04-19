import '../App.css'
import { Routes, Route, useLocation } from 'react-router-dom'
import NavBar from '../components/layout/NavBar'
import Home from '../pages/Home'
import StaffPage from '../pages/StaffPage'
import ServicesPage from '../pages/ServicesPage'
import AboutPage from '../pages/AboutPage'
import ContactPage from '../pages/ContactPage'
import BookingPage from '../pages/BookingPage'
import LoginPage from '../pages/LoginPage'
import RegisterPage from '../pages/RegisterPage'
import ProfilePage from '../pages/ProfilePage'
import ProfileEditPage from '../pages/ProfileEditPage'
import ShiftsPage from '../pages/ShiftsPage'
import CalendarBlockPage from '../pages/CalendarBlockPage'
// import ManageStaffs from '../pages/ManageStaffs'
import ManageServices from '../pages/ManageServices'
import ManageUsers from '../pages/ManageUsers'
import { UIStateProvider } from "../context/UIStateContext.jsx";
import { useContext, useEffect } from 'react'
import { UIStateContext } from '../context/UIStateContext.jsx'
import Footer from '../components/layout/Footer.jsx'

function DataErrorBanner() {
    const { error, loadingServices, loadingStaff } = useContext(UIStateContext)
    if (!error && loadingServices === false && loadingStaff === false) return null
    return (
        <div style={{ background: '#fff3cd', color: '#856404', padding: '8px 12px', borderRadius: 4, margin: '12px' }}>
            {error ? <div>Data load error: {error}</div> : null}
            {loadingServices && <div>Loading services...</div>}
            {loadingStaff && <div>Loading staff...</div>}
        </div>
    )
}

function ScrollToTop() {
    const { pathname } = useLocation()

    useEffect(() => {
        window.scrollTo(0, 0)
    }, [pathname])

    return null
}

function App() {
    return (
        <div className="main-content">
            <ScrollToTop />
            <NavBar />
            <UIStateProvider>
                <DataErrorBanner />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/staff" element={<StaffPage />} />
                    <Route path="/services" element={<ServicesPage />} />
                    <Route path="/about" element={<AboutPage />} />
                    <Route path="/contact" element={<ContactPage />} />
                    <Route path="/booking" element={<BookingPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/profile/edit" element={<ProfileEditPage />} />
                    <Route path="/shifts" element={<ShiftsPage />} />
                    <Route path="/time-off" element={<CalendarBlockPage />} />
                    {/* <Route path="/admin/staffs" element={<ManageStaffs />} /> */}
                    <Route path="/admin/services" element={<ManageServices />} />
                    <Route path="/admin/users" element={<ManageUsers />} />
                </Routes>
            </UIStateProvider>
            <Footer />
        </div>
    )
}

export default App
