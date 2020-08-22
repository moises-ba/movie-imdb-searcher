package org.mba.challenge.test;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

//@Slf4j
public class Client {





    public static String efetuarChamada()   {
        StringBuilder lMessage = new StringBuilder();
        try {
            final String sizeSeparator = ":";

            Socket socket = new Socket("localhost",8000);

            String query = "title=avengers";

            PrintStream saida = new PrintStream(socket.getOutputStream());
            saida.println(String.valueOf(query.length()) + ":" + query);
            saida.flush();


            try(DataInputStream in = new DataInputStream(socket.getInputStream())) {


                String actualChar = "";
                do {
                    lMessage.append(actualChar);
                    actualChar  = String.valueOf((char)in.readByte());

                } while(!actualChar.equals(sizeSeparator));

                int lTamanhoMensagem = Integer.valueOf(lMessage.toString());

                System.out.println("tamanho mensagem : " + lTamanhoMensagem);


                lMessage.append(":");
                for(int i = 0; i < lTamanhoMensagem; i++) {
                    lMessage.append(String.valueOf((char)in.readByte()));
                }

            }




        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lMessage.toString();
    }

}
