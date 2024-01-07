package com.example.advancedpokedex.ui;

import com.example.advancedpokedex.data.Pokemon;
import com.example.advancedpokedex.data.PokemonService;
import com.example.advancedpokedex.data.TypeApi;
import com.example.advancedpokedex.ui.internal.GlobalPokedexServerRunnable;
import com.example.advancedpokedex.ui.internal.PokemonDetailScreen;
import com.example.advancedpokedex.ui.internal.PokemonListCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class GlobalPokedex extends Application {

    protected static final int WINDOW_WIDTH = 600;
    protected static final int WINDOW_HEIGHT = 500;

    protected Stage mainStage;
    protected Scene overViewScene;
    protected final PokemonService pokemonService = new PokemonService();
    protected final TypeApi typeApi = new TypeApi();
    protected final ObservableList<Pokemon> pokemonList = FXCollections.observableArrayList();

    protected PokemonDetailScreen detailScreen;

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

    protected void setListFactory(ListView<Pokemon> listViewPokemon) {
        listViewPokemon.setCellFactory(param -> new PokemonListCell());
    }

    private void navigateToDetailPage(Pokemon pokemon) {
        Scene detailScene = new Scene(buildDetailScreen(pokemon), GlobalPokedex.WINDOW_WIDTH, GlobalPokedex.WINDOW_HEIGHT);
        mainStage.setScene(detailScene);
        mainStage.show();
    }

    protected BorderPane buildDetailScreen(Pokemon pokemon){
        return detailScreen.buildDetailScene(pokemon);
    }

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
