import { useContext } from 'react'
import ServiceCard from '../components/common/ServiceCard'
import StaffCard from '../components/common/StaffCard'
import { DataContext } from '../context/DataContext'

function Home() {
    const { services, staff, error, loadingServices, loadingStaff } = useContext(DataContext)

    return (
        <div className="home">
            {error && <div className='error-message'>{error}</div>}
            {loadingServices? (
                <div className='loading'>Loading services...</div>

            ) : (
                <div className='services-grid'>
                    <h1>Services</h1>
                    {services.map(service => (
                        <ServiceCard key={service.serviceId} service={service} />
                    ))}
                </div>
            )}
            <hr />
            {loadingStaff ? (
                <div className='loading'>Loading staff...</div>

            ) : (
                <div className='staff-grid'>
                    <h1>Staff</h1>
                    {staff.map(member => (
                        <StaffCard key={member.staffId} staff={member} />
                    ))}
                </div>
            )}
        </div>
    )
}

export default Home