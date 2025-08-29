package de.buddelbubi.display;

import com.github.kwhat.jnativehook.GlobalScreen;
import de.buddelbubi.misc.Settings;
import lombok.Getter;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ScreenReader {

    public static final int ROBLOX_IU_SHIFT = 48;
    public static final Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    @Getter
    private static Robot ROBOT;

    public static void initRobot() {
        try {
            ROBOT = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static Point calculateElementPos(double x, double y, int offset) {
        return new Point((int) (SCREEN.width * x),  (int)((SCREEN.height-offset) * y) + offset);
    }

    public static Point calculateElementPos(double x, double y) {
        return calculateElementPos(x, y, ROBLOX_IU_SHIFT);
    }

    public static boolean isRobloxFront() {
        return getColor(new Point(36, 26)).equals(Color.decode("#F7F7F8"));
    }

    private static Point lastMoved = null;
    public static void moveMouse(Point p) {
        lastMoved = p;
        //have to move twice because one jump does not trigger the hover of a clickable UI element
        for(int i = 2; i >= 0; i--) {
            getROBOT().mouseMove(p.x, p.y+i);
            if(i != 0) {
                try {
                    Thread.sleep(3);
                }catch (Exception e) {}
            }
        }
    }
    public static void click() {
        Point mouse = MouseInfo.getPointerInfo().getLocation();

        if(mouse.distance(lastMoved) > 3) {
            getROBOT().mouseMove(lastMoved.x, lastMoved.y);
        }
        getROBOT().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        getROBOT().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static Color getColor(Point p) {
        return ScreenReader.getROBOT().getPixelColor(p.x, p.y);
    }

    public static boolean awaitColor(Point p, Color... wanted) {
        return awaitColor(p, wanted, 25);
    }

    public static boolean awaitColor(Point p, Color wantedColors, int tries) {
        return awaitColor(p, new Color[] {wantedColors}, tries);
    }

    public static boolean awaitColor(Point p, Color[] wantedColors, int tries) {
        for(int i = 0; i < tries; i++) {
            Color color = getColor(p);
            System.out.println("Color " + i + ": " + String.format("\033[38;2;%d;%d;%dm", color.getRed(), color.getGreen(), color.getBlue()) + " " + color + "\033[0m" );
            for(Color wanted : wantedColors) {
                if(color.equals(wanted)) return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(Settings.SCREEN_WAIT_MILLIS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean awaitColorGone(Point p, int tries) {
        Color wanted = getColor(p);
        for(int i = 0; i < tries; i++) {
            Color color = getColor(p);
            if(!color.equals(wanted)) return true;
            try {
                TimeUnit.MILLISECONDS.sleep(Settings.SCREEN_WAIT_MILLIS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isFirstPerson() {
        Point middle = ScreenReader.calculateElementPos(0.5, 0.5, 0);
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        return middle.distance(mouse) == 0;
    }

}
