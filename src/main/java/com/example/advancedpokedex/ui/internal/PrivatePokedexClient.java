package com.example.advancedpokedex.ui.internal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PrivatePokedexClient {
    private Socket clientSocket;
    private static final String SERVER_HOST = "localhost"; // Change to the server's hostname or IP address
    private static final int SERVER_PORT = 12345; // Use the same port as the GlobalPokedex server

    public PrivatePokedexClient() {
        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            oos.writeObject(message);
            oos.flush();
            System.out.println("Sent message to GlobalPokedex: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
