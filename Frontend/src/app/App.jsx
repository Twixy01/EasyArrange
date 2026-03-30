import '../App.css'
import { Routes, Route } from 'react-router-dom'
import NavBar from '../components/layout/NavBar'
import Home from '../pages/Home'
import StaffPage from '../pages/StaffPage'
import ServicesPage from '../pages/ServicesPage'
import AboutPage from '../pages/AboutPage'
import ContactPage from '../pages/ContactPage'
import BookingPage from '../pages/BookingPage'

function App() {
  return (
    <div className="main-content">
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/staff" element={<StaffPage />} />
        <Route path="/services" element={<ServicesPage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/contact" element={<ContactPage />} />
        <Route path="/booking" element={<BookingPage />} />
      </Routes>
    </div>
  )
}

export default App
