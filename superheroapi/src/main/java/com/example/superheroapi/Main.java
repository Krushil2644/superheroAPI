package com.example.superheroapi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/superheroapi/View.fxml"));
            Parent root = loader.load();

            // Create a scene with the loaded FXML
            Scene scene = new Scene(root);

            // Apply CSS styling
            scene.getStylesheets().add(getClass().getResource("/com/example/superheroapi/styles.css").toExternalForm());

            primaryStage.setTitle("Superhero API Viewer");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
