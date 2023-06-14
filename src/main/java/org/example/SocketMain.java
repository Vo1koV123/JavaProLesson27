package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SocketMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8081)) {
            System.out.println("Waiting for a client to connect... ... ...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            try (
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true))
            {
                writer.println("Hello! How are you?");
                boolean suspect = false;
                String message;

                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                    if (!suspect && (message.contains("ъ") || message.contains("э") || message.contains("ё") || message.contains("ы"))) {
                        writer.println("Що таке паляниця?");
                        suspect = true;
                        continue;
                    }

                    if (message.contains("Український круглий хліб із пшеничного борошна")) {
                        writer.println(LocalDateTime.now() + System.lineSeparator() + "Goodbye");
                        break;
                    }

                    if (suspect) {
                        clientSocket.shutdownInput();
                    }
                }

                System.out.println("Client disconnected, server will stop now");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
