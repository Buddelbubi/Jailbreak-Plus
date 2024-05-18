package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.input.KeyboardListener;
import de.buddelbubi.input.MouseListener;
import de.buddelbubi.misc.Settings;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VehicleAutomatisation {

    //Spawn your first favorited car
    public static void spawnFirstFavorite() {

        if (isDriving() && !MouseListener.RIGHT_IN_USE) {
            applyGarage();
            return;
        }

        //UI Element not present in Jailbreak Uncopylocked!! ): Had to make this tradeoff...
        //If someone can extract that data from the jb archive, that would be cool :)
        if(!MouseListener.RIGHT_IN_USE) {
            if (ScreenReader.SCREEN.height < 1080) return;
            Settings.IN_ACTION = true;
            Point origin = MouseInfo.getPointerInfo().getLocation();

            Point garage = ScreenReader.calculateElementPos(0, 0.6, 0);
            garage.x += ScreenReader.SCREEN.height * 0.4 * 0.4 * 0.75;
            ScreenReader.moveMouse(garage);
            ScreenReader.click();

            //Not 100% accurate, had to calculate on pixels. Tested with 1440p and 1080p
            Point favorite_cars = ScreenReader.calculateElementPos(0.63, 0.11, 0);
            if (!ScreenReader.awaitColor(favorite_cars, new Color(179, 179, 179), 50)) return;
            ScreenReader.moveMouse(favorite_cars);
            ScreenReader.click();
            ScreenReader.moveMouse(garage); //Move it away so cursor is not on the star
            Point first_favorite = ScreenReader.calculateElementPos(0.3, 0.2, 0);
            if (!ScreenReader.awaitColor(favorite_cars, new Color(255, 251, 0), 50)) return;
            ScreenReader.moveMouse(first_favorite);
            ScreenReader.click();
            enteredVehicle();
            ScreenReader.moveMouse(origin);
            if(KeyboardListener.PRESSED_KEYS.contains("w")) {
                ScreenReader.getROBOT().keyPress(KeyEvent.VK_W);
            }
            Settings.IN_ACTION = false;
        }

    }

    public static boolean ENTERED = false;
    public static void enteredVehicle() {
        ENTERED = false;
        if(!MouseListener.RIGHT_IN_USE) {
            System.out.println("Entered Vehicle");
            if (Settings.AUTOMATIC_GARAGE) {
                for(int i = 0; i < 15; i++) {
                    if(isDriving()) {
                        applyGarage();
                        break;
                    } else {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } else ENTERED = true;

    }

    public static void applyGarage() {
        Settings.IN_ACTION = true;
        System.out.println("Applying Garage");
        Point origin = MouseInfo.getPointerInfo().getLocation();

        Point garage = ScreenReader.calculateElementPos(0, 0.6, 0);
        garage.x += ScreenReader.SCREEN.height*0.4*0.4*0.75;
        ScreenReader.moveMouse(garage);
        ScreenReader.click();
        Point favorite_cars = ScreenReader.calculateElementPos(0.63, 0.11, 0);
        if(!ScreenReader.awaitColor(favorite_cars, new Color(179, 179, 179))) return;
        ScreenReader.moveMouse(garage);
        ScreenReader.click();

        if(ScreenReader.awaitColorGone(favorite_cars, 10)) {
            ScreenReader.moveMouse(origin);
        }
        Settings.IN_ACTION = false;

    }

    public static boolean isDriving() {

        Point lock = ScreenReader.calculateElementPos(0.374, 0.9);
        Color color = null;
       // Color round = new Color(252, 40,47);
        for(int i = 0; i < 5; i++) {
            Color round = ScreenReader.getROBOT().getPixelColor(lock.x, lock.y + i);
            if(color == null) {
                color = round;
            } else if(!color.equals(round)) {
                return false;
            }
        }
        System.out.println("Vehicle detected with Color: " + color);
        return true;
    }


}
