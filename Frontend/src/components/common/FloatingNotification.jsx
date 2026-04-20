import { useContext } from "react";
import { UIStateContext } from "../../context/UIStateContext";

export default function FloatingNotification() {
  const { notification, hideNotification } = useContext(UIStateContext);

  if (!notification.isVisible) return null;

  return (
    <div className={`floating-notification ${notification.type}`}>
      <div className="floating-notification-content">
        <div className="floating-notification-icon">
          {notification.type === "success" && "✓"}
          {notification.type === "error" && "!"}
          {notification.type === "loading" && <span className="loading-spinner" />}
        </div>

        <div className="floating-notification-text">
          {notification.message}
        </div>
      </div>

      {notification.type !== "loading" && (
        <button
          className="floating-notification-close"
          onClick={hideNotification}
          aria-label="Close notification"
          type="button"
        >
          ×
        </button>
      )}
    </div>
  );
}