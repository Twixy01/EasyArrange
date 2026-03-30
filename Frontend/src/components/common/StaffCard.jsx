function StaffCard({ staff }) {
    return (
        <div className="staff-card">
            <img
                src={staff.user.profilePicture}
                alt={staff.user.name}
            />
            <div className="card-body">
                <h3>{staff.user.name}</h3>
                <p className="muted">{staff.title}</p>
                <p>{staff.bio}</p>
                <div className="rating-row">
                    <span>★ 5.0</span>
                </div>
            </div>
        </div>
    )
}

export default StaffCard