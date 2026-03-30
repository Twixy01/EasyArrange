import { useEffect, useState } from 'react'
import ServiceCard from '../components/common/ServiceCard'
import StaffCard from '../components/common/StaffCard'
import { getServices } from '../services/api'
import { getStaff } from '../services/api'

function Home() {
    const [services, setServices] = useState([])
    const [staff, setStaff] = useState([])

    const [error, setError] = useState(null)
    const [loadingServices, setLoadingServices] = useState(true)
    const [loadingStaff, setLoadingStaff] = useState(true)

    useEffect(() => {
        const loadServices = async () => {
            try {
                const response = await getServices();
                setServices(response);
            } catch (error) {
                console.error(error);
                setError('Failed to load services');
            } finally {
                setLoadingServices(false);
            }
        };

        loadServices();
    }, [])

    useEffect(() => {
        const loadStaff = async () => {
            try {
                const response = await getStaff();
                setStaff(response);
            } catch (error) {
                console.error(error);
                setError('Failed to load staff');
            } finally {
                setLoadingStaff(false);
            }
        };

        loadStaff();
    }, [])

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