package org.mba.challenge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.mba.challenge.config.MovieSearcherModule;
import org.mba.challenge.server.ServerSocket;

public class Application {

    public static void main(String... args) throws Exception {

        MovieSearcherModule module = new MovieSearcherModule();
        Injector injector = Guice.createInjector(module);

        ServerSocket serverSocket = injector.getInstance(ServerSocket.class);

        serverSocket.init();


    }

}
