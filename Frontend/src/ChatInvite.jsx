import React, { useState, useEffect } from "react";

export default function ChatInvite({ onGoToChat }) {
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [activeTab, setActiveTab] = useState("invite"); // "invite" or "invited"

  // Fetch users from your backend
  useEffect(() => {
    fetch('/users')
      .then(res => {
        if (!res.ok) throw new Error('Network response was not ok');
        return res.json();
      })
      .then(data => setUsers(data))
      .catch(err => console.error('Error fetching users:', err));
  }, []);

  // Filter users based on search term
  const filteredUsers = users.filter(user =>
    (user.username && user.username.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (user.userId && user.userId.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  return (
    <div style={container}>
      <h3 style={{ color: "#e57373", marginBottom: 16, textAlign: "center" }}>Let's Chat</h3>
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 16 }}>
        <span style={activeTab === "invite" ? tabActive : tab}
          onClick={() => setActiveTab("invite")}>Invite</span>
        <span style={activeTab === "invited" ? tabActive : tab}
          onClick={() => setActiveTab("invited")}>Invited</span>
      </div>

      <input
        placeholder="Search User by Username or User ID"
        style={input}
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      <div style={{ margin: "16px 0" }}>
        {activeTab === "invite" ? (
          filteredUsers.length > 0 ? (
            filteredUsers.map(user => (
              <div key={user.userId} style={userRow}>
                <div style={avatar}>
                  {user.profilePicUrl ? (
                    <img
                      src={user.profilePicUrl}
                      alt={`${user.username}'s profile`}
                      style={{ width: "100%", height: "100%", objectFit: "cover" }}
                    />
                  ) : (
                    <div style={{ width: "100%", height: "100%", background: "#ccc" }} />
                  )}
                </div>
                <div>
                  <div style={{ fontWeight: 600 }}>{user.username}</div>
                  <div style={{ fontSize: 13, color: "#888" }}>{user.userId}</div>
                </div>
                <button style={inviteBtn}>INVITE</button>
              </div>
            ))
          ) : (
            <div style={{ textAlign: "center", color: "#888", margin: "24px 0" }}>
              No matching users found.
            </div>
          )
        ) : (
          <div style={{ textAlign: "center", color: "#888", margin: "24px 0" }}>
            No invited users yet.
          </div>
        )}
      </div>
      <button style={goBtn} onClick={onGoToChat}>Go to Chat</button>
    </div>
  );
}

// Styles
const container = {
  background: "rgba(255,255,255,0.6)",
  borderRadius: 16,
  padding: 40,
  minWidth: 400,
};
const tabActive = { color: "#e57373", fontWeight: "bold", cursor: "pointer" };
const tab = { color: "#888", cursor: "pointer" };
const input = {
  padding: "10px",
  borderRadius: 6,
  border: "1px solid #ccc",
  width: "100%",
  marginBottom: 16,
};
const userRow = {
  display: "flex",
  alignItems: "center",
  gap: 16,
  margin: "12px 0",
};
const avatar = {
  width: 40,
  height: 40,
  borderRadius: "50%",
  overflow: "hidden",
};
const inviteBtn = {
  marginLeft: "auto",
  padding: "6px 18px",
  background: "#b3e5fc",
  border: "none",
  borderRadius: 6,
  fontWeight: 600,
};
const goBtn = {
  marginTop: 16,
  padding: "10px 120px",
  background: "#f48fb1",
  color: "#fff",
  border: "none",
  borderRadius: 6,
  fontWeight: 600,
};
