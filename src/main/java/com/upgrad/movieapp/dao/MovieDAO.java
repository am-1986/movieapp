package com.upgrad.movieapp.dao;

import com.upgrad.movieapp.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This layer will be used to talk to databases
 */
@Repository
public interface MovieDAO extends JpaRepository<Movie, Integer> {
    
}
