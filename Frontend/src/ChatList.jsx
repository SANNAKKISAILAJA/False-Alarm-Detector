// ChatList.js
import React from "react";

const chats = [
  { name: "Amir Khan" },
  { name: "Joseph" },
  { name: "Abner" },
  { name: "Christie" },
];

export default function ChatList({ onTracker }) {
  return (
    <div style={container}>
      <h3 style={{ marginBottom: 16, textAlign:"center"}}>Chats</h3>
      <ul style={{ listStyle: "none", padding: 0 }}>
        {chats.map(chat => (
          <li key={chat.name} style={chatRow}>
            <div style={avatar}></div>
            <span style={{ fontWeight: 600 }}>{chat.name}</span>
          </li>
        ))}
      </ul>
      <button style={goBtn} onClick={onTracker}>Go to Tracker</button>
    </div>
  );
}

const container = {
  background: "rgba(255,255,255,0.6)",
  borderRadius: 16,
  padding: 40,
  minWidth: 340,
};

const chatRow = { display: "flex", alignItems: "center", gap: 16, margin: "12px 0" };
const avatar = { width: 40, height: 40, borderRadius: "50%", background: "#ccc" };
const goBtn = {
  marginTop: 16,
  padding: "12px 0",
  width: "100%",
  background: "#f48fb1",
  color: "#fff",
  border: "none",
  borderRadius: 8,
  fontWeight: 700,
  fontSize: 20,
  cursor: "pointer",
  boxShadow: "0 2px 8px rgba(244,143,177,0.15)",
  transition: "background 0.2s",
};
