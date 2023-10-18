package com.example.tankproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class AnimationsCreator {

    public ImageView explosionImageView;
    private final ArrayList<Image> explosionImages;
    private final int explosionNumImages = 11;
    private final double explosionSize = 80;
    private final Timeline explosionTimeLine;

    public AnimationsCreator() {
        this.explosionImageView = new ImageView();
        this.explosionImages = preloadImages(this.explosionNumImages,"images/explosion/");
        this.explosionImageView.setFitWidth(this.explosionSize);
        this.explosionImageView.setPreserveRatio(true);
        this.explosionTimeLine = explosionTimelineInitialize();
    }

    // Starts explosion animation from a position
    public void startExplosionAnimation(Point position) {
        this.explosionImageView.setTranslateX(ComponentsCreator.transformX(position.getX()));
        this.explosionImageView.setTranslateY(ComponentsCreator.transformY(position.getY()));
        this.explosionTimeLine.play();
    }

    // Timeline explosion animation initialize
    private Timeline explosionTimelineInitialize() {
        Timeline timeline = new Timeline();
        int frameDuration = 100;
        for (int i = 0; i < this.explosionNumImages; i++) {
            int finalI = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * frameDuration), event -> updateImage(this.explosionImageView, this.explosionImages, finalI));
            timeline.getKeyFrames().add(keyFrame);
        }

        return timeline;
    }

    // Preloads animation images into an ArrayList to speed up the animation process
    private ArrayList<Image> preloadImages(int numImages, String path) {
        ArrayList<Image> temp = new ArrayList<>();

        for (int i = 0; i < numImages; i++) {
            temp.add(new Image(Objects.requireNonNull(ComponentsCreator.class.getResource( path + i + ".png")).toExternalForm()));
        }
        return temp;
    }

    // Updates ImageView from an Image in an ArrayList
    private void updateImage(ImageView imageView, ArrayList<Image> images, int index) {
        imageView.setImage(images.get(index));
    }
}
