import React from "react";

export default function LoginForm({ onDone }) {
  return (
    <form style={form}>
      <h3 style={{ marginBottom: 16 }}>Login</h3>
      <input placeholder="Userid" style={input} />
      <input placeholder="Password" type="password" style={input} />
      <button type="button" style={doneBtn} onClick={onDone}>Done</button>
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
