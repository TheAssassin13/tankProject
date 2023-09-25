package com.example.tankproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    private static Stage stage;
    private static Scene scene;

    public static void setRoot(String fxml) throws IOException {
        App.scene.setRoot(loadFXML(fxml));
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        initializeGame();
    }

    public void initializeGame() throws IOException {
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("icons/windows_icon.png")).toExternalForm());
        scene = new Scene(loadFXML("menu"), Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        stage.setMinWidth(Constants.WINDOWS_WIDTH);
        stage.setMinHeight(Constants.WINDOWS_HEIGHT + 40);
        stage.setMaxWidth(Constants.WINDOWS_WIDTH);
        stage.setMaxHeight(Constants.WINDOWS_HEIGHT + 40);
        stage.getIcons().add(icon);
        stage.setTitle("Tank Project");
        stage.setScene(scene);
        stage.show();
    }

    // Closes actual window and start a new one
    public static void restartGame() throws IOException{
        stage.close();
        App newApp = new App();
        newApp.start(new Stage());
        App.setRoot("game");
    }

    // Converts a Color object to a Hexadecimal string
    public static String toHexString(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        return String.format("#%02x%02x%02x", red, green, blue);
    }
    public static void main(String[] args) {
        launch();
    }
}