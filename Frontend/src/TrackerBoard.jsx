import React, { useState, useEffect } from "react";

export default function TrackerBoard() {
  const [flaggedUsers, setFlaggedUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("/flagged-users")
      .then((res) => res.json())
      .then((data) => {
        setFlaggedUsers(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Failed to fetch flagged users:", err);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div style={container}>Loading flagged users...</div>;
  }

  return (
    <div style={container}>
      <h3 style={{ color: "#e57373", marginBottom: 16, textAlign: "center" }}>
        Tracked Users (Flagged Terms &gt; 3)
      </h3>
      <div style={box}>
        {flaggedUsers.length === 0 ? (
          <p style={{ textAlign: "center" }}>No users flagged for excessive term usage.</p>
        ) : (
          flaggedUsers.map((user, index) => (
            <div key={index} style={userBox}>
              <table style={infoTable}>
                <tbody>
                  <tr>
                    <td style={labelCell}>Username</td>
                    <td style={valueCell}>{user.username}</td>
                  </tr>
                  <tr>
                    <td style={labelCell}>User ID</td>
                    <td style={valueCell}>{user.userId}</td>
                  </tr>
                  <tr>
                    <td style={labelCell}>Location</td>
                    <td style={valueCell}>{user.location || "Unknown"}</td>
                  </tr>
                  <tr>
                    <td style={labelCell}>Flagged Terms</td>
                    <td style={valueCell}>
                      {user.flaggedTerms?.join(", ") || "None"}
                    </td>
                  </tr>
                  <tr>
                    <td style={labelCell}>Flagged Chats</td>
                    <td style={valueCell}>
                      {user.chats?.map((chat, idx) => (
                        <div key={idx}>{chat}</div>
                      )) || "None"}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          ))
        )}
      </div>
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

const box = {
  border: "2px solid #e57373",
  borderRadius: 8,
  padding: 24,
  display: "inline-block",
  background: "rgba(255,255,255,0.8)",
  width: "100%",
};

const infoTable = {
  borderCollapse: "collapse",
  width: "100%",
};

const labelCell = {
  color: "#333",
  padding: "8px 24px 8px 0",
  fontSize: 20,
  fontWeight: 400,
  textAlign: "left",
  verticalAlign: "top",
};

const valueCell = {
  color: "#222",
  padding: "8px 0",
  fontSize: 20,
  fontWeight: 400,
  textAlign: "left",
  verticalAlign: "top",
};

const userBox = {
  border: "1px solid #e57373",
  borderRadius: 8,
  padding: 16,
  marginBottom: 16,
  background: "rgba(255,255,255,0.9)",
};
