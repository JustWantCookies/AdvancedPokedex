package com.example.advancedpokedex.ui;

import com.example.adcancedpokedex.data.Pokemon;
import com.example.advancedpokedex.ui.internal.PokemonListCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GlobalPokedex extends Application {

    //TODO Sort class
    //TODO clean up class
    private Stage mainStage;

    private Scene overViewScene;

    @Override
    public void start(Stage stage) {
        overViewScene = this.buildOverviewScene(callDetailPage());
        mainStage = stage;
        stage.setTitle("Global Pokedex!");
        stage.setScene(overViewScene);
        stage.show();
    }

    private Consumer<Pokemon> callDetailPage() {
        return pokemon -> {
            mainStage.setScene(this.buildDetailScene(pokemon));
            mainStage.show();
        };
    }

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 500;

    private static final List<Pokemon> pokemonList = new ArrayList<>();

    public Scene buildOverviewScene(Consumer<Pokemon> onDetailClick) {
        BorderPane pane = new BorderPane();

        TextField searchField = new TextField();
        searchField.setPromptText("Search your Pokemon here!");

        pane.setTop(searchField);

        //TODO Remove and get Real Data
        addTestData();

        FilteredList<Pokemon> filteredData = new FilteredList<>(FXCollections.observableArrayList(pokemonList), p -> true);

        ListView<Pokemon> listViewPokemon = new ListView<>(filteredData);
        listViewPokemon.setCellFactory(param -> new PokemonListCell());

        listViewPokemon.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Pokemon selectedPokemon = listViewPokemon.getSelectionModel().getSelectedItem();
                if (selectedPokemon != null) {
                    onDetailClick.accept(selectedPokemon);
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pokemon -> newValue.isEmpty() || pokemon.getName().toLowerCase().contains(newValue.toLowerCase()));
        });

        pane.setCenter(listViewPokemon);
        return new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public Scene buildDetailScene(Pokemon pokemon) {
        BorderPane pane = new BorderPane();

        Button backButton = new Button();
        backButton.setOnMouseClicked(event -> mainStage.setScene(overViewScene));

        pane.setTop(backButton);

        //TODO Sett Image With Left
        TextField test = new TextField();
        test.setText(pokemon.getName());

        pane.setCenter(test);

        return new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void addTestData(){
        pokemonList.add(new Pokemon("Glumanda", 1));
        pokemonList.add(new Pokemon("Arceus", 100));
        pokemonList.add(new Pokemon("Zekrom", 50));
    }
}
