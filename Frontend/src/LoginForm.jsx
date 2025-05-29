import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate

export default function LoginForm({ onDone }) {
  const [userid, setUserid] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState(""); // "admin" or "user"
  const navigate = useNavigate(); // Initialize navigate

  const handleDone = () => {
    if (!userid || !password || !role) {
      alert("Enter login credentials.");
      return;
    }
    if (role === "admin") {
      navigate("/tracker"); // Go to TrackerBoard for admin
    } else {
      onDone({ userid, password, role }); // Continue as before for user
    }
  };

  return (
    <form style={form} onSubmit={e => e.preventDefault()}>
      <h3 style={loginTitle}>Login</h3>
      <div style={roleBtnGroup}>
        <button
          type="button"
          style={role === "admin" ? roleBtnActive : roleBtn}
          onClick={() => setRole("admin")}
        >
          Admin
        </button>
        <button
          type="button"
          style={role === "user" ? roleBtnActive : roleBtn}
          onClick={() => setRole("user")}
        >
          User
        </button>
      </div>
      <input
        placeholder="Userid"
        style={input}
        value={userid}
        onChange={e => setUserid(e.target.value)}
      />
      <input
        placeholder="Password"
        type="password"
        style={input}
        value={password}
        onChange={e => setPassword(e.target.value)}
      />
      <button type="button" style={doneBtn} onClick={handleDone}>
        Done
      </button>
    </form>
  );
}

// Styles remain unchanged...
const form = {
  background: "rgba(255,255,255,0.6)",
  borderRadius: 16,
  padding: 40,
  minWidth: 340,
  display: "flex",
  flexDirection: "column",
  gap: 12,
};

const loginTitle = {
  marginBottom: 16,
  textAlign: "center",
};

const input = {
  padding: "10px",
  borderRadius: 6,
  border: "1px solid #ccc",
  marginBottom: 8,
};

const doneBtn = {
  marginTop: 16,
  padding: "10px 32px",
  border: "none",
  background: "#f48fb1",
  color: "#fff",
  borderRadius: 6,
  fontWeight: 600,
  cursor: "pointer",
};

const roleBtnGroup = {
  display: "flex",
  gap: 16,
  justifyContent: "center",
  marginBottom: 12,
};

const roleBtn = {
  padding: "8px 20px",
  border: "1px solid #f48fb1",
  background: "#fff",
  color: "#f48fb1",
  borderRadius: 6,
  fontWeight: 600,
  cursor: "pointer",
};

const roleBtnActive = {
  ...roleBtn,
  background: "#f48fb1",
  color: "#fff",
};
