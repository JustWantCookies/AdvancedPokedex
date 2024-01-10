package com.example.advancedpokedex.ui.internal;

import com.example.advancedpokedex.data.Pokemon;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class PokemonListCell extends ListCell<Pokemon> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final VBox layout = new VBox(title, detail);

    public PokemonListCell() {
        super();
        title.setStyle("-fx-font-size: 20px;");
    }

    /**
     * Customizes the rendering of a single item in a ListView cell for displaying Pokemon data.
     * Updates the cell's content to show the Pokemon's name.
     *
     * @param pokemon The Pokemon object to be displayed in the cell.
     * @param empty   A boolean indicating whether the cell should be empty.
     */
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

            setGraphic(layout);
        }
    }
}
