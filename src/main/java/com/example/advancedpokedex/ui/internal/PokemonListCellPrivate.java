package com.example.advancedpokedex.ui.internal;

import com.example.advancedpokedex.data.Pokemon;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PokemonListCellPrivate extends ListCell<Pokemon> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label detail2 = new Label();

    private final RadioButton catched = new RadioButton();
    private final RadioButton shiny = new RadioButton();
    private final HBox layout = new HBox(title, detail, catched, detail2, shiny);

    public PokemonListCellPrivate() {
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
            detail.setText("Is Favourite: ");

            setGraphic(layout);
        }
    }
}
