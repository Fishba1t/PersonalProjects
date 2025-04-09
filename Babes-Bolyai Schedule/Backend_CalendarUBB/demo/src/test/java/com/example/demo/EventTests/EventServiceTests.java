package com.example.demo.Service;

import com.example.demo.Domain.Event;
import com.example.demo.Domain.User;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Repository.EventRepository;
import com.example.demo.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EventServiceTests {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;
    private User user;

    @BeforeEach
    public void setup() {

        event = new Event();
        event.setId(1L);
        event.setTitle("Science Conference");
        event.setDescription("A conference about the latest science trends");
        event.setDate(LocalDate.of(2024, 12, 15));
        event.setStartTime(LocalTime.of(10, 0));
        event.setEndTime(LocalTime.of(15, 0));
        event.setLocation("Convention Center");
        event.setFaculty("Science");
        event.setField("Biology");
        event.setType("Conference");
        event.setLink("http://example.com/event");



        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setPhone("123-456-7890");


        User user1 = new User();
        user1.setId(2L);
        user1.setFirstName("Alice");
        user1.setLastName("Smith");
        user1.setEmail("alice.smith@example.com");
        user1.setPassword("securePassword1");
        user1.setPhone("987-654-3210");

        User user2 = new User();
        user2.setId(3L);
        user2.setFirstName("Bob");
        user2.setLastName("Johnson");
        user2.setEmail("bob.johnson@example.com");
        user2.setPassword("securePassword2");
        user2.setPhone("987-654-3220");

        User user3 = new User();
        user3.setId(4L);
        user3.setFirstName("Charlie");
        user3.setLastName("Brown");
        user3.setEmail("charlie.brown@example.com");
        user3.setPassword("securePassword3");
        user3.setPhone("987-654-3230");

        User user4 = new User();
        user4.setId(5L);
        user4.setFirstName("Diana");
        user4.setLastName("Prince");
        user4.setEmail("diana.prince@example.com");
        user4.setPassword("securePassword4");
        user4.setPhone("987-654-3240");

        // Creating individual Event objects
        event1 = new Event();
        event1.setId(2L);
        event1.setTitle("Tech Expo");
        event1.setDescription("Expo showcasing cutting edge technology");
        event1.setDate(LocalDate.of(2024, 10, 20));
        event1.setStartTime(LocalTime.of(10, 30));
        event1.setEndTime(LocalTime.of(18, 0));
        event1.setLocation("Tech Park");
        event1.setFaculty("Engineering");
        event1.setField("Technology");
        event1.setType("Exhibition");
        event1.setLink("http://example.com/techexpo");
        event1.setOrganisers(Arrays.asList(user1, user2));

        event2 = new Event();
        event2.setId(3L);
        event2.setTitle("Health Seminar");
        event2.setDescription("Seminar on Health and Wellness");
        event2.setDate(LocalDate.of(2024, 11, 15));
        event2.setStartTime(LocalTime.of(9, 0));
        event2.setEndTime(LocalTime.of(17, 0));
        event2.setLocation("Health Center");
        event2.setFaculty("Medical");
        event2.setField("Healthcare");
        event2.setType("Seminar");
        event2.setLink("http://example.com/healthseminar");
        event2.setOrganisers(Arrays.asList(user3));

        event3 = new Event();
        event3.setId(4L);
        event3.setTitle("Business Conference");
        event3.setDescription("Annual conference for business professionals");
        event3.setDate(LocalDate.of(2024, 9, 10));
        event3.setStartTime(LocalTime.of(9, 30));
        event3.setEndTime(LocalTime.of(16, 30));
        event3.setLocation("Convention Center");
        event3.setFaculty("Business Studies");
        event3.setField("Business");
        event3.setType("Conference");
        event3.setLink("http://example.com/businessconf");
        event3.setOrganisers(Arrays.asList(user4));

        event4 = new Event();
        event4.setId(5L);
        event4.setTitle("Art Festival");
        event4.setDescription("A celebration of local and international art");
        event4.setDate(LocalDate.of(2024, 8, 25));
        event4.setStartTime(LocalTime.of(12, 0));
        event4.setEndTime(LocalTime.of(22, 0));
        event4.setLocation("City Gallery");
        event4.setFaculty("Arts");
        event4.setField("Fine Arts");
        event4.setType("Festival");
        event4.setLink("http://example.com/artfestival");
        event4.setOrganisers(Arrays.asList(user1, user2, user3, user4));


        event.setOrganisers(Arrays.asList(user));
    }

    @Test
    public void testCreateEvent() throws ResourceNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);

        assertNotNull(createdEvent);
        assertEquals("Science Conference", createdEvent.getTitle());
        verify(eventRepository).save(event);
        verify(userRepository).findById(anyLong());
    }

    @Test
    public void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        List<Event> events = eventService.getAllEvents();

        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
    }

    @Test
    public void testGetEventById() throws ResourceNotFoundException {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.getEventById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Science Conference", result.getTitle());
    }

    @Test
    public void testUpdateEvent() throws ResourceNotFoundException {
        Event newDetails = new Event();
        newDetails.setTitle("Updated Science Conference");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(newDetails);

        Event updatedEvent = eventService.updateEvent(1L, newDetails);

        assertNotNull(updatedEvent);
        assertEquals("Updated Science Conference", updatedEvent.getTitle());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    public void testDeleteEvent() throws ResourceNotFoundException {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(event);

        eventService.deleteEvent(1L);

        verify(eventRepository).delete(event);
    }

    @Test
    public void testGetEventsByField() {
        List<Event> mockEvents = Arrays.asList(event1, event2, event3, event4);
        when(eventRepository.findByField("Technology")).thenReturn(mockEvents.stream().filter(e -> e.getField().equals("Technology")).collect(Collectors.toList()));

        List<Event> result = eventService.getEventsByField("Technology");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Tech Expo", result.get(0).getTitle());
    }

    @Test
    public void testGetEventsByMonthAndYear() {
        List<Event> mockEvents = Arrays.asList(event1, event2, event3, event4);
        when(eventRepository.findByMonthAndYear(10, 2024)).thenReturn(mockEvents.stream().filter(e -> e.getDate().getMonthValue() == 10 && e.getDate().getYear() == 2024).collect(Collectors.toList()));

        List<Event> result = eventService.getEventsByMonthAndYear(10, 2024);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Tech Expo", result.get(0).getTitle());
    }
    @Test
    public void testGetEventsByYear() {
        List<Event> mockEvents = Arrays.asList(event1, event2, event3, event4);
        when(eventRepository.findByYear(2024)).thenReturn(mockEvents.stream().filter(e -> e.getDate().getYear() == 2024).collect(Collectors.toList()));

        List<Event> result = eventService.getEventsByYear(2024);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }
    @Test
    public void testCountEventsByField() {
        List<Object[]> mockCounts = Arrays.asList(new Object[]{"Technology", 1L}, new Object[]{"Healthcare", 1L});
        when(eventRepository.countByField()).thenReturn(mockCounts);

        List<Object[]> result = eventService.countEventsByField();

        assertNotNull(result);
        assertEquals(2, result.size());  // Assuming counts for two fields
        assertEquals("Technology", result.get(0)[0]);
        assertEquals(1L, result.get(0)[1]);
    }

    @Test
    public void testCountEventsByMonthAndYear() {
        List<Object[]> mockCounts = Arrays.asList(new Object[]{2024, 10, 1L}, new Object[]{2024, 11, 1L});
        when(eventRepository.countByMonthAndYear()).thenReturn(mockCounts);

        List<Object[]> result = eventService.countEventsByMonthAndYear();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2024, result.get(0)[0]);
        assertEquals(10, result.get(0)[1]);
        assertEquals(1L, result.get(0)[2]);
    }
    @Test
    public void testGetEventsByType() {

        when(eventRepository.findByType("Conference")).thenReturn(Arrays.asList(event, event3));

        List<Event> result = eventService.getEventsByType("Conference");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(e -> e.getType().equals("Conference")));

        verify(eventRepository).findByType("Conference");
    }
    @Test
    public void testCountEventsByOrganiser() {
        // Arrange
        List<Object[]> mockCounts = Arrays.asList(new Object[]{"John Doe", 1L}, new Object[]{"Alice Smith", 2L});
        when(eventRepository.countByOrganiser()).thenReturn(mockCounts);

        // Act
        List<Object[]> counts = eventService.countEventsByOrganiser();

        // Assert
        assertNotNull(counts);
        assertEquals(2, counts.size());
        assertEquals("John Doe", counts.get(0)[0]);
        assertEquals(1L, counts.get(0)[1]);
        assertEquals("Alice Smith", counts.get(1)[0]);
        assertEquals(2L, counts.get(1)[1]);

        // Verify
        verify(eventRepository).countByOrganiser();
    }


}
