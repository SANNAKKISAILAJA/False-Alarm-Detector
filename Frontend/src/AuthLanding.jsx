import React, { useState } from "react";

// Navbar
function Navbar() {
  return (
    <header style={navbarStyle}>
      <div style={{ fontWeight: 750, fontSize:25, color: "#e57373" }}>
        False Alarm Detection
      </div>
    </header>
  );
}

// Footer
function Footer() {
  return (
    <footer style={footerStyle}>
      <div>
        Contact: <a href="mailto:contact@myapp.com" style={footerLink}>contact@myapp.com</a> |{" "}
        <a href="tel:+1234567890" style={footerLink}>+1 234 567 890</a>
      </div>
    </footer>
  );
}
function MissionVision() {
  const features = [
    {
      icon: "🚨",
      title: "Identify False Alarms",
      description: " The system evaluates warning signals and message patterns to determine the probability that an alert is a false alarm, reducing unnecessary notifications and minimizing alarm fatigue for users and moderators.",
    },
    {
      icon: "⚠️",
      title: "Provide Warning Messages",
      description: "When potentially harmful or misleading content is detected, users receive immediate warning messages, helping to prevent the spread of misinformation or accidental panic.",
    },
    {
      icon: "🚫",
      title: "Spot Misuse Candidates",
      description: "The platform continuously monitors for patterns of misuse, such as repeated triggering of false alarms or suspicious activity, and flags these users for further review or action by moderators.",
    },
  ];

  const [hoveredCard, setHoveredCard] = useState(null);

  const cardBaseStyle = {
    background: "rgba(255,255,255,0.85)",
    borderRadius: 10,
    padding: 24,
    margin: 10,
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    transition: "all 0.3s ease",
    flex: "1 1 200px", // Allows cards to grow and shrink, minimum 200px width
    minWidth: 200, // Minimum width for each card
  };

  const cardHoveredStyle = {
    ...cardBaseStyle,
    transform: "translateY(-5px)",
    boxShadow: "0 6px 16px rgba(0,0,0,0.2)",
    background: "rgba(255,255,255,1)",
  };

  const missionContainerStyle = {
    width: "100%",
    maxWidth: 1200,
    margin: "40px auto",
    textAlign: "center",
  };

  const missionCardsStyle = {
    display: "flex",
    flexWrap: "wrap", // Allows cards to wrap if screen is too narrow
    justifyContent: "center",
    marginTop: 24,
    gap: 16, // Adds space between cards (modern browsers)
  };

  return (
    <div style={missionContainerStyle}>
      <h2 style={{ fontSize: 32, color: "#e57373", marginBottom: 16 }}>Our Mission and Vision</h2>
      <div style={missionCardsStyle}>
        {features.map((feature, index) => (
          <div
            key={index}
            style={hoveredCard === index ? cardHoveredStyle : cardBaseStyle}
            onMouseEnter={() => setHoveredCard(index)}
            onMouseLeave={() => setHoveredCard(null)}
          >
            <span style={{ fontSize: 32, marginBottom: 8, display: "block" }}>{feature.icon}</span>
            <h3 style={{ fontSize: 20, marginBottom: 8 }}>{feature.title}</h3>
            <p style={{ fontSize: 16 }}>{feature.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}



// AuthLanding Box
function AuthLandingBox({ onSignup, onSignin }) {
  const [active, setActive] = useState("signup");

  return (
    <div style={authBoxStyle}>
      <p style={{ fontSize: 16, marginBottom: 16 }}>
        Connect and achieve more with us. Sign up or sign in to begin!
      </p>
      <div style={buttonGroup}>
        <button
          style={active === "signup" ? activeBtn : btn}
          onClick={() => {
            setActive("signup");
            onSignup();
          }}
        >
          SIGNUP
        </button>
        <button
          style={active === "signin" ? activeBtn : btn}
          onClick={() => {
            setActive("signin");
            onSignin();
          }}
        >
          SIGNIN
        </button>
      </div>
      <button style={googleBtn} onClick={() => window.location.href = "http://localhost:8081/oauth2/authorization/google"}>
    <img
      src="https://img.icons8.com/color/512/google-logo.png"
      alt="Google"
      style={{ width: 20, height: 20, marginRight: 8 }}
    />
    <span>Continue with Google</span>
    </button>
    </div>

  );
}

// Main Page
export default function AuthLanding({ onSignup, onSignin }) {
  return (
    <div style={bodyBackground}>
      <Navbar />
      <main style={mainContent}>
        <h1 style={headingStyle}>Welcome to this Page</h1>
        <p style={bodyText}>
        Your project is an intelligent chat monitoring system that detects false alarms and misuse in 
        online conversations using advanced algorithms. It analyzes chat content in real time to 
        distinguish between genuine alerts and suspicious messages, reducing unnecessary disruptions.
        When harmful or misleading activity is detected, the system automatically issues warning 
        notifications and flags repeat misusers for moderators. With a user-friendly interface and
        customizable alerts, the platform helps create a safer and more trustworthy online 
        communication environment.
        </p>
      </main>
      <MissionVision />
      <Footer />
      {/* Auth Box Floating at Top Right of the page */}
      <div style={authBoxFixedWrapper}>
        <AuthLandingBox onSignup={onSignup} onSignin={onSignin} />
      </div>
    </div>
  );
}

//
// === Styles ===
//

const bodyBackground = {
  minHeight: "100vh",
  width: "100%",
  margin: 0,
  padding: 0,
  display: "flex",
  flexDirection: "column",
  backgroundImage: "url('https://securitytoday.com/-/media/SEC/Security-Products/Images/2022/01/false_alarms.jpg')",
  backgroundRepeat: "no-repeat",
  backgroundSize: "cover",
  backgroundPosition: "center",
  backgroundAttachment: "fixed",
};


const navbarStyle = {
  width: "100%",
  padding: "20px 40px",
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  background:"rgba(135, 206, 235, 0.9)",
  boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
};

const mainContent = {
  flex: 1,
  width: "100%",
  padding: "60px 40px",
  textAlign: "center",
};

const headingStyle = {
  fontSize: 36,
  color: "#e57373",
  marginTop:"80px",
  marginBottom: 16,
};

const bodyText = {
  fontSize: "24px",
  width: "850px",
  marginLeft: "350px",
  marginTop:"40px",
};

const footerStyle = {
  width: "100%",
  padding: 10,
  textAlign: "center",
  background: "#fff",
  borderTop: "1px solid #ddd",
};

const footerLink = {
  color: "#e57373",
  textDecoration: "none",
};

const authBoxFixedWrapper = {
  position: "fixed",
  top: 80,
  right: 20,
  zIndex: 1000,
};

const authBoxStyle = {
  width: 300,
  padding: 24,
  background: "rgba(255,255,255,0.6)",
  borderRadius: 10,
  boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
  textAlign: "center",
};

const buttonGroup = {
  display: "flex",
  justifyContent: "space-between",
  marginBottom: 12,
};

const btn = {
  flex: 1,
  padding: "10px",
  margin: "0 4px",
  border: "none",
  borderRadius: 6,
  background: "#e0e0e0",
  cursor: "pointer",
  fontWeight: 600,
};

const activeBtn = {
  ...btn,
  background: "#bdbdbd",
};

const googleBtn = {
  marginTop: 10,
  padding: "10px 30px",
  border: "none",
  borderRadius: 6,
  background: "#fff",
  boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
  cursor: "pointer",
  fontWeight: 500,
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
};
