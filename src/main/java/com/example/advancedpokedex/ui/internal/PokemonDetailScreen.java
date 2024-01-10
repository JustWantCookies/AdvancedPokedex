package com.example.advancedpokedex.ui.internal;

import com.example.advancedpokedex.data.Pokemon;
import com.example.advancedpokedex.data.PokemonStat;
import com.example.advancedpokedex.data.PokemonType;
import com.example.advancedpokedex.ui.GlobalPokedex;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PokemonDetailScreen {
    private final Stage mainStage;
    private final Scene overviewScene;

    public PokemonDetailScreen(Stage mainStage, Scene overviewScene) {
        this.mainStage = mainStage;
        this.overviewScene = overviewScene;
    }

    /**
     * Builds and returns the detail scene for a specific Pokemon, which displays its name, image, statistics,
     * types, and a button to navigate back to the overview scene.
     *
     * @param pokemon The Pokemon object for which to build the detail scene.
     * @return A BorderPane containing the detail scene for the specified Pokemon.
     */
    public BorderPane buildDetailScene(Pokemon pokemon) {
        BorderPane pane = new BorderPane();
        Label name = new Label();
        ImageView imageView = new ImageView();
        VBox topVbox = new VBox();
        VBox rightVbox = new VBox();
        VBox leftVbox = new VBox();
        Button backButton = new Button("Back");

        name.setStyle("-fx-font-size: 30px; -fx-padding: 10px;");

        imageView.setImage(new Image(pokemon.getSprites().getFrontDefault()));
        for (PokemonStat pokemonStat : pokemon.getStats()) {
            rightVbox.getChildren().add(createPokemonStatNode(pokemonStat));
        }
        for (PokemonType pokemonType : pokemon.getTypes()) {
            leftVbox.getChildren().add(new Text(pokemonType.getType().getName()));
        }

        imageView.setFitWidth(350);
        imageView.setPreserveRatio(true);

        backButton.setOnAction(event -> mainStage.setScene(overviewScene));
        name.setText(pokemon.getName());

        topVbox.getChildren().add(backButton);
        topVbox.getChildren().add(name);

        pane.setCenter(imageView);
        pane.setTop(topVbox);
        pane.setRight(rightVbox);
        pane.setLeft(leftVbox);

        return pane;
    }

    /**
     * Creates and returns a graphical representation of a Pokemon statistic, including its name and base value.
     *
     * @param pokemonStat The PokemonStat object containing the statistic information.
     * @return A Node representing the Pokemon statistic.
     */
    private Node createPokemonStatNode(PokemonStat pokemonStat) {
        HBox hBox = new HBox();

        Text name = new Text();
        Text stat = new Text();
        Text spacer = new Text(": ");

        hBox.setAlignment(Pos.TOP_LEFT);

        name.setText(pokemonStat.getStatName().getStatName());
        stat.setText(String.valueOf(pokemonStat.getBaseStat()));

        name.setStyle("-fx-font-size: 12px; -fx-padding: 10px;");
        stat.setStyle("-fx-font-size: 12px; -fx-padding: 10px;");

        hBox.getChildren().add(name);
        hBox.getChildren().add(spacer);
        hBox.getChildren().add(stat);

        return hBox;
    }
}
