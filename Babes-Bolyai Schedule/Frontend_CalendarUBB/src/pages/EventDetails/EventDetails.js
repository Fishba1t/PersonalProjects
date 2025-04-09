import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom'; // Importă useParams pentru a prelua id-ul din URL

function EventDetails() {
    const { id } = useParams(); // Preluăm id-ul evenimentului din URL
    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Fetch detalii eveniment din API folosind id-ul
        fetch(`http://localhost:8080/api/events/${id}`)
            .then(response => response.json())
            .then(data => {
                setEvent(data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Failed to fetch event details:', error);
                setLoading(false);
            });
    }, [id]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!event) {
        return <div>Event not found!</div>;
    }

    return (
        <div className="event-details">
            <h1 className="event-title">{event.title}</h1>
            <p><strong>Date:</strong> {new Date(event.date).toDateString()}</p>
            <p><strong>Interval:</strong> {event.interval}</p>
            <p><strong>Location:</strong> {event.location}</p>
            <p><strong>Faculty:</strong> {event.faculty}</p>
            <p><strong>Field:</strong> {event.field}</p>
            <p><strong>Description:</strong> {event.description}</p>
            {event.link && (
                <a href={event.link} target="_blank" rel="noopener noreferrer">
                    More Details
                </a>
            )}
        </div>
    );
}

export default EventDetails;
