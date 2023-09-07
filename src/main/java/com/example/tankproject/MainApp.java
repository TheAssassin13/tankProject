package com.example.tankproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/com/example/tankProject/icons/windows_icon.png")).toExternalForm());
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        stage.getIcons().add(icon);
        stage.setTitle("Tank Project");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

    }
}