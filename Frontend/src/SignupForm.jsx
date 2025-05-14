// SignupForm.js
import React, { useState } from "react";

export default function SignupForm({ onDone }) {
  const [agree, setAgree] = useState(false);
  return (
    <form style={form}>
      <h3 style={{ marginBottom: 16 }}>Create an account</h3>
      <input placeholder="Username" style={input} />
      <input placeholder="Userid" style={input} />
      <input placeholder="Email" type="email" style={input} />
      <input placeholder="Password" type="password" style={input} />
      <input type="file" style={input} />
      <div style={{ display: "flex", alignItems: "center", margin: "16px 0" }}>
        <input type="checkbox" checked={agree} onChange={e => setAgree(e.target.checked)} />
        <label style={{ marginLeft: 8, fontSize: 14 }}>I agree to Terms and Conditions</label>
      </div>
      <button type="button" style={doneBtn} disabled={!agree} onClick={onDone}>Done</button>
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
