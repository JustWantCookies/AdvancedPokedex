module com.example.advancedpokedex {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.advancedpokedex to javafx.fxml;
    exports com.example.advancedpokedex;
}