package org.mba.challenge.service;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.mba.challenge.model.domain.IMDBSearchResponse;
import org.mba.challenge.model.exception.BussinessException;
import org.mba.challenge.model.repository.IMBDMovieSearchRepository;

import java.util.Optional;

@Slf4j
public class MovieSearchServiceImpl implements MovieSearchService {

    @Inject
    private IMBDMovieSearchRepository iMBDMovieSearchRepository;


    @Override
    public Optional<IMDBSearchResponse> findMovies(String pTitle) throws BussinessException {
        return this.iMBDMovieSearchRepository.findMovies(pTitle);
    }

    @Override
    public String findMoviesWithFormatedResponse(String pTitle)  throws BussinessException {
        log.info("iniciando busca por filmes  com titulo: {}", pTitle);


        final StringBuilder lReturn = new StringBuilder();
        Optional<IMDBSearchResponse> lOptIMDBSearchResponse = this.findMovies(pTitle);

        if(lOptIMDBSearchResponse.isPresent()) {
            IMDBSearchResponse lIMDBSearchResponse = lOptIMDBSearchResponse.get();
            if(lIMDBSearchResponse.getSearch() != null) {
                lIMDBSearchResponse.getSearch().forEach(item -> {
                    if(lReturn.length() > 0) {
                        lReturn.append("\n");
                    }
                    lReturn.append(item.getTitle());
                });
            }
        } else {
            log.info("Filmes com titulo: {} nao encontrado", pTitle);
        }

        lReturn.append("\n");

        log.info("Fim da busca por filme");

        return lReturn.toString();
    }
}
