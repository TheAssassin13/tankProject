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
    Timeline timeLine;
    double showingSeconds = 2.5;
    public HealthRemainingHUD() {
        this.healthRemainingHBox = new HBox();
        this.timeLine = new Timeline();

        this.healthRemainingHBox.setId("healthRemainingHBox");
        this.healthRemainingHBox.setVisible(false);
        timeLineAnimationInitialize();
    }

    // Makes HUD visible showing the tank remaining health
    public void healthRemainingBox(Tank tank) {
        this.healthRemainingHBox.setVisible(true);
        this.healthRemainingHBox.getChildren().clear();
        this.healthRemainingHBox.getChildren().add(ComponentsCreator.createHealthRemainingHBox(tank,14,20, "Health:",5, Color.BLACK));
        this.healthRemainingHBox.setTranslateX(ComponentsCreator.transformX(tank.position.getX()));
        this.healthRemainingHBox.setTranslateY(ComponentsCreator.transformY(tank.position.getY()) - 50);

        this.timeLine.play();
    }
    // Makes HUD visible when the mouse hovers over a tank
    public void healthRemainingBoxMouseEvents(ArrayList<Player> players) {
        scene.setOnMouseMoved(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            for (Player p : players) {
                if ((Math.pow(p.tank.position.getX() - mouseX,2) + Math.pow(p.tank.position.getY() - mouseY,2))  <= (Math.pow(Constants.TANK_SIZE, 2))) {
                    healthRemainingBox(p.tank);
                }
            }
        });
    }

    // TimeLine animation initialize
    public void timeLineAnimationInitialize() {
        // Makes HUD invisible after some time
        this.timeLine.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> healthRemainingHBox.setOpacity(1)),
                new KeyFrame(Duration.seconds(this.showingSeconds), e -> healthRemainingHBox.setOpacity(0))
        );

        this.timeLine.setOnFinished(event -> this.timeLine.stop());
    }

}
