package com.example.tankproject;

import javafx.scene.paint.Color;


public class Constants {
    public static int FPS = 60;
    public static long FRAME_TIME = 1_000_000_000L / FPS;
    public static int WINDOWS_HEIGHT = 720;
    public static int WINDOWS_WIDTH = 1280;
    public static int BUTTONS_PANEL_HEIGHT = 160;
    public static int CANVAS_HEIGHT = WINDOWS_HEIGHT - BUTTONS_PANEL_HEIGHT;
    public static int SEA_LEVEL = CANVAS_HEIGHT - 200;
    public static int TANKS_QUANTITY = 2;
    public static double GRAVITY_FACTOR = 0.25;
    public static double GRAVITY = 9.8 * Constants.GRAVITY_FACTOR;
    public static int SHOT_SIZE = 10;
    public static int TANK_SIZE = 30;
    public static double SHOT_VELOCITY = 0.3;
    public static Color TERRAIN_COLOR = Color.BLACK;
    public static Color SHOT_COLOR = Color.BROWN;
    public static Color TRAJECTORY_COLOR = Color.LIGHTGRAY;
    public static Color[] TANK_COLORS = {Color.GREENYELLOW, Color.BLUE, Color.RED, Color.GRAY, Color.ORANGE, Color.BEIGE, Color.BROWN, Color.GREEN, Color.VIOLET, Color.PINK};
    public static Color WIN_SCREEN_BACKGROUND_COLOR = Color.GRAY;
    public static int[] AMMO_QUANTITY = {3,10,3}; // Light, medium and heavy
    public static int[] AMMO_DAMAGE= {30,40,50}; // Light, medium and heavy
}
