package com.example.advancedpokedex.ui.internal;

import com.example.advancedpokedex.exceptions.InternalProcessException;
import com.example.advancedpokedex.ui.GlobalPokedex;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GlobalPokedexServerRunnable implements Runnable {
    private ServerSocket serverSocket;
    private final int SERVER_PORT;

    public GlobalPokedexServerRunnable(int port) {
        SERVER_PORT = port;
    }

    /**
     * Listens for incoming client connections and handles each client's requests.
     * This method continuously accepts client connections and processes them.
     * It runs indefinitely until an exception occurs.
     */
    @Override
    public void run() throws InternalProcessException {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            throw new InternalProcessException(e.getMessage());
        }
    }

    /**
     * Handles communication with a connected client socket, reading and processing incoming messages.
     * When a message is received, it is displayed as a pop-up notification.
     *
     * @param clientSocket The connected client socket to handle communication with.
     */
    private void handleClient(Socket clientSocket) throws InternalProcessException {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            Object receivedObject = ois.readObject();

            if (receivedObject instanceof String) {
                String message = (String) receivedObject;

                // Display a pop-up notification with the received message
                Platform.runLater(() -> showAlert("Message from PrivatePokedex", message));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new InternalProcessException(e.getMessage());
        }
    }

    /**
     * Displays an informational alert dialog with the specified title and content.
     *
     * @param title   The title of the alert dialog.
     * @param content The content text to be displayed in the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

