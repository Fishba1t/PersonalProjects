package com.example.demo.Controller;

import com.example.demo.Domain.Event;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping()
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.getEventById(id);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The event you want to access does not exist!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The event you want to access does not exist!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The event you want to access does not exist!");
        }
    }
    @GetMapping("/field/{field}")
    public ResponseEntity<List<Event>> getEventsByField(@PathVariable String field) {
        List<Event> events = eventService.getEventsByField(field);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/month/{month}/year/{year}")
    public ResponseEntity<List<Event>> getEventsByMonthAndYear(@PathVariable int month, @PathVariable int year) {
        List<Event> events = eventService.getEventsByMonthAndYear(month, year);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/year/{year}")
    public ResponseEntity<List<Event>> getEventsByYear(@PathVariable int year) {
        List<Event> events = eventService.getEventsByYear(year);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/count-by-field")
    public ResponseEntity<List<Object[]>> countEventsByField() {
        List<Object[]> counts = eventService.countEventsByField();
        return ResponseEntity.ok(counts);
    }
    @GetMapping("/count-by-year")
    public ResponseEntity<List<Object[]>> countEventsByYear() {
        List<Object[]> counts = eventService.countEventsByYear();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/count-by-month-and-year")
    public ResponseEntity<List<Object[]>> countEventsByMonthAndYear() {
        List<Object[]> counts = eventService.countEventsByMonthAndYear();
        return ResponseEntity.ok(counts);
    }
    // Endpoint to get count of events by type
//    @GetMapping("/count-by-type")
//    public ResponseEntity<List<Object[]>> countEventsByType() {
//        List<Object[]> counts = eventService.countEventsByType();
//        return ResponseEntity.ok(counts);
//    }
//
//    // Endpoint to get events by type
//    @GetMapping("/by-type/{type}")
//    public ResponseEntity<List<Event>> getEventsByType(@PathVariable String type) {
//        List<Event> events = eventService.getEventsByType(type);
//        return ResponseEntity.ok(events);
//    }
//
//    // Endpoint to get count of events by organizer
//    @GetMapping("/count-by-organizer")
//    public ResponseEntity<List<Object[]>> countEventsByOrganizer() {
//        List<Object[]> counts = eventService.countEventsByOrganiser();
//        return ResponseEntity.ok(counts);
//    }
//
//    // Endpoint to get events by organizer ID
//    @GetMapping("/by-organizer/{organizerId}")
//    public ResponseEntity<List<Event>> getEventsByOrganizer(@PathVariable Long organizerId) {
//        List<Event> events = eventService.getEventsByOrganiser(organizerId);
//        return ResponseEntity.ok(events);
//    }
}
