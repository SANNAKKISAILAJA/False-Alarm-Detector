import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function ChatInvite({ onGoToChat }) {
  const [activeTab, setActiveTab] = useState("invite");
  const [invitedUsers, setInvitedUsers] = useState([]);
  const [registeredUsers, setRegisteredUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [invitingId, setInvitingId] = useState(null);
  const navigate = useNavigate();

  const username = localStorage.getItem("userid");
  const password = localStorage.getItem("password");

  const getAuthHeader = () => {
    if (!username || !password) return {};
    const token = btoa(`${username}:${password}`);
    return { Authorization: `Basic ${token}` };
  };

  const fetchUsers = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch("http://localhost:8081/users", {
        headers: getAuthHeader(),
      });
      if (!res.ok) {
        if (res.status === 401) {
          navigate("/login");
          return;
        }
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      const data = await res.json();
      setRegisteredUsers(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchInvitedUsers = async () => {
    if (!username) return;
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`http://localhost:8081/users/invites/received/${username}`, {
        headers: getAuthHeader(),
      });
      if (!res.ok) {
        if (res.status === 401) {
          navigate("/login");
          return;
        }
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      const data = await res.json();
      setInvitedUsers(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const sendInvite = async (receiverUserId) => {
    setInvitingId(receiverUserId);
    try {
      const res = await fetch(`http://localhost:8081/users/invite/${receiverUserId}`, {
        method: "POST",
        headers: {
          ...getAuthHeader(),
          "Content-Type": "application/json",
        },
      });
      if (!res.ok) {
        const errorText = await res.text();
        if (res.status === 400 && errorText.includes("Invite already sent")) {
          alert("You have already sent an invite to this user.");
          return;
        }
        throw new Error(`HTTP error! status: ${res.status}, message: ${errorText}`);
      }
      alert("Invite sent successfully");
      fetchInvitedUsers();
    } catch (err) {
      alert("Failed to send invite: " + err.message);
    } finally {
      setInvitingId(null);
    }
  };

  const handleAcceptInvite = async (senderUserId) => {
    try {
      const res = await fetch(`http://localhost:8081/users/invite/accept/${senderUserId}`, {
        method: "POST",
        headers: getAuthHeader(),
      });
      if (!res.ok) throw new Error("Failed to accept invite");
      alert("Invite accepted!");
      fetchInvitedUsers();
    } catch (err) {
      alert("Error: " + err.message);
    }
  };

  const handleRejectInvite = async (senderUserId) => {
    try {
      const res = await fetch(`http://localhost:8081/users/invite/reject/${senderUserId}`, {
        method: "DELETE",
        headers: getAuthHeader(),
      });
      if (!res.ok) throw new Error("Failed to reject invite");
      alert("Invite rejected!");
      fetchInvitedUsers();
    } catch (err) {
      alert("Error: " + err.message);
    }
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab);
    tab === "invite" ? fetchUsers() : fetchInvitedUsers();
  };

  const filteredUsers = registeredUsers.filter(
    (user) =>
      user.userId !== username && (
        user.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.userId?.toLowerCase().includes(searchTerm.toLowerCase())
      )
  );

  const styles = {
    container: {
      background: "rgba(255,255,255,0.6)",
      borderRadius: 16,
      padding: 40,
      minWidth: 400,
    },
    tab: {
      color: "#888",
      cursor: "pointer",
      minWidth: 80,
      textAlign: "center",
    },
    tabActive: {
      color: "#e57373",
      fontWeight: "bold",
      cursor: "pointer",
      minWidth: 80,
      textAlign: "center",
    },
    input: {
      padding: "10px",
      borderRadius: 6,
      border: "1px solid #ccc",
      width: "100%",
      marginBottom: 16,
    },
    userRow: {
      display: "flex",
      alignItems: "center",
      gap: 16,
      margin: "12px 0",
    },
    avatar: {
      width: 40,
      height: 40,
      borderRadius: "50%",
      overflow: "hidden",
    },
    inviteBtn: {
      marginLeft: "auto",
      padding: "6px 18px",
      background: "#b3e5fc",
      border: "none",
      borderRadius: 6,
      fontWeight: 600,
      cursor: "pointer",
    },
    inviteBtnDisabled: {
      marginLeft: "auto",
      padding: "6px 18px",
      background: "#e0e0e0",
      border: "none",
      borderRadius: 6,
      fontWeight: 600,
      cursor: "not-allowed",
    },
    goBtn: {
      padding: "10px 100px",
      background: "#e57373",
      border: "none",
      borderRadius: 8,
      fontWeight: "bold",
      color: "#fff",
      cursor: "pointer",
    },
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div style={styles.container}>
      <h3 style={{ color: "#e57373", marginBottom: 16, textAlign: "center" }}>
        Let's Chat
      </h3>
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 16 }}>
        <div
          style={activeTab === "invite" ? styles.tabActive : styles.tab}
          onClick={() => handleTabClick("invite")}
        >
          Invite
        </div>
        <div
          style={activeTab === "invited" ? styles.tabActive : styles.tab}
          onClick={() => handleTabClick("invited")}
        >
          Invited
        </div>
      </div>

      <input
        placeholder="Search User by Username or User ID"
        style={styles.input}
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />

      <div style={{ marginTop: 16 }}>
        {loading ? (
          <div style={{ textAlign: "center", color: "#888" }}>Loading...</div>
        ) : error ? (
          <div style={{ textAlign: "center", color: "#e57373" }}>Error: {error}</div>
        ) : activeTab === "invite" ? (
          filteredUsers.length > 0 ? (
            filteredUsers.map((user) => (
              <div key={user.userId} style={styles.userRow}>
                <div style={styles.avatar}>
                  {user.profilePicUrl ? (
                    <img
                      src={`http://localhost:8081/images/${user.profilePicUrl}`}
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
                <button
                  style={
                    invitingId === user.userId
                      ? styles.inviteBtnDisabled
                      : styles.inviteBtn
                  }
                  onClick={() => sendInvite(user.userId)}
                  disabled={invitingId === user.userId}
                >
                  {invitingId === user.userId ? "Sending..." : "INVITE"}
                </button>
              </div>
            ))
          ) : (
            <div style={{ textAlign: "center", color: "#888" }}>No matching users found.</div>
          )
        ) : invitedUsers.length > 0 ? (
          invitedUsers.map((user) => (
            <div key={user.userId} style={styles.userRow}>
              <div style={styles.avatar}>
                {user.profilePicUrl ? (
                  <img
                    src={`http://localhost:8081/images/${user.profilePicUrl}`}
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
              <button
                onClick={() => handleAcceptInvite(user.id)}
                style={{ ...styles.inviteBtn, background: "#a5d6a7" }}
              >
                Accept
              </button>
              <button
                onClick={() => handleRejectInvite(user.id)}
                style={{ ...styles.inviteBtn, background: "#ef9a9a" }}
              >
                Reject
              </button>
            </div>
          ))
        ) : (
          <div style={{ textAlign: "center", color: "#888" }}>No invites received yet.</div>
        )}
      </div>

      <div style={{ textAlign: "center", marginTop: 20 }}>
        <button style={styles.goBtn} onClick={onGoToChat}>
          Go to Chat
        </button>
      </div>
    </div>
  );
}