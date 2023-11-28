package com.example.advancedpokedex.ui;

import com.example.adcancedpokedex.data.Pokemon;
import com.example.advancedpokedex.ui.internal.PokemonListCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public abstract class AbstractPokedex extends Application {

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 500;

    private static ObservableList<Pokemon> pokemonList = FXCollections.observableArrayList();

    public Pane buildOverviewPane() {
        BorderPane pane = new BorderPane();

        TextField searchField = new TextField();
        searchField.setPromptText("Search your Pokemon here!");

        pane.setTop(searchField);

        //TODO Remove
        addTestData();

        ListView<Pokemon> listViewPokemon = new ListView<>(pokemonList);
        listViewPokemon.setCellFactory(param -> new PokemonListCell());

        pane.setCenter(listViewPokemon);

        return pane;
    }

    private void addTestData(){
        pokemonList.add(new Pokemon("Glumanda", 1));
        pokemonList.add(new Pokemon("Arceus", 100));
        pokemonList.add(new Pokemon("Zekrom", 50));
    }

}
