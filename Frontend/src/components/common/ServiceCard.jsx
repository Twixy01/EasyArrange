function ServiceCard({ service }) {
    return (
        <div className="card">
            <img src={service.image} alt={service.name} />
            <div className="card-body">
                <h3>{service.name}</h3>
                <p>{service.description}</p>
                <div className="service-meta">
                    <span>{service.price} HUF</span>
                    <span>{service.duration} min</span>
                </div>
            </div>
        </div>
    );
}

export default ServiceCard