package com.upgrad.movieapp.services;

import com.upgrad.movieapp.dao.MovieDAO;
import com.upgrad.movieapp.entities.Movie;
import com.upgrad.movieapp.entities.Theatre;
import com.upgrad.movieapp.entities.User;
import com.upgrad.movieapp.feign.TheatreServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class MovieServiceImpl implements MovieService{

    @Value("${userApp.url}")
    private String userAppUrl;

    @Value("${theatreApp.url}")
    private String theatreAppUrl;

    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TheatreServiceClient theatreServiceClient;

    @Override
    public Movie acceptMovieDetails(Movie movie) {
        return movieDAO.save(movie);
    }

    @Override
    public List<Movie> acceptMultipleMovieDetails(List<Movie> movies) {
        List<Movie> savedMovie = new ArrayList<>();
        for(Movie movie : movies) {
            savedMovie.add(acceptMovieDetails(movie));
        }
        return savedMovie;
    }

    @Override
    public Movie getMovieDetails(int id) {
        return movieDAO.findById(id).get();
    }

    @Override
    public Movie updateMovieDetails(int id, Movie movie) {
        Movie savedMovie = getMovieDetails(id);
        savedMovie.setMovieId(movie.getMovieId());
        savedMovie.setMovieName(movie.getMovieName());
        savedMovie.setMovieDescription(movie.getMovieDescription());
        savedMovie.setDuration(movie.getDuration());
        savedMovie.setTrailerUrl(movie.getTrailerUrl());
        savedMovie.setCoverPhotoUrl(movie.getCoverPhotoUrl());
        savedMovie.setReleaseDate(movie.getReleaseDate());

        movieDAO.save(savedMovie);

        return savedMovie;
    }

    @Override
    public boolean deleteMovie(int id) {
        Movie savedMovie = getMovieDetails(id);
        if(savedMovie == null) {
            return false;
        }

        movieDAO.delete(savedMovie);
        return true;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieDAO.findAll();
    }

    @Override
    public Page<Movie> getPaginatedMovieDetails(Pageable pageable) {
        return movieDAO.findAll(pageable);
    }

    @Override
    public boolean bookMovie(User user, Movie movie, Theatre theatre) {

        // Check whether requested movie is valid or not
        Optional<Movie> requestedMovie = movieDAO.findById(movie.getMovieId());
        if(!requestedMovie.isPresent())
            return false;

            // Check if the user is valid
            Map<String, String> userUriMap = new HashMap<>();
            userUriMap.put("id", String.valueOf(user.getUserId()));

            User receivedUser = restTemplate.getForObject(userAppUrl, User.class, userUriMap);

            if(receivedUser == null)
            return false;

            // Check whether movie and theatre combination is available

            Map<String, String> theatreUriMap = new HashMap<>();
            theatreUriMap.put("theatreId", String.valueOf(theatre.getTheatreId()));

            //Theatre receivedTheatre = restTemplate.getForObject(theatreAppUrl, Theatre.class, theatreUriMap);
        Theatre receivedTheatre = theatreServiceClient.getTheatre(theatre.getTheatreId(), theatre.getMovieId());
            if(receivedTheatre == null) {
                return false;
            }
                return true;
    }
}
