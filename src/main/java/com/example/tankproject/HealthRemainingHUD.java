package com.example.tankproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.example.tankproject.App.scene;

public class HealthRemainingHUD {
    HBox healthRemainingHBox;
    Timeline timeline;
    double showingSeconds = 2.5;

    public HealthRemainingHUD() {
        this.healthRemainingHBox = new HBox();
        this.timeline = new Timeline();

        this.healthRemainingHBox.setId("healthRemainingHBox");
        this.healthRemainingHBox.setVisible(false);
        timelineAnimationInitialize();
    }

    // Makes HUD visible showing the tank remaining health
    public void showHUD(Tank tank, ImagesLoader images) {
        this.healthRemainingHBox.setVisible(true);
        this.healthRemainingHBox.getChildren().clear();
        this.healthRemainingHBox.getChildren().add(ComponentsCreator.createHealthRemainingHBox(tank,14,20, "Health:",5, Color.BLACK, images));
        this.healthRemainingHBox.setTranslateX(ComponentsCreator.transformX(tank.position.getX()));
        this.healthRemainingHBox.setTranslateY(ComponentsCreator.transformY(tank.position.getY()) - 65);

        this.timeline.play();
    }
    // Makes HUD visible when the mouse hovers over a tank
    public void HUDMouseEvents(ArrayList<Player> players, ImagesLoader images) {
        scene.setOnMouseMoved(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            for (Player p : players) {
                if ((Math.pow(p.tank.position.getX() - mouseX,2) + Math.pow(p.tank.position.getY() - mouseY,2))  <= (Math.pow(Constants.TANK_SIZE, 2))) {
                    showHUD(p.tank, images);
                }
            }
        });
    }

    // TimeLine animation initialize
    public void timelineAnimationInitialize() {
        // Makes HUD invisible after some time
        this.timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> healthRemainingHBox.setOpacity(1)),
                new KeyFrame(Duration.seconds(this.showingSeconds), e -> healthRemainingHBox.setOpacity(0))
        );

        this.timeline.setOnFinished(event -> this.timeline.stop());
    }

}
