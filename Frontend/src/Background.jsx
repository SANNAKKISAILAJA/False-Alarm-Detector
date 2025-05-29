import React from "react";

export default function Background({ children }) {
  const bgStyle = {
    background: "url('https://securitytoday.com/-/media/SEC/Security-Products/Images/2022/01/false_alarms.jpg') center/cover no-repeat",
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
