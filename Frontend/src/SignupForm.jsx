import React, { useState } from "react";
import { useNavigate } from "react-router-dom";


export default function SignupForm() {
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState(""); // Use userId for consistency
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [file, setFile] = useState(null);
  const [agree, setAgree] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const navigate = useNavigate();


  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMsg("");
    setErrorMsg("");

    if (!username || !userId || !email || !password || !file || !agree) {
      setErrorMsg("Please fill all fields and agree to the terms.");
      return;
    }

    // Create FormData object
    const formData = new FormData();
    formData.append("username", username);
    formData.append("userId", userId);
    formData.append("email", email);
    formData.append("password", password);
    formData.append("image", file);

    try {
      const res = await fetch('http://localhost:8081/users/user', {
        method: 'POST',
        body: formData,
      });

      if (res.ok) {
        setSuccessMsg("Account created successfully!");
        setErrorMsg("");
        // Optionally reset fields:
        setUsername("");
        setUserId("");
        setEmail("");
        setPassword("");
        setFile(null);
        setAgree(false);
      } else {
        const errorText = await res.text();
        setErrorMsg(errorText || "Account creation failed.");
        setSuccessMsg("");
      }
    } catch (error) {
      setErrorMsg("");
      setSuccessMsg("");
    }
  };

  return (
    <form style={form} onSubmit={handleSubmit}>
      <h3 style={{ marginBottom: 16, textAlign: "center" }}>Create an account</h3>
      {successMsg && <div style={{ color: "green", marginBottom: 10 }}>{successMsg}</div>}
      {errorMsg && <div style={{ color: "red", marginBottom: 10 }}>{errorMsg}</div>}
      <input
        placeholder="Username"
        style={input}
        value={username}
        onChange={e => setUsername(e.target.value)}
      />
      <input
        placeholder="UserID"
        style={input}
        value={userId}
        onChange={e => setUserId(e.target.value)}
      />
      <input
        placeholder="Email"
        type="email"
        style={input}
        value={email}
        onChange={e => setEmail(e.target.value)}
      />
      <input
        placeholder="Password"
        type="password"
        style={input}
        value={password}
        onChange={e => setPassword(e.target.value)}
      />
      <input
        type="file"
        style={input}
        onChange={e => setFile(e.target.files[0])}
      />
      <div style={{ display: "flex", alignItems: "center", margin: "16px 0" }}>
        <input
          type="checkbox"
          checked={agree}
          onChange={e => setAgree(e.target.checked)}
        />
        <label style={{ marginLeft: 8, fontSize: 14 }}>
          I agree to Terms and Conditions
        </label>
      </div>
      <button
        type="submit"
        style={doneBtn}
        disabled={!agree}
      >
        Done
      </button>
      <div> Have an Account? 
      <span onClick={() => navigate("/login")} style={{ color: "#e57373", cursor: "pointer" }}> signin </span>
      </div>
    </form>
  );
}

const form = {
  background: "rgba(255,255,255,0.6)",
  borderRadius: 16,
  padding: 40,
  minWidth: 340,
  display: "flex",
  flexDirection: "column",
  gap: 12,
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
