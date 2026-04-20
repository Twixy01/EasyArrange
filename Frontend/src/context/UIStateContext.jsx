/* eslint-disable react-refresh/only-export-components */
import { createContext, useCallback, useMemo, useRef, useState } from "react";

export const UIStateContext = createContext();

export function UIStateProvider({ children }) {
  const [notification, setNotification] = useState({
    isVisible: false,
    type: "",
    message: "",
  });

  const timeoutRef = useRef(null);

  const hideNotification = useCallback(() => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
      timeoutRef.current = null;
    }

    setNotification({
      isVisible: false,
      type: "",
      message: "",
    });
  }, []);

  const showNotification = useCallback((type, message, duration = 3000) => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }

    setNotification({
      isVisible: true,
      type,
      message,
    });

    if (type !== "loading") {
      timeoutRef.current = setTimeout(() => {
        setNotification({
          isVisible: false,
          type: "",
          message: "",
        });
        timeoutRef.current = null;
      }, duration);
    }
  }, []);

  const showSuccess = useCallback((message, duration) => {
    showNotification("success", message, duration);
  }, [showNotification]);

  const showError = useCallback((message, duration = 4000) => {
    showNotification("error", message, duration);
  }, [showNotification]);

  const showLoading = useCallback((message = "Loading...") => {
    showNotification("loading", message);
  }, [showNotification]);

  const value = useMemo(() => ({
    notification,
    showNotification,
    showSuccess,
    showError,
    showLoading,
    hideNotification,
  }), [
    notification,
    showNotification,
    showSuccess,
    showError,
    showLoading,
    hideNotification,
  ]);

  return (
    <UIStateContext.Provider value={value}>
      {children}
    </UIStateContext.Provider>
  );
}