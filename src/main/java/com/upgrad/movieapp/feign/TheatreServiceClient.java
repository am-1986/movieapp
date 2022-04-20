package com.upgrad.movieapp.feign;

import com.upgrad.movieapp.entities.Theatre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "theatre-service", url = "http://localhost:8082")
public interface TheatreServiceClient {

    @RequestMapping(value = "/theatre_app/v1/theatres/{theatreId}/movies/{movieId}", method = RequestMethod.GET)
    Theatre getTheatre(@PathVariable(name = "theatreId") int theatreId, @PathVariable(name = "movieId") int movieId);
}
