package com.example.advancedpokedex.ui;

import com.example.advancedpokedex.data.*;
import com.example.advancedpokedex.ui.internal.PokemonDetailScreen;
import com.example.advancedpokedex.ui.internal.PokemonListCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class GlobalPokedex extends Application {

    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 500;

    private Stage mainStage;
    private Scene overViewScene;
    private final PokemonService pokemonService = new PokemonService();
    private final TypeApi typeApi = new TypeApi();
    private final ObservableList<Pokemon> pokemonList = FXCollections.observableArrayList();

    private PokemonDetailScreen detailScreen; // Reference to PokemonDetailScreen

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        overViewScene = buildOverviewScene();
        detailScreen = new PokemonDetailScreen(mainStage, overViewScene); // Initialize the detailScreen

        mainStage.setTitle("Global Pokedex!");
        mainStage.setScene(overViewScene);
        mainStage.show();

        loadPokemonDataDetails();
    }

    private Scene buildOverviewScene() {
        BorderPane pane = new BorderPane();
        TextField searchField = new TextField();
        FilteredList<Pokemon> filteredData = new FilteredList<>(pokemonList, p -> true);
        ListView<Pokemon> listViewPokemon = new ListView<>(filteredData);
        listViewPokemon.setCellFactory(param -> new PokemonListCell());
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
        return new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void navigateToDetailPage(Pokemon pokemon) {
        Scene detailScene = detailScreen.buildDetailScene(pokemon); // Use the PokemonDetailScreen
        mainStage.setScene(detailScene);
        mainStage.show();
    }

    private void loadPokemonDataDetails() {
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
