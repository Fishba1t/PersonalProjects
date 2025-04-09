import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { FaCalendarAlt } from "react-icons/fa";
import "./MainPage.css";

function MainPage() {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [startDate, setStartDate] = useState(null);
  const [calendarOpen, setCalendarOpen] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    axios
        .get("http://localhost:8080/api/events/all")
        .then((response) => {
          setEvents(response.data);
          setFilteredEvents(response.data);
        })
        .catch((error) => console.error("Error fetching events:", error));
  }, []);

  const handleSearch = (event) => {
    const value = event.target.value.toLowerCase();
    setSearchTerm(value);

    const filtered = events.filter((e) => {
      const eventDate = new Date(e.date);
      return (
          e.name.toLowerCase().includes(value) &&
          (!startDate || eventDate >= startDate)
      );
    });
    setFilteredEvents(filtered);
  };

  const handleDateChange = (date) => {
    setStartDate(date);
    setCalendarOpen(false);

    const filtered = events.filter((e) => {
      const eventDate = new Date(e.date);
      return !date || eventDate >= date;
    });
    setFilteredEvents(filtered);
  };

  const toggleCalendar = () => {
    setCalendarOpen((prev) => !prev);
  };

  const handleLoginClick = () => {
    navigate("/login");
  };

  return (
      <div className="main-page-container">
        <header className="header">
          <div>
            <h1>Evenimentele UBB, toate într-un singur loc!</h1>
            <p>Nu rata nicio ocazie importantă. Vezi toate evenimentele universitare centralizate aici!</p>
          </div>
          <button className="login-button" onClick={handleLoginClick}>
            Intră în cont
          </button>
        </header>

        <div className="main-page">
          <h2>Fiecare facultate, fiecare eveniment, oricând ai nevoie!</h2>
          <div className="filter-bar">
            <input
                type="text"
                placeholder="Caută eveniment"
                value={searchTerm}
                onChange={handleSearch}
            />
            <div className="calendar-container">
              <button className="calendar-toggle" onClick={toggleCalendar}>
                <FaCalendarAlt className="calendar-icon" /> Selectează data
              </button>
              {calendarOpen && (
                  <div className="calendar-popup">
                    <Calendar onChange={handleDateChange} value={startDate} />
                  </div>
              )}
            </div>
            <select>
              <option>Selectează facultatea</option>
            </select>
            <select>
              <option>Selectează tipul de eveniment</option>
            </select>
          </div>

          <div className="event-container">
            <div className="date-sidebar">
              <h2>4 Decembrie</h2>
              <p>Miercuri</p>
            </div>
            <div className="event-list">
              {filteredEvents.map((event) => (
                  <div className="event-card" key={event.id}>
                    <h3>{event.name}</h3>
                    <p>
                      {event.faculty} | {event.startTime} | {event.location}
                    </p>
                    <p>{event.type}</p>
                    <p>Data: {event.date}</p>
                  </div>
              ))}
            </div>
          </div>
        </div>
      </div>
  );
}

export default MainPage;
