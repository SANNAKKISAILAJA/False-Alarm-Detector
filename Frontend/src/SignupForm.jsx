import React, { useState } from "react";

export default function SignupForm({ onDone }) {
  const [username, setUsername] = useState("");
  const [userid, setUserid] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [file, setFile] = useState(null);
  const [agree, setAgree] = useState(false);

  const handleDone = () => {
    if (!username || !userid || !email || !password || !file || !agree) {
      alert("Enter all required credentials.");
      return;
    }
    onDone({ username, userid, email, password, file });
  };

  return (
    <form style={form} onSubmit={e => e.preventDefault()}>
      <h3 style={{ marginBottom: 16, textAlign: "center" }}>Create an account</h3>
      <input
        placeholder="Username"
        style={input}
        value={username}
        onChange={e => setUsername(e.target.value)}
      />
      <input
        placeholder="UserID"
        style={input}
        value={userid}
        onChange={e => setUserid(e.target.value)}
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
        type="button"
        style={doneBtn}
        disabled={!agree}
        onClick={handleDone}
      >
        Done
      </button>
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
