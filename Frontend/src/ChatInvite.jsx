import React, { useState, useEffect } from "react";

export default function ChatInvite({ onGoToChat, currentUser }) {
  const [activeTab, setActiveTab] = useState("invite"); // "invite" or "invited"
  const [invitedUsers, setInvitedUsers] = useState([]);
  const [socket, setSocket] = useState(null);
  const [registeredUsers, setRegisteredUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  // Fetch registered users from your backend
  useEffect(() => {
    fetch('/api/users')
      .then(res => {
        if (!res.ok) throw new Error('Network response was not ok');
        return res.json();
      })
      .then(data => setRegisteredUsers(data))
      .catch(err => console.error('Error fetching users:', err));
  }, []);

  // Connect to WebSocket server
  useEffect(() => {
    const ws = new WebSocket("ws://localhost:8081/chat-invite");
    setSocket(ws);

    ws.onopen = () => {
      console.log("Connected to WebSocket for chat invites");
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type === "invite") {
        setInvitedUsers(prev => [...prev, data.user]);
      }
      if (data.type === "locationRequest") {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            ws.send(JSON.stringify({
              type: "location",
              userId: data.userId,
              lat: position.coords.latitude,
              lng: position.coords.longitude
            }));
          },
          (error) => {
            console.error("Error getting location:", error);
          }
        );
      }
    };

    return () => {
      ws.close();
    };
  }, []);

  const handleInvite = (userId) => {
    if (socket && currentUser?.id) {
      socket.send(JSON.stringify({
        type: "invite",
        userId: userId,
        from: currentUser.id
      }));
    }
  };

  const handleGoToChat = () => {
    onGoToChat();
  };

  // Filter users based on search term
  const filteredUsers = registeredUsers.filter(user =>
    user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.id.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div style={container}>
      <h3 style={{ color: "#e57373", marginBottom: 16, textAlign: "center" }}>Let's Chat</h3>
      <div style={{ display: "flex", gap: 24, marginBottom: 16 }}>
        <span
          style={activeTab === "invite" ? tabActive : tab}
          onClick={() => setActiveTab("invite")}
        >
          Invite
        </span>
        <span
          style={activeTab === "invited" ? tabActive : tab}
          onClick={() => setActiveTab("invited")}
        >
          Invited
        </span>
      </div>
      <input
        placeholder="Search User by Username or Userid"
        style={input}
        value={searchTerm}
        onChange={e => setSearchTerm(e.target.value)}
      />
      <div style={{ margin: "16px 0" }}>
        {activeTab === "invite" ? (
          filteredUsers.length > 0 ? (
            filteredUsers.map(user => (
              <div key={user.id} style={userRow}>
                <div style={avatar}></div>
                <div>
                  <div style={{ fontWeight: 600 }}>{user.name}</div>
                  <div style={{ fontSize: 13, color: "#888" }}>{user.id}</div>
                </div>
                <button style={inviteBtn} onClick={() => handleInvite(user.id)}>
                  INVITE
                </button>
              </div>
            ))
          ) : (
            <div style={{ textAlign: "center", color: "#888", margin: "24px 0" }}>
              No matching users found.
            </div>
          )
        ) : (
          invitedUsers.length > 0 ? (
            invitedUsers.map(user => (
              <div key={user.id} style={userRow}>
                <div style={avatar}></div>
                <div>
                  <div style={{ fontWeight: 600 }}>{user.name}</div>
                  <div style={{ fontSize: 13, color: "#888" }}>{user.id}</div>
                </div>
              </div>
            ))
          ) : (
            <div style={{ textAlign: "center", color: "#888", margin: "24px 0" }}>
              No invited users yet.
            </div>
          )
        )}
      </div>
      <button style={goBtn} onClick={handleGoToChat}>Go to Chat</button>
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
const tab = { color: "#888", cursor: "pointer", marginLeft: "auto" };
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
  background: "#ccc",
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
