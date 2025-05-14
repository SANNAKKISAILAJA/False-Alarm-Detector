import React, { useState } from "react";
import AuthLanding from "./AuthLanding";
import SignupForm from "./SignupForm";
import LoginForm from "./LoginForm";
import Background from "./Background";

function App() {
  const [screen, setScreen] = useState("landing");

  return (
    <Background>
      {screen === "landing" && (
        <AuthLanding onSignup={() => setScreen("signup")} onSignin={() => setScreen("login")} />
      )}
      {screen === "signup" && (
        <SignupForm onDone={() => setScreen("login")} />
      )}
      {screen === "login" && (
        <LoginForm onDone={() => setScreen(" ")} />
      )}
    </Background>
  );
}

export default App;
