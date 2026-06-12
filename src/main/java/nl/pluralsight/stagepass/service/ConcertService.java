package nl.pluralsight.stagepass.service;

import nl.pluralsight.stagepass.model.Booking;
import nl.pluralsight.stagepass.model.Concert;
import nl.pluralsight.stagepass.model.ConcertSummary;
import nl.pluralsight.stagepass.repository.BookingRepository;
import nl.pluralsight.stagepass.repository.ConcertRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConcertService {

    private final BookingRepository bookingRepository;
    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository, BookingRepository bookingRepository) {

        this.concertRepository = concertRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Concert> getAllConcerts() {

        return concertRepository.findAll();
    }

    public Optional<Concert> getConcertById(Long id) {

        return concertRepository.findById(id);
    }

    // feature 2
    public List<Concert> getConcertsByArtist(Long artistId) {
        return concertRepository.findAll().stream()
                .filter(c -> c.getArtist().getId().equals(artistId))
                .collect(Collectors.toList());
    }

    // feature 3
    public List<Concert> getUpcomingConcerts() {
        return concertRepository.findByDateAfterOrderByDateAsc(LocalDate.now());
    }

    // feature 4
    public ConcertSummary getConcertSummary(long id) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found"));

        List<Booking> bookings = bookingRepository.findByConcertId(id);

        int seatsBooked = bookings.stream()
                .mapToInt(Booking::getNumberOfTickets)
                .sum();

        BigDecimal totalRevenue = bookings.stream()
                .map(Booking::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ConcertSummary(concert.getId(), concert.getTitle(), concert.getTotalSeats(), seatsBooked, concert.getAvailableSeats(), totalRevenue);
    }

    public Concert createConcert(Concert concert) {

        return concertRepository.save(concert);
    }

    public Optional<Concert> updateConcert(Long id, Concert updatedConcert) {
        return concertRepository.findById(id).map(existing -> {
            existing.setTitle(updatedConcert.getTitle());
            existing.setDate(updatedConcert.getDate());
            existing.setArtist(updatedConcert.getArtist());
            existing.setVenue(updatedConcert.getVenue());
            existing.setTotalSeats(updatedConcert.getTotalSeats());
            existing.setAvailableSeats(updatedConcert.getAvailableSeats());
            existing.setTicketPrice(updatedConcert.getTicketPrice());
            return concertRepository.save(existing);
        });
    }

    public boolean deleteConcert(Long id) {
        if (concertRepository.existsById(id)) {
            concertRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
