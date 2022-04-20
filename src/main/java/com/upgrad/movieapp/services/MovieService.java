package com.upgrad.movieapp.services;

import com.upgrad.movieapp.entities.Movie;
import com.upgrad.movieapp.entities.Theatre;
import com.upgrad.movieapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

   Movie acceptMovieDetails(Movie movie);

    List<Movie> acceptMultipleMovieDetails(List<Movie> movie);

    Movie getMovieDetails(int id);

    Movie updateMovieDetails(int id, Movie movie);

    boolean deleteMovie(int id);

    List<Movie> getAllMovies();

    Page<Movie> getPaginatedMovieDetails(Pageable pageable);

    boolean bookMovie(User user, Movie movie, Theatre theatre);
}
