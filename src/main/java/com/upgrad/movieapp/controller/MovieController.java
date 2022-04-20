package com.upgrad.movieapp.controller;

import com.upgrad.movieapp.dto.MovieBookingDTO;
import com.upgrad.movieapp.dto.MovieDTO;
import com.upgrad.movieapp.entities.Movie;
import com.upgrad.movieapp.entities.Theatre;
import com.upgrad.movieapp.entities.User;
import com.upgrad.movieapp.services.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/movie_app/v1")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * Method for creating movies
     * 127.0.0.1:8080/movie_app/v1/movies
     */

    @PostMapping(value = "/movies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDTO> createMovie(MovieDTO movieDTO) {

        // Convert movieDTO to movie Entity

        Movie newMovie = modelMapper.map(movieDTO, Movie.class);
        Movie savedMovie = movieService.acceptMovieDetails(newMovie);

        MovieDTO savedMovieDTO = modelMapper.map(savedMovie, MovieDTO.class);
        return new ResponseEntity<>(savedMovieDTO, HttpStatus.CREATED);
    }

    /**
     *  Method to get all movies
     */

    @GetMapping(value = "/movies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDTO>> getAllMovies(){
        List<Movie> movieList = movieService.getAllMovies();
        List<MovieDTO> movieDTOList = new ArrayList<>();

        for(Movie movie : movieList) {
            movieDTOList.add(modelMapper.map(movie, MovieDTO.class));
        }

        return new ResponseEntity<>(movieDTOList, HttpStatus.OK);
    }

    /**
     *  Method to get Movie based on Id
     */

    @GetMapping(value = "/movies/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDTO> getMovieBasedOnId(@PathVariable(name = "id") int id) {
        Movie responseMovie = movieService.getMovieDetails(id);

        MovieDTO responseMovieDTO = modelMapper.map(responseMovie, MovieDTO.class);

        return new ResponseEntity<>(responseMovieDTO, HttpStatus.OK);
    }

    /**
     * Method to update movie
     */

    @PutMapping(value = "/movies/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable(name = "id") int id, @RequestBody MovieDTO movieDTO) {
        Movie newMovie = modelMapper.map(movieDTO, Movie.class);
        Movie updatedMovie = movieService.updateMovieDetails(id, newMovie);

        MovieDTO updatedMovieDTO = modelMapper.map(updatedMovie, MovieDTO.class);

        return new ResponseEntity<>(updatedMovieDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/bookings/movie", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieBookingDTO> bookMovieDetails(@RequestBody MovieBookingDTO movieBookingDTO) {

        Movie requestedMovie = modelMapper.map(movieBookingDTO.getMovie(), Movie.class);
        User fromUser = modelMapper.map(movieBookingDTO.getUser(), User.class);
        Theatre requestedTheatre = modelMapper.map(movieBookingDTO.getTheatre(), Theatre.class);

        boolean isValidBooking = movieService.bookMovie(fromUser, requestedMovie, requestedTheatre);

        if(!isValidBooking)
            return new ResponseEntity("Not Booked !!", HttpStatus.OK);

        return new ResponseEntity("Booked Successfully !!", HttpStatus.OK);
    }
}
