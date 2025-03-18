package com.example.demo.Repository;

import com.example.demo.Domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    //Filtrare dupa domeniu
    List<Event> findByField(String field);

    // Filtrare după lună și an
    @Query("SELECT e FROM Event e WHERE MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Event> findByMonthAndYear(int month, int year);

    // Filtrare după an
    @Query("SELECT e FROM Event e WHERE YEAR(e.date) = :year")
    List<Event> findByYear(int year);

    // Contorizarea evenimentelor după field
    @Query("SELECT e.field, COUNT(e) FROM Event e GROUP BY e.field")
    List<Object[]> countByField();
    // Contorizare după an
    @Query("SELECT YEAR(e.date) AS year, COUNT(e) AS count FROM Event e GROUP BY YEAR(e.date)")
    List<Object[]> countByYear();

    // Contorizare după lună și an
    @Query("SELECT YEAR(e.date) AS year, MONTH(e.date) AS month, COUNT(e) AS count " +
            "FROM Event e GROUP BY YEAR(e.date), MONTH(e.date)")
    List<Object[]> countByMonthAndYear();

    // Count events by type
    @Query("SELECT e.type, COUNT(e) FROM Event e GROUP BY e.type")
    List<Object[]> countByEventType();

    // Find events by type
    List<Event> findByType(String type);

    // Count events by organizer
    @Query("SELECT o.firstName, o.lastName, COUNT(e) FROM Event e JOIN e.organisers o GROUP BY o.id")
    List<Object[]> countByOrganiser();

    // Find events by organizer ID
    List<Event> findByOrganisers_Id(Long organiserId);
}
