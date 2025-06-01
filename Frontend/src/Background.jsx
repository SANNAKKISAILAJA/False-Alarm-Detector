import React from "react";
import { useLocation } from "react-router-dom";

export default function Background({ children }) {

  const location = useLocation();

  
  const isAuthLanding = location.pathname === "/";

  const bgStyle = {
    background: isAuthLanding
      ? "#f9f9f9"
      : "url('https://securitytoday.com/-/media/SEC/Security-Products/Images/2022/01/false_alarms.jpg') center/cover no-repeat", // Default background for other pages
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    minHeight: "100vh",
    position: "relative"
  };
  return (
      <div style={bgStyle}>
        {children}
      </div>
  );
}
