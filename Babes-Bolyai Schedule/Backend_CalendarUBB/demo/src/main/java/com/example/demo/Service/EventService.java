package com.example.demo.Service;

import com.example.demo.Domain.Event;
import com.example.demo.Domain.ImageUrl;
import com.example.demo.Domain.User;
import com.example.demo.Exceptions.InvalidEventTimeException;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Repository.EventRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new event
    public Event createEvent(Event event) throws ResourceNotFoundException {
        try {
            System.out.println("Payload received: " + event);
            validateEventTimes(event);

            List<User> organisers = event.getOrganisers();
            if (organisers != null && !organisers.isEmpty()) {
                organisers.forEach(user -> userRepository.findById(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + user.getId())));
            }

            if (event.getImageUrls() != null) {
                event.getImageUrls().forEach(imageUrl -> imageUrl.setEvent(event));
            }

            return eventRepository.save(event);
        } catch (Exception e) {
            System.err.println("Error during event creation: " + e.getMessage());
            throw e;
        }
    }


    // Retrieve all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Retrieve event by ID
    public Event getEventById(Long id) throws ResourceNotFoundException {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }

    // Update an existing event
    public Event updateEvent(Long id, Event eventDetails) throws ResourceNotFoundException {
        Event event = getEventById(id);

        // Verificăm timpul de început și sfârșit
        validateEventTimes(eventDetails);

        // Update fields
        event.setDate(eventDetails.getDate());
        event.setField(eventDetails.getField());
        event.setFaculty(eventDetails.getFaculty());
        event.setLink(eventDetails.getLink());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setLocation(eventDetails.getLocation());
        event.setType(eventDetails.getType());

        // Update organisers
        if (eventDetails.getOrganisers() != null) {
            event.setOrganisers(eventDetails.getOrganisers());
        }

        // Handle image URLs
        if (eventDetails.getImageUrls() != null) {
            event.getImageUrls().clear();
            eventDetails.getImageUrls().forEach(imageUrl -> {
                imageUrl.setEvent(event);
                event.getImageUrls().add(imageUrl);
            });
        }

        return eventRepository.save(event);
    }

    // Delete an event
    public void deleteEvent(Long id) throws ResourceNotFoundException {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }

    // Retrieve events by field
    public List<Event> getEventsByField(String field) {
        return eventRepository.findByField(field);
    }

    // Retrieve events by month and year
    public List<Event> getEventsByMonthAndYear(int month, int year) {
        return eventRepository.findByMonthAndYear(month, year);
    }

    // Retrieve events by year
    public List<Event> getEventsByYear(int year) {
        return eventRepository.findByYear(year);
    }

    // Count events by field
    public List<Object[]> countEventsByField() {
        return eventRepository.countByField();
    }

    // Count events by year
    public List<Object[]> countEventsByYear() {
        return eventRepository.countByYear();
    }

    // Count events by month and year
    public List<Object[]> countEventsByMonthAndYear() {
        return eventRepository.countByMonthAndYear();
    }

    private void validateEventTimes(Event event) {
        if (event.getStartTime() != null && event.getEndTime() != null) {
            if (event.getEndTime().isBefore(event.getStartTime()) || event.getEndTime().equals(event.getStartTime())) {
                throw new InvalidEventTimeException("End time must be after start time");
            }
        }
    }

}
