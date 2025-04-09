package com.example.demo.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private String startTime; // Start time in HH:mm format

    @JsonFormat(pattern = "HH:mm")
    private String endTime; // End time in HH:mm format

    private List<String> imageUrls;

    private String location;

    private String description;

    private String faculty;

    private String field;  // Example: Biologie, Geografie

    private String link;  // Optional: Link to the original event

    private String type;  // New field of type String

    private List<String> organisers; // List of organiser names or IDs
}
