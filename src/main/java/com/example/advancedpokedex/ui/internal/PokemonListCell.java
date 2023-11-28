package com.example.advancedpokedex.ui.internal;

import com.example.adcancedpokedex.data.Pokemon;
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
    protected void updateItem(Pokemon item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);

        if (empty || item == null || item.getName() == null) {
            title.setText(null);
            detail.setText(null);
            setGraphic(null);
        } else {
            title.setText(item.getName());
            detail.setText("Level: " + item.getLevel());
            setGraphic(layout);
        }
    }
}
