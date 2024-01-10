package com.example.advancedpokedex.ui;

import com.example.advancedpokedex.data.AuthServiceDatabaseException;
import com.example.advancedpokedex.data.Pokemon;
import com.example.advancedpokedex.data.User;
import com.example.advancedpokedex.data.pojo.Note;
import com.example.advancedpokedex.services.AuthService;
import com.example.advancedpokedex.ui.internal.PokemonDetailScreen;
import com.example.advancedpokedex.ui.internal.PokemonListCellPrivate;
import com.example.advancedpokedex.ui.internal.PrivatePokedexClient;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

/**
 * A ui class for showing PrivatPokedex.
 */
public class PrivatePokedex extends GlobalPokedex {
    PrivatePokedexClient client = new PrivatePokedexClient();
    private User user;

    private Scene authScene;
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
        authScene = new Scene(buildAuthScene(), WINDOW_WIDTH, WINDOW_HEIGHT);

        try {
            authService = new AuthService();
        } catch (AuthServiceDatabaseException e) {
            //TODO
        }

        mainStage.setTitle("Pokedex!");
        mainStage.setScene(authScene);
        mainStage.show();

        loadPokemonDataDetails();
    }

    private BorderPane buildAuthScene() {
        BorderPane pane = new BorderPane();
        Button login = new Button("Login");
        Button register = new Button("Register");

        login.setOnMouseClicked(mouseEvent -> {
            user = authService.getUserByUid(authService.performLogon());
            if (user != null) {
                mainStage.setScene(overViewScene);
                mainStage.show();
            }
        });

        register.setOnMouseClicked(e -> authService.addUserWithDlg());

        pane.setCenter(new VBox(login, register));

        return pane;
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

        Button commentButton = new Button("Add Comment");
        commentButton.setOnAction(event -> addComment(pokemon));

        detailScene.setBottom(new VBox(commentButton, detailScene.getBottom()));

        return detailScene;
    }

    /**
     * Retrieves and formats public notes from other users associated with a specific Pokemon as a string.
     *
     * @param pokemon The Pokemon for which to retrieve and format public notes.
     * @return A formatted string containing public notes from other users for the specified Pokemon.
     */
    @Override
    protected String getNotesFromPokemonAsString(Pokemon pokemon) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Note> notes = noteService.readAllNotesForPokemon(pokemon.getName());
        for (Note note : notes) {
            if (!note.isPublic() || note.getAuthor() != user)
                continue;
            String username = "Unknown";
            if (note.getAuthor() != null)
                username = note.getAuthor().getUname();

            stringBuilder.append(username).append(": ").append(note.getContent()).append("\n");
        }
        return stringBuilder.toString();
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
                noteService.writeNoteForPokemon(pokemon.getName(), comment, user, visibility.equals("Public"));

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
