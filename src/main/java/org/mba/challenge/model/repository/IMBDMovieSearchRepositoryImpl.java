package org.mba.challenge.model.repository;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.mba.challenge.model.domain.IMDBSearchResponse;
import org.mba.challenge.model.exception.BussinessException;
import org.mba.challenge.model.exception.EnvironmentException;
import org.mba.challenge.utils.JSONUtils;

import java.io.IOException;
import java.util.Optional;

@Slf4j

public class IMBDMovieSearchRepositoryImpl implements IMBDMovieSearchRepository {

    private final static String ENCODING = "UTF-8";

    @Inject
    @Named("imdb.apikey")
    private String imdbAPIKey;

    @Inject
    @Named("imdb.url")
    private String imbdURL;

    @Inject
    private CloseableHttpClient httpClient;



    @Override
    public Optional<IMDBSearchResponse> findMovies(String pTitle) throws BussinessException {


        StringBuilder lUrlBuilder = new StringBuilder(this.imbdURL)
                .append("?apikey=").append(this.imdbAPIKey)
                .append("&s=").append(pTitle);

        HttpGet lHttpGetRequest = new HttpGet(lUrlBuilder.toString());

        try(CloseableHttpResponse lResponse = httpClient.execute(lHttpGetRequest)) {

          if (HttpStatus.SC_OK == lResponse.getStatusLine().getStatusCode()) {

              HttpEntity lHttpEntity = lResponse.getEntity();
              String lStrJsonResponse = EntityUtils.toString(lHttpEntity,ENCODING);

              IMDBSearchResponse lIMDBSearchResponse =
                      JSONUtils.getInstance().toObject(lStrJsonResponse.getBytes(ENCODING), IMDBSearchResponse.class);

              if(!lIMDBSearchResponse.containsError()) {
                  return Optional.of(lIMDBSearchResponse);
              }

             return Optional.empty();
          } else {
              log.warn("Falha ao chamar serviço IMDB returnCode={}", lResponse.getStatusLine().getStatusCode());
              throw new BussinessException("falha ao consultar filmes");
          }


        } catch (IOException ex) {
            log.error("Falha ao chamar serviço IMDB ", ex);
            throw new EnvironmentException(ex);
        }


    }





}
