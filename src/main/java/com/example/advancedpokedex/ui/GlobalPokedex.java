package com.example.advancedpokedex.ui;

import com.example.adcancedpokedex.data.Pokemon;
import com.example.advancedpokedex.ui.internal.PokemonListCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GlobalPokedex extends Application {

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 500;

    private Stage mainStage;

    private Scene overViewScene;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        overViewScene = this.buildOverviewScene(navigateToDetailPage());

        mainStage.setTitle("Global Pokedex!");
        mainStage.setScene(overViewScene);
        mainStage.show();
    }

    private Consumer<Pokemon> navigateToDetailPage() {
        return pokemon -> {
            mainStage.setScene(this.buildDetailScene(pokemon));
            mainStage.show();
        };
    }

    private EventHandler<MouseEvent> navigateToOverviewPage() {
        return event -> mainStage.setScene(overViewScene);
    }

    private Scene buildOverviewScene(Consumer<Pokemon> onDetailClick) {
        BorderPane pane = new BorderPane();
        TextField searchField = new TextField();

        List<Pokemon> pokemonList = new ArrayList<>();
        FilteredList<Pokemon> filteredData = new FilteredList<>(FXCollections.observableArrayList(pokemonList), p -> true);
        ListView<Pokemon> listViewPokemon = new ListView<>(filteredData);
        listViewPokemon.setCellFactory(param -> new PokemonListCell());

        searchField.setPromptText("Search your Pokemon here!");

        //TODO Remove and get Real Data
        addTestData(pokemonList);

        listViewPokemon.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Pokemon selectedPokemon = listViewPokemon.getSelectionModel().getSelectedItem();
                if (selectedPokemon != null) {
                    onDetailClick.accept(selectedPokemon);
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

    private Scene buildDetailScene(Pokemon pokemon) {
        BorderPane pane = new BorderPane();
        TextField test = new TextField();
        Button backButton = new Button();

        backButton.setOnMouseClicked(navigateToOverviewPage());
        test.setText(pokemon.getName());

        pane.setCenter(test);
        pane.setTop(backButton);

        return new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void addTestData(List<Pokemon> pokemonList){
        pokemonList.add(new Pokemon("Glumanda", 1));
        pokemonList.add(new Pokemon("Arceus", 100));
        pokemonList.add(new Pokemon("Zekrom", 50));
    }
}
