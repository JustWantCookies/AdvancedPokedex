module com.example.advancedpokedex {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    exports com.example.advancedpokedex.data;
    opens com.example.advancedpokedex.ui to javafx.fxml;
    exports com.example.advancedpokedex.ui;
exports com.example.advancedpokedex.ui.internal;
    exports com.example.advancedpokedex.services;

}