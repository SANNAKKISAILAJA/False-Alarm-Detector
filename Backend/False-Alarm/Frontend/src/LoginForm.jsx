import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function LoginForm({ onDone }) {
  const [userid, setUserid] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState(""); // "admin" or "user"
  const navigate = useNavigate();

  const handleDone = async () => {
    if (!userid || !password || !role) {
      alert("Enter login credentials.");
      return;
    }
    try {
      const res = await fetch("http://localhost:8081/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId: userid, password, role }),
      });
      if (res.ok) {
        if (role === "admin") {
          navigate("/tracker");
        } else {
          onDone({ userid, password, role });
        }
      } else {
        const errorText = await res.text();
        alert("Login failed");
      }
    } catch (err) {
      alert("Network error");
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

//styles
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
