import { useEffect, useState } from 'react'
import ServiceCard from '../components/common/ServiceCard'

function Home() {
    const [services, setServices] = useState([])

    useEffect(() => {
        fetch('http://localhost:8080/api/services')
            .then(response => response.json())
            .then(data => setServices(data))
            .catch(error => console.error('Error fetching services:', error));
    }, [])

    return (
        <div className="home">
            {services.map(service => (
                <ServiceCard key={service.serviceId} service={service} />
            ))}
        </div>
    )
}

export default Home