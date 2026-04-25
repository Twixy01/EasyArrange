import { Link, useLocation } from "react-router-dom";

export default function NotFoundPage() {
  const location = useLocation();

  return (
    <section className="section not-found-page">
      <div className="container">
        <div className="not-found-shell">
          <div className="not-found-code">404</div>

          <h1 className="not-found-title">Az oldal nem található</h1>

          <p className="not-found-description">
            A megadott URL nem létezik ebben az alkalmazásban, vagy az útvonal
            időközben megváltozott.
          </p>

          <div className="not-found-url-box" aria-live="polite">
            <span className="not-found-label">Kért útvonal</span>
            <code>{location.pathname}</code>
          </div>

          <div className="not-found-actions">
            <Link to="/" className="btn btn-primary">
              Vissza a főoldalra
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}
