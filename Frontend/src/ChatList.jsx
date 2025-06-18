import React, { useState, useEffect } from "react";

export default function ChatList() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);

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

  return (
    <div style={container}>
      {/* Sidebar */}
      <div style={sidebar}>
        <div style={sidebarHeader}>
          <span style={chatsLabel}>Chats</span>
        </div>
        <div style={userList}>
          {users.map((user) => (
            <div
              key={user.userId || user.id}
              style={userItem}
              onClick={() => setSelectedUser(user)}
            >
              <img
                src={user.profilePicUrl || "https://www.shutterstock.com/image-vector/user-profile-icon-vector-avatar-600nw-2247726673.jpg"}
                alt={user.username || user.name || "User"}
                style={sidebarAvatar}
              />
              <span style={userName}>{user.username || user.name || "User"}</span>
            </div>
          ))}
        </div>
      </div>
      {/* Main Content */}
      <div style={mainArea}>
        <div style={mainHeader}>
          <img
            src={
              selectedUser?.profilePicUrl ||
              "https://www.shutterstock.com/image-vector/user-profile-icon-vector-avatar-600nw-2247726673.jpg"
            }
            alt={selectedUser?.username || "User"}
            style={mainAvatar}
          />
          <span style={mainUserName}>
            {selectedUser?.username || "Select a user"}
          </span>
        </div>
        <div style={chatRowsContainer}>
          <div style={{ textAlign: "center", color: "#888", margin: "24px 0" }}>
            {selectedUser
              ? `Chat with ${selectedUser.username}`
              : "Select a chat to view messages."}
          </div>
        </div>
      </div>
    </div>
  );
}

// Styles
const container = {
  display: "flex",
  height: "100vh",
  width: "900px",
  background: "url('https://www.shutterstock.com/image-illustration/mobile-apps-pattern-260nw-362377472.jpg') center/cover no-repeat",
};
const sidebar = {
  width: 120,
  background: "linear-gradient(180deg, #f8bbd0 0%, #e57373 100%)",
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  paddingTop: 8,
  borderRight: "2px solid #e57373",
};
const sidebarHeader = {
  width: "100%",
  padding: "12px 0",
  background: "#fff",
  borderBottom: "2px solid #e57373",
  marginBottom: 12,
  textAlign: "center",
};
const chatsLabel = {
  color: "#e57373",
  fontWeight: 700,
  fontSize: 22,
};
const userList = {
  width: "100%",
};
const userItem = {
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  marginBottom: 16,
  cursor: "pointer",
};
const sidebarAvatar = {
  width: 48,
  height: 48,
  borderRadius: "50%",
  border: "2px solid #fff",
  marginBottom: 4,
};
const userName = {
  color: "#333",
  fontWeight: 600,
  fontSize: 15,
};
const mainArea = {
  flex: 1,
  padding: 0,
  display: "flex",
  flexDirection: "column",
  background: "rgba(255,255,255,0.3)",
};
const mainHeader = {
  display: "flex",
  alignItems: "center",
  height: 64,
  padding: "0 32px",
  background: "linear-gradient(90deg, #fff 60%, #64b5f6 100%)",
  borderBottom: "2px solid #e0e0e0",
};
const mainAvatar = {
  width: 48,
  height: 48,
  borderRadius: "50%",
  marginRight: 16,
  border: "2px solid #fff",
};
const mainUserName = {
  fontWeight: 700,
  fontSize: 20,
  color: "#222",
};
const chatRowsContainer = {
  flex: 1,
  display: "flex",
  flexDirection: "column",
  justifyContent: "flex-start",
  padding: "10px 0",
};
