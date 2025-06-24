import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";

// Example data
const users = [
  { name: "Joseph", avatar: "https://randomuser.me/api/portraits/men/1.jpg" },
  { name: "Abner", avatar: "https://randomuser.me/api/portraits/men/2.jpg" },
  { name: "Christie", avatar: "https://randomuser.me/api/portraits/women/1.jpg" },
];

export default function ChatList() {
  const [socket, setSocket] = useState(null);
  const [message, setMessage] = useState("");
  const [chatHistory, setChatHistory] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [isBlocked, setIsBlocked] = useState(false);
  const location = useLocation();
  const userId = location.state?.userId || "anonymous";
  const username = location.state?.username || "Anonymous";

  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8081/ws/alarm');

    ws.onopen = () => {
      console.log('WebSocket Connected');
      setSocket(ws);
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      
      if (data.type === 'alert') {
        setAlerts(prev => [...prev, data.message]);
      } else if (data.type === 'locationRequest') {
        // Handle location request
        navigator.geolocation.getCurrentPosition(
          position => {
            ws.send(JSON.stringify({
              type: 'location',
              latitude: position.coords.latitude,
              longitude: position.coords.longitude
            }));
          },
          error => console.error('Location access denied:', error)
        );
      }
    };

    ws.onclose = () => {
      console.log('WebSocket Disconnected');
      setSocket(null);
    };

    return () => {
      ws.close();
    };
  }, []);

  const sendMessage = (event) => {
    event.preventDefault();
    if (!socket || socket.readyState !== WebSocket.OPEN) return;

    if (message.trim()) {
      socket.send(JSON.stringify({
        userId: userId,
        username: username,
        message: message,
        location: "123.456,789.012" // For testing, we can get a real location later
      }));
      setMessage("");
    }
  };

const chatRows = [
  { avatar: "https://randomuser.me/api/portraits/men/3.jpg" },
  { avatar: "https://randomuser.me/api/portraits/men/3.jpg" },
  { avatar: "https://randomuser.me/api/portraits/men/3.jpg" },
  { avatar: "https://randomuser.me/api/portraits/men/3.jpg" },
  { avatar: "https://randomuser.me/api/portraits/men/3.jpg" },
];

export default function ChatList() {
  return (
    <div style={container}>
      {/* Sidebar */}
      <div style={sidebar}>
        <div style={sidebarHeader}>
          <span style={chatsLabel}>Chats</span>
        </div>
        <div style={userList}>
          {users.map((user) => (
            <div key={user.name} style={userItem}>
              <img src={user.avatar} alt={user.name} style={sidebarAvatar} />
              <span style={userName}>{user.name}</span>
            </div>
          ))}
        </div>
      </div>
      {/* Main Content */}
      <div style={mainArea}>
        <div style={mainHeader}>
          <img
            src="https://randomuser.me/api/portraits/men/3.jpg"
            alt="Amir Khan"
            style={mainAvatar}
          />
          <span style={mainUserName}>Amir Khan</span>
        </div>
        <div style={chatRowsContainer}>
          {chatRows.map((row, idx) => (
            <div
              key={idx}
              style={{
                ...chatRow,
                background: idx % 2 === 0 ? "rgba(255,255,255,0.5)" : "rgba(20,190,212,0.50)",
              }}
            >
              <img src={row.avatar} alt="avatar" style={chatRowAvatar} />
            </div>
          ))}
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
  background: "url('https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=1500&q=80') center/cover no-repeat",
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

const chatRow = {
  height: 64,
  display: "flex",
  alignItems: "center",
  paddingLeft: 32,
  marginBottom: 16,
  borderRadius: 16,
  marginRight: 32,
};

const chatRowAvatar = {
  width: 56,
  height: 56,
  borderRadius: "50%",
  border: "2px solid #fff",
};}

