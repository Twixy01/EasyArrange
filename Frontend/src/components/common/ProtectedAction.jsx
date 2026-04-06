import { Link } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

export default function ProtectedAction({ children, fallbackText }) {
  const { isLoggedIn } = useAuth();

  if (isLoggedIn) return children;

  return (
    <div className="protected-box">
      <p>{fallbackText || "Please log in to continue."}</p>
      <Link to="/login" className="inline-link">
        Go to login
      </Link>
    </div>
  );
}