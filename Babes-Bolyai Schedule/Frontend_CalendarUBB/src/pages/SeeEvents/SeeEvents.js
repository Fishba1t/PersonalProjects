import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'; // Importă Link pentru navigare
import './SeeEvents.css';

function SeeEvents() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [eventsPerPage] = useState(4);

    useEffect(() => {
        fetch('http://localhost:8080/api/events/all')
            .then(response => response.json())
            .then(data => {
                setEvents(data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Failed to fetch events:", error);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <div className="see-events-page">
                <h1 className="title">All Events</h1>
                <p>Loading events...</p>
            </div>
        );
    }

    const filteredEvents = events.filter(event =>
        event.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        event.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
        event.location.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const indexOfLastEvent = currentPage * eventsPerPage;
    const indexOfFirstEvent = indexOfLastEvent - eventsPerPage;
    const currentEvents = filteredEvents.slice(indexOfFirstEvent, indexOfLastEvent);

    const totalPages = Math.ceil(filteredEvents.length / eventsPerPage);

    const nextPage = () => {
        if (currentPage < totalPages) setCurrentPage(currentPage + 1);
    };

    const prevPage = () => {
        if (currentPage > 1) setCurrentPage(currentPage - 1);
    };

    const handleSearchChange = (e) => {
        setSearchQuery(e.target.value);
        setCurrentPage(1);
    };

    return (
        <div className="see-events-page">
            <h1 className="title">All Events</h1>

            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Search events..."
                    className="search-input"
                    value={searchQuery}
                    onChange={handleSearchChange}
                />
            </div>

            <div className="events-list">
                {currentEvents.length > 0 ? (
                    currentEvents.map((event, index) => (
                        <div key={index} className="event-item">
                            <h3>{event.title}</h3>
                            <p>Date: {new Date(event.date).toDateString()}</p>
                            <p>Interval: {event.interval}</p>
                            <p>Location: {event.location}</p>
                            <p>Faculty: {event.faculty}</p>
                            <p>Field: {event.field}</p>
                            <p>Description: {event.description}</p>
                            <Link to={`/event/${event.id}`} className="view-details-link">
                                View Details
                            </Link>
                        </div>
                    ))
                ) : (
                    <p>No events found</p>
                )}
            </div>

            <div className="pagination">
                <button onClick={prevPage} disabled={currentPage === 1}>
                    Previous
                </button>
                <span>{currentPage} of {totalPages}</span>
                <button onClick={nextPage} disabled={currentPage === totalPages}>
                    Next
                </button>
            </div>
        </div>
    );
}

export default SeeEvents;
