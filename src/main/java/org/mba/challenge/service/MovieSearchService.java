package org.mba.challenge.service;

import org.mba.challenge.model.domain.IMDBSearchResponse;
import org.mba.challenge.model.exception.BussinessException;

import java.util.Optional;

public interface MovieSearchService {

    /**
     * Efetua a busca no IMDB com titulos contendo o nome no parametro informado
     * @param pTitle
     * @return
     */
    Optional<IMDBSearchResponse> findMovies(String pTitle) throws BussinessException;

    String findMoviesWithFormatedResponse(String pTitle)  throws BussinessException;


}
