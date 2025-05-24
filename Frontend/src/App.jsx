import React, { useState } from "react";
import AuthLanding from "./AuthLanding";
import SignupForm from "./SignupForm";
import LoginForm from "./LoginForm";
import ChatInvite from "./ChatInvite";
import ChatList from "./ChatList";
import TrackerBoard from "./TrackerBoard";
import Background from "./Background";

function App() {
  const [screen, setScreen] = useState("landing");
  const [prevScreen, setPrevScreen] = useState(null);

  // Helper to change screens and remember the previous one
  const navigate = (nextScreen) => {
    setPrevScreen(screen);
    setScreen(nextScreen);
  };

  // Go back to the previous screen
  const goToPrevious = () => {
    if (prevScreen) {
      setScreen(prevScreen);
      setPrevScreen(null); // Optional: clear after going back
    }
  };

  return (
    <Background
      screen={screen}
      onGoPrevious={prevScreen ? goToPrevious : null}
    >
      {screen === "landing" && (
        <AuthLanding
          onSignup={() => navigate("signup")}
          onSignin={() => navigate("login")}
        />
      )}
      {screen === "signup" && (
        <SignupForm
          onDone={() => navigate("login")}
        />
      )}
      {screen === "login" && (
        <LoginForm
          onDone={() => navigate("invite")}
        />
      )}
      {screen === "invite" && (
        <ChatInvite
          onGoToChat={() => navigate("chat")}
        />
      )}
      {screen === "chat" && (
        <ChatList
          onTracker={() => navigate("tracker")}
        />
      )}
      {screen === "tracker" && (
        <TrackerBoard />
      )}
    </Background>
  );
}

export default App;
