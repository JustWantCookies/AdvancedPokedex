package com.example.advancedpokedex.ui;

import com.example.advancedpokedex.data.AuthServiceDatabaseException;
import com.example.advancedpokedex.data.Pokemon;
import com.example.advancedpokedex.data.User;
import com.example.advancedpokedex.services.AuthService;
import com.example.advancedpokedex.ui.internal.PokemonDetailScreen;
import com.example.advancedpokedex.ui.internal.PokemonListCellPrivate;
import com.example.advancedpokedex.ui.internal.PrivatePokedexClient;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class PrivatePokedex extends GlobalPokedex {

    private User user;

    PrivatePokedexClient client = new PrivatePokedexClient();


    private AuthService authService;

    public PrivatePokedex() {
        super();
    }


    /**
     * Initializes and starts the Pokedex application by setting up the main stage,
     * loading Pokemon data, initializing the detail screen, and optionally initializing
     * the authentication service if it is available.
     *
     * @param stage The primary stage for the Pokedex application.
     */
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        overViewScene = new Scene(buildOverviewScene(), WINDOW_WIDTH, WINDOW_HEIGHT);
        detailScreen = new PokemonDetailScreen(mainStage, overViewScene); // Initialize the detailScreen
        try {
            authService = new AuthService();
        } catch (AuthServiceDatabaseException e) {
            authService = null;
        }

        if(authService != null){
          //  authService.addUserWithDlg();
        }

        mainStage.setTitle("Pokedex!");
        mainStage.setScene(overViewScene);
        mainStage.show();

        loadPokemonDataDetails();
    }

    /**
     * Builds and returns the detail screen for a specific Pokemon, which includes its detailed information,
     * comments, and an option to add a new comment.
     *
     * @param pokemon The Pokemon object for which to build the detail screen.
     * @return A BorderPane containing the detail screen for the specified Pokemon with comment functionality.
     */
    @Override
    protected BorderPane buildDetailScreen(Pokemon pokemon) {
        BorderPane detailScene = super.buildDetailScreen(pokemon); // Call the parent's buildDetailScene method
        TextArea textArea = new TextArea();

        //GET ALL COMMENTS
        textArea.appendText("USER1: dummy \nUSER2: dummy2");

        Button commentButton = new Button("Add Comment");
        commentButton.setOnAction(event -> addComment(pokemon));

        detailScene.setBottom(new VBox(commentButton, textArea));

        return detailScene;
    }

    /**
     * Displays a dialog to add a comment for a specific Pokemon, allowing the user to choose the comment's visibility (public or private).
     * The comment is then sent to a client and can be handled accordingly based on its visibility.
     *
     * @param pokemon The Pokemon object for which to add a comment.
     */
    private void addComment(Pokemon pokemon) {
        // Create a choice dialog to select comment visibility
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Public", "Public", "Private");
        choiceDialog.setTitle("Add Comment");
        choiceDialog.setHeaderText("Add a comment for " + pokemon.getName());
        choiceDialog.setContentText("Select comment visibility:");

        Optional<String> visibilityResult = choiceDialog.showAndWait();

        visibilityResult.ifPresent(visibility -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Comment");
            dialog.setHeaderText("Add a " + visibility + " comment for " + pokemon.getName());
            dialog.setContentText("Enter your comment:");

            Optional<String> commentResult = dialog.showAndWait();

            commentResult.ifPresent(comment -> {
                client.sendMessage("COMMENT MADE -> " + comment);
            if (visibility.equals("Public")) {
                    // Handle public comment
                    // You can implement your own data storage and handling mechanism
                } else if (visibility.equals("Private")) {
                    // Handle private comment
                    // You can implement your own data storage and handling mechanism
                }
                showAlert("Comment Added", visibility + " comment for " + pokemon.getName() + " added successfully.");
            });
        });
    }

    /**
     * Sets the cell factory for a ListView of Pokemon, allowing custom rendering of each item
     * with private details.
     *
     * @param listViewPokemon The ListView to set the cell factory for.
     */
    protected void setListFactory(ListView<Pokemon> listViewPokemon) {
        listViewPokemon.setCellFactory(param -> new PokemonListCellPrivate());
    }

    /**
     * Displays an informational alert dialog with the specified title and content.
     * Helper method to display alerts
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

    public static void main(String[] args) {
        launch(args);
    }
}
