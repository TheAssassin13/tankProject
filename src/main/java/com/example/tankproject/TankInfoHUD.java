package com.example.tankproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.example.tankproject.App.scene;

public class TankInfoHUD {
    HBox tankInfoHBox;
    Timeline timeline;
    double showingSeconds = 2.5;

    public TankInfoHUD() {
        this.tankInfoHBox = new HBox();
        this.timeline = new Timeline();

        this.tankInfoHBox.setId("tankInfoHBox");
        this.tankInfoHBox.setVisible(false);
        timelineAnimationInitialize();
    }

    // Makes HUD visible showing the tank remaining health
    public void showHUD(Tank tank) {
        this.tankInfoHBox.setVisible(true);
        this.tankInfoHBox.getChildren().clear();
        this.tankInfoHBox.getChildren().add(ComponentsCreator.createHealthRemainingHBox(tank,14,20,5, Color.BLACK,true));
        this.tankInfoHBox.setTranslateX(ComponentsCreator.transformX(tank.position.getX()));
        this.tankInfoHBox.setTranslateY(ComponentsCreator.transformY(tank.position.getY()) - 65);

        this.timeline.play();
    }
    // Makes HUD visible when the mouse hovers over a tank
    public void HUDMouseEvents(ArrayList<Player> players) {
        scene.setOnMouseMoved(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            for (Player p : players) {
                if ((Math.pow(p.tank.position.getX() - mouseX,2) + Math.pow(p.tank.position.getY() - mouseY,2))  <= (Math.pow(Constants.TANK_SIZE, 2))) {
                    showHUD(p.tank);
                }
            }
        });
    }

    // TimeLine animation initialize
    public void timelineAnimationInitialize() {
        // Makes HUD invisible after some time
        this.timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> tankInfoHBox.setOpacity(1)),
                new KeyFrame(Duration.seconds(this.showingSeconds), e -> tankInfoHBox.setOpacity(0))
        );

        this.timeline.setOnFinished(event -> this.timeline.stop());
    }

}
