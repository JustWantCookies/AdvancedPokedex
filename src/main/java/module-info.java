module com.example.advancedpokedex {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.advancedpokedex.ui to javafx.fxml;
    exports com.example.advancedpokedex.ui;
}