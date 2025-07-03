import React from "react";
import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import AuthLanding from "./AuthLanding";
import SignupForm from "./SignupForm";
import LoginForm from "./LoginForm";
import ChatInvite from "./ChatInvite";
import ChatList from "./ChatList";
import TrackerBoard from "./TrackerBoard";
import Background from "./Background";

// Wrapper to provide navigation to child components
function AppRoutes() {
  const navigate = useNavigate();

  return (
    <Background>
      <Routes>
        <Route
          path="/"
          element={
            <AuthLanding
              onSignup={() => navigate("/signup")}
              onSignin={() => navigate("/login")}
            />
          }
        />
        <Route
          path="/signup"
          element={
            <SignupForm
              onDone={() => navigate("/login")}
            />
          }
        />
        <Route
          path="/login"
          element={
            <LoginForm
              onDone={() => navigate("/invite")}
            />
          }
        />
        <Route
          path="/invite"
          element={
            <ChatInvite
              onGoToChat={() => navigate("/chat")}
            />
          }
        />
        <Route
          path="/chat"
          element={
            <ChatList
              onTracker={() => navigate("/tracker")}
            />
          }
        />
        <Route
          path="/tracker"
          element={<TrackerBoard />}
        />
      </Routes>
    </Background>
  );
}

function App() {
  return (
    <Router>
      <AppRoutes />
    </Router>
  );
}

export default App;
