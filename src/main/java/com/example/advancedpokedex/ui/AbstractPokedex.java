package com.example.advancedpokedex.ui;

import com.example.adcancedpokedex.data.Pokemon;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

        //TODO cell factory for Pokemon Item -> https://stackoverflow.com/questions/36657299/how-can-i-populate-a-listview-in-javafx-using-custom-objects
        //TODO PokemonView Item

        return pane;
    }

}
