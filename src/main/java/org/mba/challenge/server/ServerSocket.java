package org.mba.challenge.server;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mba.challenge.model.exception.BussinessException;
import org.mba.challenge.model.exception.EnvironmentException;
import org.mba.challenge.service.MovieSearchService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A escolha de utilização do socket Channel foi devida a leituda de sem bloqueios fazendo que que apenas uma thread se comunique com varias conexões abertas
 * no mesmo servidor atraves da criação de multiplos SocketChannels registrados em um selector.
 * evitando assim um overhead de criação de theads por conexão, pois a troca de contexto entre threads eh custosa para o sistema operacional
*/
@Slf4j
public class ServerSocket {

    //ex:
    //14:title=avengers
    private String requestPayloadPattern = "\\d*\\:title=\\S*";

    @Inject
    @Named("imdbsearcher.server.port")
    private int serverPort;

    @Inject
    private MovieSearchService movieSearchService;

    @Getter @Setter
    private boolean toContinue = true;




    public void init() throws EnvironmentException {

        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress("localhost", this.serverPort));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(256);


            System.out.println("Aplicativo iniciado e escutando na porta: " + this.serverPort);
            while (this.toContinue) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {

                    SelectionKey key = iter.next();

                    if (key.isAcceptable()) {
                        this.register(selector, serverSocket);
                    }

                    if (key.isReadable()) {
                        this.searchMovie(buffer, key);
                    }
                    iter.remove();
                }
            }

        } catch(Exception ex) {
            throw new EnvironmentException(ex);
        }

    }


    private void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }


    private void searchMovie(ByteBuffer buffer, SelectionKey key)
            throws IOException {

        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        buffer.flip();


        String queryMovie = new String(buffer.array()).trim();

        log.info("QUERY: {}",queryMovie);
        System.out.println(queryMovie);

        StringBuilder lReturnedMovies = new StringBuilder();
        if(Pattern.matches(this.requestPayloadPattern, queryMovie)) {
            try {
                lReturnedMovies.append(movieSearchService.findMoviesWithFormatedResponse(queryMovie.split("=")[1]));
            } catch (BussinessException e) {
                log.warn("Error: {}", e.getMessage());
                lReturnedMovies.append(e.getMessage());
            }
        } else {
            lReturnedMovies.append("Query no formato invalido\\n");
            log.warn(lReturnedMovies.toString());
        }

        lReturnedMovies.insert(0, lReturnedMovies.length() + ":")
                .append(lReturnedMovies);

        ByteBuffer writeBuffer = ByteBuffer.allocate(lReturnedMovies.length()*2);
        writeBuffer.put(lReturnedMovies.toString().getBytes());

        //responder para o cliente
        buffer.clear();
        writeBuffer.flip();
        client.write(writeBuffer);
        writeBuffer.clear();


        client.close();
    }


}
