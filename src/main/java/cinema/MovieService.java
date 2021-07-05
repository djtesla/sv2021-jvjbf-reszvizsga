package cinema;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private List<Movie> movies = new ArrayList<>();

    private ModelMapper modelMapper;

    private AtomicLong idGenerator = new AtomicLong();


    public MovieService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public List<MovieDTO> getMovies(Optional<String> title) {
        return movies.stream()
                .filter(movie -> title.isEmpty() || movie.getTitle().equalsIgnoreCase(title.get()))
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                        .collect(Collectors.toList());
    }


    public MovieDTO getMovieById(long id) {
        Movie movie = findMovieById(id);
        return modelMapper.map(movie, MovieDTO.class);
    }


    private Movie findMovieById(long id) {
        return movies.stream().filter(movie -> movie.getId()==id)
                .findAny().orElseThrow(()-> new MovieNotFoundException("Movie not found by id"));
    }


    public MovieDTO createMovie(CreateMovieCommand command) {
        Movie movieToBeCreated = new Movie(idGenerator.incrementAndGet(), command.getTitle(),command.getDate(),command.getMaxSpaces(), command.getMaxSpaces());
        movies.add(movieToBeCreated);
        return modelMapper.map(movieToBeCreated, MovieDTO.class);
    }


    public MovieDTO updateMovieWithReservation(long id, CreateReservationCommand command) {
        Movie movieToBeUpdated = findMovieById(id);
        movieToBeUpdated.reserve(command.getReservedSpaces());
        return modelMapper.map(movieToBeUpdated, MovieDTO.class);
    }


    public MovieDTO updateMovieWithDate(long id, UpdateDateCommand command) {
        Movie movieToBeUpdated = findMovieById(id);
        movieToBeUpdated.setDate(command.getDate());
        return modelMapper.map(movieToBeUpdated, MovieDTO.class);
    }


    public void deleteAllMovies() {
        movies = new ArrayList<>();
        idGenerator = new AtomicLong();
    }

}
