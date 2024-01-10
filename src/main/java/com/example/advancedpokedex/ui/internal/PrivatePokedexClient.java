package com.example.advancedpokedex.ui.internal;

import com.example.advancedpokedex.exceptions.InternalProcessException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PrivatePokedexClient {
    private Socket clientSocket;
    private static final String SERVER_HOST = "localhost"; // Change to the server's hostname or IP address
    private static final int SERVER_PORT = 12345; // Use the same port as the GlobalPokedex server

    /**
     * Constructs a new PrivatePokedexClient instance and establishes a connection to the server
     * using the predefined SERVER_HOST and SERVER_PORT.
     *
     * @throws InternalProcessException if an I/O error occurs while attempting to establish the connection.
     */
    public PrivatePokedexClient() throws InternalProcessException{
        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            throw new InternalProcessException(e.getMessage());
        }
    }

    /**
     * Sends a message to the GlobalPokedex server using the established client socket connection.
     *
     * @param message The message to be sent to the server.
     * @throws InternalProcessException if an I/O error occurs while attempting to establish the connection.
     */
    public void sendMessage(String message) throws InternalProcessException {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            oos.writeObject(message);
            oos.flush();
            System.out.println("Sent message to GlobalPokedex: " + message);
        } catch (IOException e) {
            throw new InternalProcessException(e.getMessage());
        }
    }
}
