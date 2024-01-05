package com.example.advancedpokedex.ui;

import com.example.advancedpokedex.data.Pokemon;
import com.example.advancedpokedex.data.PokemonService;
import com.example.advancedpokedex.ui.internal.PokemonListCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GlobalPokedex extends Application {

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 500;

    private Stage mainStage;
    private Scene overViewScene;
    private final PokemonService pokemonService = new PokemonService();
    private final ObservableList<Pokemon> pokemonList = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        overViewScene = buildOverviewScene();

        mainStage.setTitle("Global Pokedex!");
        mainStage.setScene(overViewScene);
        mainStage.show();

        loadPokemonData();
    }

    private Scene buildOverviewScene() {
        BorderPane pane = new BorderPane();
        TextField searchField = new TextField();
        FilteredList<Pokemon> filteredData = new FilteredList<>(pokemonList, p -> true);
        ListView<Pokemon> listViewPokemon = new ListView<>(filteredData);
        listViewPokemon.setCellFactory(param -> new PokemonListCell());

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
                filteredData.setPredicate(pokemon -> newValue.isEmpty() || pokemon.getName().toLowerCase().contains(newValue.toLowerCase()))
        );

        pane.setTop(searchField);
        pane.setCenter(listViewPokemon);
        return new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void navigateToDetailPage(Pokemon pokemon) {
        Scene detailScene = buildDetailScene(pokemon);
        mainStage.setScene(detailScene);
        mainStage.show();
    }

    private Scene buildDetailScene(Pokemon pokemon) {
        BorderPane pane = new BorderPane();
        Label name = new Label();
        ImageView imageView = new ImageView();
        VBox vBox = new VBox();
        Button backButton = new Button("Back");

        name.setStyle("-fx-font-size: 30px; -fx-padding: 10px;");

        loadPokemonDataDetail(pokemon, p -> imageView.setImage(new Image(p.getSprites().getFrontDefault())));

        imageView.setImage(new Image("default.jpg"));
        imageView.setFitWidth(350);
        imageView.setPreserveRatio(true);

        backButton.setOnAction(event -> mainStage.setScene(overViewScene));
        name.setText(pokemon.getName());

        vBox.getChildren().add(backButton);
        vBox.getChildren().add(name);

        pane.setCenter(imageView);
        pane.setTop(vBox);

        return new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void loadPokemonDataDetail(Pokemon pokemon, Consumer<Pokemon> callback) {
        Task<Pokemon> loadPokemonTask = new Task<>() {
            @Override
            protected Pokemon call() throws Exception {
                return pokemonService.getPokemonDetail(pokemon.getDetailURL());
            }
        };
        Thread thread = new Thread(loadPokemonTask);
        thread.setDaemon(true);
        thread.start();

        loadPokemonTask.setOnSucceeded(event -> {
            callback.accept(loadPokemonTask.getValue());
        });
    }

    private void loadPokemonData() {
        Task<List<Pokemon>> loadPokemonTask = new Task<>() {
            @Override
            protected List<Pokemon> call() throws Exception {
                return pokemonService.getPokemons();
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

    public static void main(String[] args) {
        launch(args);
    }
}
