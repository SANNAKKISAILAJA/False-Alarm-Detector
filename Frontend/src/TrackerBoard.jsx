import React from "react";

export default function TrackerBoard() {
  return (
    <div style={container}>
      <h3 style={{ color: "#e57373", marginBottom: 16, textAlign: "center" }}>
        Tracker Board
      </h3>
      <div style={box}>
        <table style={infoTable}>
          <tbody>
            <tr>
              <td style={labelCell}>Username</td>
              <td style={valueCell}>Amir Khan</td>
            </tr>
            <tr>
              <td style={labelCell}>UserId</td>
              <td style={valueCell}>Amir05</td>
            </tr>
            <tr>
              <td style={labelCell}>Location</td>
              <td style={valueCell}>His current Location</td>
            </tr>
            <tr>
              <td style={labelCell}>Flagged Term</td>
              <td style={{ ...valueCell}}>The term used</td>
            </tr>
            <tr>
              <td style={labelCell}>Chats</td>
              <td style={valueCell}>That particular chat</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}

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
};

const infoTable = {
  borderCollapse: "collapse",
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
