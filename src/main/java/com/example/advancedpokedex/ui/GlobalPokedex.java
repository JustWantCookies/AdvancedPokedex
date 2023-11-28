package com.example.advancedpokedex.ui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GlobalPokedex extends AbstractPokedex{

    @Override
    public void start(Stage stage) {
        Pane overviewPane = this.buildOverviewPane();
        Scene scene = new Scene(overviewPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Global Pokedex!");
        stage.setScene(scene);
        stage.show();
    }
}
