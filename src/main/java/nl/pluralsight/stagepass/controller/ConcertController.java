package nl.pluralsight.stagepass.controller;

import jakarta.validation.Valid;
import nl.pluralsight.stagepass.model.Artist;
import nl.pluralsight.stagepass.model.Concert;
import nl.pluralsight.stagepass.service.BookingService;
import nl.pluralsight.stagepass.service.ConcertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    private final ConcertService concertService;
    private final BookingService bookingService;

    public ConcertController(ConcertService concertService, BookingService bookingService) {
        this.concertService = concertService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Concert>> getAllConcerts() {
        return ResponseEntity.ok(concertService.getAllConcerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concert> getConcertById(@PathVariable Long id) {
        return concertService.getConcertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // feature 2
    @GetMapping("artist/{artistId}")
    public ResponseEntity<List<Concert>> findByArtistId(@PathVariable Long artistId) {
        return ResponseEntity.ok(concertService.getConcertsByArtist(artistId));
    }

    // feature 3
    @GetMapping("/upcoming")
    public ResponseEntity<List<Concert>> UpcomingConcerts() {
        return ResponseEntity.ok(concertService.getUpcomingConcerts());
    }

    @PostMapping
    public ResponseEntity<Concert> createConcert(@RequestBody Concert concert) {
        Concert created = concertService.createConcert(concert);
        return ResponseEntity.status(201).body(created); // bug 1
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concert> updateConcert(@PathVariable Long id, @RequestBody Concert concert) {
        return concertService.updateConcert(id, concert)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long id) {
        if (concertService.deleteConcert(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
