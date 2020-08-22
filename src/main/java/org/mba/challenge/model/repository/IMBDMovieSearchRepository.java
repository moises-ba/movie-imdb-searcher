package org.mba.challenge.model.repository;

import org.mba.challenge.model.domain.IMDBSearchResponse;
import org.mba.challenge.model.exception.BussinessException;

import java.util.Optional;

public interface IMBDMovieSearchRepository {

    /**
     * Efetua a busca no IMDB com titulos contendo o nome no parametro informado
     * @param pTitle
     * @return
     */
    Optional<IMDBSearchResponse> findMovies(String pTitle) throws BussinessException;
}
