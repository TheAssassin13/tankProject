package com.example.tankproject;

import javafx.scene.paint.Color;


public class Constants {
    public final static int FPS = 60;
    public final static long FRAME_TIME = 1_000_000_000L / FPS;
    public final static int[] RESOLUTION_HEIGHT = {700, 720, 768};
    public final static int[] RESOLUTION_WIDTH = {1000, 1280, 1366};
    public static int WINDOWS_HEIGHT = RESOLUTION_HEIGHT[1];
    public static int WINDOWS_WIDTH = RESOLUTION_WIDTH[1];
    public final static int BUTTONS_PANEL_HEIGHT = 175;
    public static int CANVAS_HEIGHT = WINDOWS_HEIGHT - BUTTONS_PANEL_HEIGHT;
    public static int SEA_LEVEL = CANVAS_HEIGHT - 200;
    public static int TANKS_QUANTITY = 2;
    public final static double GRAVITY_FACTOR = 0.25;
    public final static double GRAVITY = 9.8 * Constants.GRAVITY_FACTOR;
    public final static int SHOT_TRAJECTORY_SIZE = 10;
    public final static double[] SHOT_HEIGHT = {24, 24, 48};
    public final static double[] SHOT_WIDTH = {25.4, 25.4, 50.8};
    public final static int TANK_SIZE = 30;
    public final static int BOX_SIZE = 20;
    public final static double SHOT_VELOCITY = 0.3;
    public final static Color TERRAIN_COLOR = Color.BLACK;
    public final static Color SHOT_COLOR = Color.BROWN;
    public final static Color BOX_COLOR = Color.DARKGOLDENROD;
    public final static Color TRAJECTORY_COLOR = Color.LIGHTGRAY;
    public final static Color[] TANK_COLORS = {Color.GREENYELLOW, Color.BLUE, Color.RED, Color.GRAY, Color.ORANGE, Color.BEIGE, Color.BROWN, Color.GREEN, Color.VIOLET, Color.PINK};
    public final static Color WIN_SCREEN_BACKGROUND_COLOR = Color.GRAY;
    public final static int[] AMMO_QUANTITY = {3,10,3}; // Light, medium and heavy
    public final static int[] AMMO_DAMAGE= {30,40,50}; // Light, medium and heavy
    public final static int TANK_HEALTH = 100;
    public final static int TERRAIN_MARGIN = 50;
}
