import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login/Login";
import MainPage from "./pages/MainPage/MainPage";
import AddEvent from "./pages/AddEvent/AddEvent";
import SeeEvents from "./pages/SeeEvents/SeeEvents";

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/login" element={<Login/>} />
          <Route path="/add-event" element={<AddEvent />} />
          <Route path="/see-events" element={<SeeEvents />} />
        </Routes>
      </Router>
  );
}

export default App;
