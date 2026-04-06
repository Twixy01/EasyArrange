export default function SectionHeader({ eyebrow, title, description, center }) {
  return (
    <div className={`section-header ${center ? "center" : ""}`}>
      {eyebrow && <p className="eyebrow">{eyebrow}</p>}
      <h2>{title}</h2>
      {description && <p className="section-description">{description}</p>}
    </div>
  );
}