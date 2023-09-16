package com.example.tankproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {
    private static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        MainApp.stage = stage;
        initializeGame();
    }

    public void initializeGame() throws IOException {
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("icons/windows_icon.png")).toExternalForm());
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        stage.getIcons().add(icon);
        stage.setTitle("Tank Project");
        stage.setScene(scene);
        stage.show();
    }
    // Convert a Color object to a Hexadecimal string
    public static String toHexString(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        return String.format("#%02x%02x%02x", red, green, blue);
    }

    // Close actual window and start a new one
    public static void restartGame() throws IOException{
        stage.close();
        MainApp newApp = new MainApp();
        newApp.start(new Stage());
    }
    public static void main(String[] args) {
        launch();
    }
}