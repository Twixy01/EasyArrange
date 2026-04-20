import "../App.css";
import { Routes, Route, useLocation } from "react-router-dom";
import NavBar from "../components/layout/NavBar";
import Home from "../pages/Home";
import StaffPage from "../pages/StaffPage";
import ServicesPage from "../pages/ServicesPage";
import AboutPage from "../pages/AboutPage";
import ContactPage from "../pages/ContactPage";
import BookingPage from "../pages/BookingPage";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import ProfilePage from "../pages/ProfilePage";
import ProfileEditPage from "../pages/ProfileEditPage";
import ShiftsPage from "../pages/ShiftsPage";
import CalendarBlockPage from "../pages/CalendarBlockPage";
import ManageServices from "../pages/ManageServices";
import ManageUsers from "../pages/ManageUsers";
import { UIStateProvider } from "../context/UIStateContext.jsx";
import { useEffect } from "react";
import Footer from "../components/layout/Footer.jsx";
import FloatingNotification from "../components/common/FloatingNotification.jsx";

function ScrollToTop() {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  return null;
}

function App() {
  return (
    <UIStateProvider>
      <div className="main-content">
        <ScrollToTop />
        <NavBar />
        <FloatingNotification />

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
          <Route path="/admin/services" element={<ManageServices />} />
          <Route path="/admin/users" element={<ManageUsers />} />
        </Routes>
        <Footer />
      </div>
    </UIStateProvider>
  );
}

export default App;