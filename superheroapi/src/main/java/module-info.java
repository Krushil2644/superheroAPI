module com.example.superheroapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.superheroapi to javafx.fxml;
    exports com.example.superheroapi;
}