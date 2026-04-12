import '../App.css'
import { Routes, Route } from 'react-router-dom'
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
import { UIStateProvider } from "../context/UIStateContext.jsx";
import { useContext } from 'react'
import { UIStateContext } from '../context/UIStateContext.jsx'

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

function App() {
    return (
        <div className="main-content">
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
                </Routes>
            </UIStateProvider>
        </div>
    )
}

export default App
