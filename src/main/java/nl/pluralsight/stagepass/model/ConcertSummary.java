package nl.pluralsight.stagepass.model;

import java.math.BigDecimal;

public record ConcertSummary (
    long concertId,
    String concertTitle,
    int totalSeats,
    int seatsBooked,
    int availableSeats,
    BigDecimal totalRevenue
) {}

