import SectionHeader from "../components/common/SectionHeader";
import Button from "../components/common/Button";

export default function ContactPage() {
  return (
    <section className="section">
      <div className="container narrow">
        <SectionHeader
          eyebrow="Contact"
          title="Get in touch"
          description="Questions, special requests, or assistance with your booking? Reach out anytime."
        />

        <form className="form-card">
          <div className="form-grid">
            <div className="field">
              <label>Name</label>
              <input type="text" placeholder="Your name" />
            </div>
            <div className="field">
              <label>Email</label>
              <input type="email" placeholder="Your email" />
            </div>
          </div>

          <div className="field">
            <label>Message</label>
            <textarea rows="5" placeholder="How can we help?" />
          </div>

          <Button type="submit">Send message</Button>
        </form>
      </div>
    </section>
  );
}