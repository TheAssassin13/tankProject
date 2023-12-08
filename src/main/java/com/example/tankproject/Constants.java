package com.example.tankproject;

import javafx.scene.paint.Color;


public class Constants {
    public final static int FPS = 60;
    public final static long FRAME_TIME = 1_000_000_000L / FPS;
    public final static int[] RESOLUTION_HEIGHT = {800, 720, 768, 900, 992, 1080};
    public final static int[] RESOLUTION_WIDTH = {800, 1280, 1366, 1440, 1768, 1920};
    public final static int SHOT_TRAJECTORY_SIZE = 10;
    public final static double[] SHOT_SIZE = {22, 40, 62};
    public final static int TANK_SIZE = 30;
    public final static int BOX_SIZE = 20;
    public final static double SHOT_VELOCITY = 0.2;
    public final static Color TERRAIN_COLOR = Color.BLACK;
    public final static Color TRAJECTORY_COLOR = Color.LIGHTGRAY;
    public final static Color[] TANK_COLORS = {Color.GREENYELLOW, Color.BLUE, Color.RED, Color.DARKCYAN, Color.ORANGE, Color.BEIGE, Color.BROWN, Color.GREEN, Color.VIOLET, Color.PINK};
    public final static int[] AMMO_DAMAGE= {30, 40, 50}; // Light, medium and heavy
    public final static int[] AMMO_PRICE = {1000, 2500, 4000}; // Light, medium and heavy
    public final static int INITIAL_CREDITS = 10000;
    public final static int CREDITS_FOR_DESTROYING_TANKS = 5000;
    public final static int CREDITS_FROM_POWER_UP = 1500;
    public final static int TANK_HEALTH = 100;
    public final static int TERRAIN_MARGIN = 50;
    public final static int WIND_MAX_VELOCITY = 10;
    public final static double MAX_VOLUME = 0.5;
    public final static int POINTS_FOR_DESTROYING_TANKS = 1000;
    public final static int POINTS_FOR_HITTING_SOMETHING = 50;
    public final static int[] POINTS_FOR_REMAINING_AMMO = {100, 250, 400};
    public final static int MAX_PLAYERS_QUANTITY = 6;
    public final static int SHOTS_FROM_BOMBARDMENT = 10;

}
