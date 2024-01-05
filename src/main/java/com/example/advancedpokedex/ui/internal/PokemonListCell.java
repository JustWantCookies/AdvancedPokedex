package com.example.advancedpokedex.ui.internal;

import com.example.advancedpokedex.data.Pokemon;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class PokemonListCell extends ListCell<Pokemon> {
    //TODO remake the design of this class
    private final Label title = new Label();
    private final Label detail = new Label();
    private final VBox layout = new VBox(title, detail);

    public PokemonListCell() {
        super();
        title.setStyle("-fx-font-size: 20px;");
    }

    @Override
    protected void updateItem(Pokemon pokemon, boolean empty) {
        super.updateItem(pokemon, empty);

        setText(null);

        if (empty || pokemon == null || pokemon.getName() == null) {
            title.setText(null);
            detail.setText(null);
            setGraphic(null);
        } else {


            title.setText(pokemon.getName());
            //detail.setText("Level: " + pokemon.ge());


            /* TODO Remove
            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    System.out.println("Selected Pokemon: " + pokemon.getName());
                }
            });
             */

            setGraphic(layout);
        }
    }
}
