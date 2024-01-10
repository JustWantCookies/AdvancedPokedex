package com.example.advancedpokedex.ui;

import com.example.advancedpokedex.data.Pokemon;
import com.example.advancedpokedex.data.PokemonService;
import com.example.advancedpokedex.data.TypeApi;
import com.example.advancedpokedex.data.User;
import com.example.advancedpokedex.data.pojo.Note;
import com.example.advancedpokedex.services.NoteService;
import com.example.advancedpokedex.ui.internal.GlobalPokedexServerRunnable;
import com.example.advancedpokedex.ui.internal.PokemonDetailScreen;
import com.example.advancedpokedex.ui.internal.PokemonListCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * A ui class for showing GlobalPokedex.
 */
public class GlobalPokedex extends Application {

    protected static final int WINDOW_WIDTH = 600;
    protected static final int WINDOW_HEIGHT = 500;

    protected static final String NOTE_PATH = "notes.txt";

    protected Stage mainStage;
    protected Scene overViewScene;
    protected final PokemonService pokemonService = new PokemonService();
    protected final TypeApi typeApi = new TypeApi();

    protected final NoteService noteService = new NoteService(NOTE_PATH);
    protected final ObservableList<Pokemon> pokemonList = FXCollections.observableArrayList();

    protected PokemonDetailScreen detailScreen;

    /**
     * Initializes and starts the Pokedex application by setting up the main stage,
     * loading Pokemon data, initializing the detail screen, and starting the server thread.
     *
     * @param stage The primary stage for the Pokedex application.
     */
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        overViewScene = new Scene(buildOverviewScene(), WINDOW_WIDTH, WINDOW_HEIGHT);
        detailScreen = new PokemonDetailScreen(mainStage, overViewScene); // Initialize the detailScreen

        mainStage.setTitle("Pokedex!");
        mainStage.setScene(overViewScene);
        mainStage.show();

        loadPokemonDataDetails();

        int serverPort = 12345; // Choose a port number
        GlobalPokedexServerRunnable serverRunnable = new GlobalPokedexServerRunnable(serverPort, this);
        Thread serverThread = new Thread(serverRunnable);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    /**
     * Builds and returns the overview scene for displaying a list of Pokemon.
     * This scene includes a search field, a type filter combo box, and a ListView for displaying the Pokemon.
     *
     * @return A BorderPane containing the overview scene.
     */
    protected BorderPane buildOverviewScene() {
        BorderPane pane = new BorderPane();
        TextField searchField = new TextField();
        FilteredList<Pokemon> filteredData = new FilteredList<>(pokemonList, p -> true);
        ListView<Pokemon> listViewPokemon = new ListView<>(filteredData);
        setListFactory(listViewPokemon);
        ComboBox<String> typeFilterComboBox = new ComboBox<>();
        typeFilterComboBox.setPromptText("Select type filter");
        loadPokemonTypesAsync(typeFilterComboBox);
        typeFilterComboBox.getItems().add("All");

        searchField.setPromptText("Search your Pokemon here!");

        listViewPokemon.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Pokemon selectedPokemon = listViewPokemon.getSelectionModel().getSelectedItem();
                if (selectedPokemon != null) {
                    navigateToDetailPage(selectedPokemon);
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(pokemon ->
                        (newValue.isEmpty() || pokemon.getName().toLowerCase().contains(newValue.toLowerCase()))
                                && (typeFilterComboBox.getSelectionModel().isEmpty() || newValue.equals("All") || pokemon.getTypes().stream().anyMatch(t -> t.getType().getName().contains(newValue)))
                )
        );
        typeFilterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(pokemon ->
                        (searchField.getText().isEmpty() || pokemon.getName().toLowerCase().contains(searchField.getText().toLowerCase()))
                                && (newValue == null || newValue.equals("All") || pokemon.getTypes().stream().anyMatch(t -> t.getType().getName().contains(newValue)))
                )
        );

        HBox filterBox = new HBox(searchField, typeFilterComboBox);
        filterBox.setSpacing(10);
        pane.setTop(filterBox);

        pane.setCenter(listViewPokemon);
        return pane;
    }

    /**
     * Sets the cell factory for a ListView of Pokemon, allowing custom rendering of each item.
     *
     * @param listViewPokemon The ListView to set the cell factory for.
     */
    protected void setListFactory(ListView<Pokemon> listViewPokemon) {
        listViewPokemon.setCellFactory(param -> new PokemonListCell());
    }

    /**
     * Navigates to the detail page for a specific Pokemon, displaying its detailed information.
     *
     * @param pokemon The Pokemon object for which to navigate to the detail page.
     */
    private void navigateToDetailPage(Pokemon pokemon) {
        Scene detailScene = new Scene(buildDetailScreen(pokemon), GlobalPokedex.WINDOW_WIDTH, GlobalPokedex.WINDOW_HEIGHT);
        mainStage.setScene(detailScene);
        mainStage.show();
    }

    /**
     * Builds and returns the detail screen for a specific Pokemon, which includes its detailed information.
     *
     * @param pokemon The Pokemon object for which to build the detail screen.
     * @return A BorderPane containing the detail screen for the specified Pokemon.
     */
    protected BorderPane buildDetailScreen(Pokemon pokemon){
        BorderPane pane = detailScreen.buildDetailScene(pokemon);

        TextArea textArea = new TextArea();
        Button reloadButton = new Button("Reload Comments");

        textArea.appendText(getNotesFromPokemonAsString(pokemon));

        reloadButton.setOnMouseClicked(mouseEvent -> textArea.setText(getNotesFromPokemonAsString(pokemon)));

        pane.setBottom(new VBox(reloadButton, textArea));

        return pane;
    }

    /**
     * Retrieves and formats public notes from other users associated with a specific Pokemon as a string.
     *
     * @param pokemon The Pokemon for which to retrieve and format public notes.
     * @return A formatted string containing public notes from other users for the specified Pokemon.
     */
    protected String getNotesFromPokemonAsString(Pokemon pokemon) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Note> notes = noteService.readAllNotesForPokemon(pokemon.getName());
        for (Note note : notes) {
            if(!note.isPublic())
                continue;
            String username = "Unknown";
            if(note.getAuthor() != null)
                username = note.getAuthor().getUname();

            stringBuilder.append(username).append(": ").append(note.getContent()).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Loads detailed Pokemon data asynchronously and populates the Pokemon list with the retrieved data.
     * This method creates a background task to fetch Pokemon details from a service.
     */
    protected void loadPokemonDataDetails() {
        Task<List<Pokemon>> loadPokemonTask = new Task<>() {
            @Override
            protected List<Pokemon> call() throws Exception {
                return pokemonService.getPokemonsDetalis();
            }
        };

        loadPokemonTask.setOnSucceeded(event -> {
            List<Pokemon> loadedData = loadPokemonTask.getValue();
            pokemonList.addAll(loadedData);
        });

        Thread thread = new Thread(loadPokemonTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Loads Pokemon types asynchronously and populates the provided ComboBox with the retrieved types.
     *
     * @param typeFilterComboBox The ComboBox to populate with Pokemon types.
     */
    private void loadPokemonTypesAsync(ComboBox<String> typeFilterComboBox) {
        Task<List<String>> loadTypesTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return typeApi.getTypes();
            }
        };

        loadTypesTask.setOnSucceeded(event -> {
            List<String> types = loadTypesTask.getValue();
            typeFilterComboBox.getItems().addAll(types);
        });

        Thread thread = new Thread(loadTypesTask);
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
