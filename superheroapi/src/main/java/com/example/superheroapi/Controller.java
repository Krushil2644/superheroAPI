package com.example.superheroapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultList;

    @FXML
    private VBox detailBox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label placeOfBirthLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private ImageView heroImageView;

    @FXML
    private Button backButton;

    private List<String> heroNames = new ArrayList<>();
    private JsonObject selectedHero;

    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText();
        resultList.getItems().clear();

        if (!searchTerm.isEmpty()) {
            List<String> results = searchSuperheroes(searchTerm);
            resultList.getItems().addAll(results);
            heroNames = results;
        } else {
            resultList.getItems().add("Please enter a search term.");
        }
    }

    private List<String> searchSuperheroes(String searchTerm) {
        List<String> results = new ArrayList<>();
        String apiUrl = "https://superheroapi.com/api/dacbe5829537a6f8742a088665527492/search/" + searchTerm;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

            if (jsonResponse.has("results")) {
                JsonArray heroes = jsonResponse.getAsJsonArray("results");
                for (int i = 0; i < heroes.size(); i++) {
                    JsonObject hero = heroes.get(i).getAsJsonObject();
                    results.add(hero.get("name").getAsString());
                }
            } else {
                results.add("No heroes found for the search term: " + searchTerm);
            }

        } catch (Exception e) {
            e.printStackTrace();
            results.add("Error fetching results. Please try again.");
        }

        return results;
    }

    @FXML
    public void handleItemClick() {
        String selectedItem = resultList.getSelectionModel().getSelectedItem();

        if (selectedItem != null && !selectedItem.isEmpty()) {
            selectedHero = getHeroDetails(selectedItem);
            displayHeroDetails(selectedHero);
        }
    }

    private JsonObject getHeroDetails(String heroName) {
        String apiUrl = "https://superheroapi.com/api/dacbe5829537a6f8742a088665527492/search/" + heroName;
        JsonObject heroDetails = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray heroes = jsonResponse.getAsJsonArray("results");

            if (heroes != null && heroes.size() > 0) {
                heroDetails = heroes.get(0).getAsJsonObject();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return heroDetails;
    }

    private void displayHeroDetails(JsonObject hero) {
        if (hero != null) {
            nameLabel.setText("Name: " + hero.get("name").getAsString());
            fullNameLabel.setText("Full Name: " + hero.get("biography").getAsJsonObject().get("full-name").getAsString());
            placeOfBirthLabel.setText("Place of Birth: " + hero.get("biography").getAsJsonObject().get("place-of-birth").getAsString());
            publisherLabel.setText("Publisher: " + hero.get("biography").getAsJsonObject().get("publisher").getAsString());

            // Load and display the hero image
            String imageUrl = hero.get("image").getAsJsonObject().get("url").getAsString();
            Image heroImage = new Image(imageUrl, 300, 300, true, true); // Maintain aspect ratio
            heroImageView.setImage(heroImage);

            detailBox.setVisible(true);
        } else {
            nameLabel.setText("No details available.");
            fullNameLabel.setText("");
            placeOfBirthLabel.setText("");
            publisherLabel.setText("");
            heroImageView.setImage(null);
            detailBox.setVisible(true);
        }
    }

    @FXML
    public void handleBackButton() {
        detailBox.setVisible(false);
    }
}
