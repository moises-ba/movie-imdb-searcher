package org.mba.challenge.config;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.mba.challenge.model.repository.IMBDMovieSearchRepository;
import org.mba.challenge.model.repository.IMBDMovieSearchRepositoryImpl;
import org.mba.challenge.service.MovieSearchService;
import org.mba.challenge.service.MovieSearchServiceImpl;

import java.util.Properties;

public class MovieSearcherModule extends AbstractModule {


    @Inject
    @Named("imdb.connection.timeout")
    private int imdbConnectionTimeout;

    @Provides
    CloseableHttpClient getClient() {
        return HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT)
                        .setConnectTimeout(this.imdbConnectionTimeout)
                        .setSocketTimeout(this.imdbConnectionTimeout)
                        .setConnectionRequestTimeout(this.imdbConnectionTimeout)
                        .build())
                .build();
    }

    @Override
    protected void configure() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
            Names.bindProperties(binder(), properties);
        } catch (Exception e) {
            throw new RuntimeException("Impossivel abrir arquivo application.properties", e);
        }

        bind(IMBDMovieSearchRepository.class).to(IMBDMovieSearchRepositoryImpl.class).in(Scopes.SINGLETON);;
        bind(MovieSearchService.class).to(MovieSearchServiceImpl.class).in(Scopes.SINGLETON);;

    }
}
