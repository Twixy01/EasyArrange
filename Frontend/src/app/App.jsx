import '../App.css'
import Home from '../pages/Home'
import { Routes, Route } from 'react-router-dom'
import NavBar from '../components/layout/NavBar'

function App() {
  return (
    <div className="main-content">
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/services" element={<Home />} />
        <Route path="/bookingNow" element={<Home />} />
      </Routes>
    </div>
  )
}

export default App
