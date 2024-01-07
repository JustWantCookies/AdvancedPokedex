package com.example.advancedpokedex.ui.internal;

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
    private final GlobalPokedex globalPokedex;

    public GlobalPokedexServerRunnable(int port, GlobalPokedex globalPokedex) {
        SERVER_PORT = port;
        this.globalPokedex = globalPokedex;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("GlobalPokedexServer is listening for incoming connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            Object receivedObject = ois.readObject();

            if (receivedObject instanceof String) {
                String message = (String) receivedObject;
                System.out.println("Received message from PrivatePokedex: " + message);

                // Display a pop-up notification with the received message
                Platform.runLater(() -> showAlert("Message from PrivatePokedex", message));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

