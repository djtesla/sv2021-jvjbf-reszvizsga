package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cinema")
public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO> getMovies(@RequestParam Optional<String> title) {
        return movieService.getMovies(title);
    }

    @GetMapping("/{id}")
    public MovieDTO getMovieById(@PathVariable ("id") long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public MovieDTO createMovie(@RequestBody @Valid CreateMovieCommand command) {
        return movieService.createMovie(command);
    }

    @PostMapping("/{id}/reserve")
    public MovieDTO updateMovieWithReservation(@PathVariable ("id") long id, @RequestBody CreateReservationCommand command) {
        return movieService.updateMovieWithReservation(id, command);
    }

    @PutMapping("/{id}")
    public MovieDTO updateMovieWithDate(@PathVariable ("id") long id, @RequestBody UpdateDateCommand command) {
        return movieService.updateMovieWithDate(id, command);
    }

    @DeleteMapping
    public void deleteAllMovies() {
        movieService.deleteAllMovies();
    }


    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Problem> handleMovieNotFound(MovieNotFoundException mnfe) {
        Problem problem = Problem.builder()
                .withType(URI.create("cinema/not-found"))
                .withStatus(Status.NOT_FOUND)
                .withTitle("Not found")
                .withDetail(mnfe.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(problem);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Problem> handleReservationExceedsFreeSpaces(IllegalStateException ise) {
        Problem problem = Problem.builder()
                .withType(URI.create("cinema/bad-reservation"))
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Reservation fails")
                .withDetail(ise.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(problem);
    }

}
