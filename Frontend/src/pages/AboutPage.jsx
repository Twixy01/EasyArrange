import SectionHeader from "../components/common/SectionHeader";

export default function AboutPage() {
  return (
    <section className="section">
      <div className="container narrow">
        <SectionHeader
          eyebrow="About Us"
          title="A modern salon experience built around craft"
          description="Veloura Salon blends premium care, elegant atmosphere, and streamlined digital booking."
        />
        <div className="content-card">
          <p>
            Our mission is to offer a premium, accessible, and highly polished
            grooming and beauty experience. From classic haircuts to beard styling,
            hair coloring, nail care, and specialist treatments, we combine
            service quality with thoughtful digital convenience.
          </p>
          <p>
            The booking experience is designed around real salon behavior:
            clients choose a service first, then the staff member who provides it,
            then select an available appointment slot.
          </p>
        </div>
      </div>
    </section>
  );
}