import React from "react";

export default function Background({ children,onGoPrevious, screen }) {
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
        {onGoPrevious && (
          <button
            onClick={onGoPrevious}
            style={{
              position: 'absolute',
              top: 20,
              left: 20,
              marginTop: 16,
              padding: "10px 20px",
              border: "none",
              background: "#eee",
              fontWeight:200,
              borderRadius: 50 ,
              cursor: "pointer",
            }}
          >
            Back
          </button>
        )}
      </div>
  );
}
