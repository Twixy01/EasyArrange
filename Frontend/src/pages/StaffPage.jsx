import { Link } from "react-router-dom";
import SectionHeader from "../components/common/SectionHeader";
import Card from "../components/common/Card";
import { useStaff } from "../hooks/queries/useStaff";

export default function StaffPage() {
  const {data: staff = [], isLoading: staffLoading, error: staffError} = useStaff();

  if (staffLoading) {
    return (
      <section className="section">
        <div className="container">
          <p>Loading staff...</p>
        </div>
      </section>
    );
  }

  if (staffError) {
    return (
      <section className="section">
        <div className="container">
          <p>{error}</p>
        </div>
      </section>
    );
  }

  return (
    <section className="section">
      <div className="container">
        <SectionHeader
          eyebrow="Staff"
          title="Professionals matched to the right services"
          description="Each staff member is assigned to supported services through the Staff_Service relationship."
        />

        <div className="grid cards-3">
          {staff.map((member) => {
            const memberName = member.user?.name;
            const memberImage = member.user?.profilePicture;

            return (
              <Card key={member.staffId} className="staff-card">
                <img src={memberImage} alt={memberName} />
                <div className="card-body">
                  <h3>{memberName}</h3>
                  <p className="muted">{member.title}</p>
                  <p>{member.bio}</p>
                  <div className="pill-wrap">
                    {member.services.map((item) => (
                      <span key={item.serviceId} className="pill">
                        {item.name}
                      </span>
                    ))}
                  </div>
                  <Link
                    to={`/booking`}
                    state={{ staffId: member.staffId }}
                    className="btn btn-primary full-width"
                  >
                    Book with {memberName?.split(" ")[0] || "Staff"}
                  </Link>
                </div>
              </Card>
            );
          })}
        </div>
      </div>
    </section>
  );
}