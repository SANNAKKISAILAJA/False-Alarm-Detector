// ChatInvite.js
import React from "react";

const users = [
  { name: "Edwin Leo", id: "Edwin Albert" },
  { name: "Yoksen32", id: "Yokeshi" },
  { name: "Remo45", id: "Remo" },
];

export default function ChatInvite({ onGoToChat }) {
  return (
    <div style={container}>
      <h3 style={{ color: "#e57373", marginBottom: 16,textAlign: "center"}}>Lets Chat</h3>
      <div style={{ display: "flex", gap: 24, marginBottom: 16 }}>
        <span style={tabActive}>Invite</span>
        <span style={tab}>Invited</span>
      </div>
      <input placeholder="Search User by Username or Userid" style={input} />
      <div style={{ margin: "16px 0" }}>
        {users.map(user => (
          <div key={user.id} style={userRow}>
            <div style={avatar}></div>
            <div>
              <div style={{ fontWeight: 600 }}>{user.name}</div>
              <div style={{ fontSize: 13, color: "#888" }}>{user.id}</div>
            </div>
            <button style={inviteBtn}>INVITE</button>
          </div>
        ))}
      </div>
      <button style={goBtn} onClick={onGoToChat}>Go to Chat</button>
    </div>
  );
}

const container = {
  background: "rgba(255,255,255,0.6)",
  borderRadius: 16,
  padding: 40,
  minWidth: 400,
};

const tabActive = { color: "#e57373", fontWeight: "bold", cursor: "pointer" };
const tab = { color: "#888", cursor: "pointer",marginLeft: "auto"  };
const input = { padding: "10px", borderRadius: 6, border: "1px solid #ccc", width: "100%" };
const userRow = { display: "flex", alignItems: "center", gap: 16, margin: "12px 0" };
const avatar = { width: 40, height: 40, borderRadius: "50%", background: "#ccc" };
const inviteBtn = { marginLeft: "auto", padding: "6px 18px", background: "#b3e5fc", border: "none", borderRadius: 6, fontWeight: 600 };
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

