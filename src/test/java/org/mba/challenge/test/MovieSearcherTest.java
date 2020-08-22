package org.mba.challenge.test;


import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mba.challenge.model.repository.IMBDMovieSearchRepository;
import org.mba.challenge.model.repository.IMBDMovieSearchRepositoryImpl;
import org.mba.challenge.server.ServerSocket;
import org.mba.challenge.service.MovieSearchService;
import org.mba.challenge.service.MovieSearchServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class MovieSearcherTest {



    private final static String MOVIE = "13:The Avengers\n";

    @Mock
    private CloseableHttpClient httpClient;

    @InjectMocks
    private IMBDMovieSearchRepository imbdMovieSearchRepository = new IMBDMovieSearchRepositoryImpl();

    private final ServerSocket serverSocket = new ServerSocket();

    @Before
    public void init(){
        MovieSearchService movieSearchService = new MovieSearchServiceImpl();        ;
        Setter.set(serverSocket,"serverPort", 8000);
        Setter.set(serverSocket,"movieSearchService", movieSearchService);

        Setter.set(imbdMovieSearchRepository,"imdbAPIKey", "");
        Setter.set(imbdMovieSearchRepository,"imbdURL", "");

        Setter.set(movieSearchService,"iMBDMovieSearchRepository", imbdMovieSearchRepository);
    }


    @Test
    public void searchTest() throws Exception {
        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class)))
                .thenReturn(this.sucessCloseableHttpResponse());

        //iniciando o servidor
        new Thread(() ->serverSocket.init()).start();



        String filmeRetornado = Client.efetuarChamada();

        serverSocket.setToContinue(false);

        System.out.println("--" + filmeRetornado);

        Assert.assertTrue("Deveria ter sido retornado: " + MOVIE,
                MOVIE.equals(filmeRetornado));

    }


    static class Setter {
        public static void set(Object obj, String fieldName, Object value)  {

            try {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(obj, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    protected CloseableHttpResponse sucessCloseableHttpResponse() {
        return new CloseableHttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                return new Header[0];
            }

            @Override
            public Header getFirstHeader(String name) {
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                return new Header[0];
            }

            @Override
            public void addHeader(Header header) {

            }

            @Override
            public void addHeader(String name, String value) {

            }

            @Override
            public void setHeader(Header header) {

            }

            @Override
            public void setHeader(String name, String value) {

            }

            @Override
            public void setHeaders(Header[] headers) {

            }

            @Override
            public void removeHeader(Header header) {

            }

            @Override
            public void removeHeaders(String name) {

            }

            @Override
            public HeaderIterator headerIterator() {
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                return null;
            }

            @Override
            public HttpParams getParams() {
                return null;
            }

            @Override
            public void setParams(HttpParams params) {

            }

            @Override
            public StatusLine getStatusLine() {
                StatusLine lStatusLine = new StatusLine(){

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        return new ProtocolVersion("",1,1);
                    }

                    @Override
                    public int getStatusCode() {
                        return HttpStatus.SC_OK;
                    }

                    @Override
                    public String getReasonPhrase() {
                        return "";
                    }
                };


                return lStatusLine;
            }

            @Override
            public void setStatusLine(StatusLine statusline) {

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {

            }

            @Override
            public HttpEntity getEntity() {
                HttpEntity lHttpEntity = new StringEntity("{\n" +
                        "    \"Search\": [\n" +
                        "        {\n" +
                        "            \"Title\": \"The Avengers\",\n" +
                        "            \"Year\": \"2012\",\n" +
                        "            \"imdbID\": \"tt0848228\",\n" +
                        "            \"Type\": \"movie\",\n" +
                        "            \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"totalResults\": \"1\",\n" +
                        "    \"Response\": \"True\"\n" +
                        "}\n","UTF-8");
                return lHttpEntity;
            }

            @Override
            public void setEntity(HttpEntity entity) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public void setLocale(Locale loc) {

            }

            @Override
            public void close() throws IOException {

            }
        };
    }

}
