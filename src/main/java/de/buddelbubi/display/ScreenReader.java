package de.buddelbubi.display;

import lombok.Getter;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ScreenReader {

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
        return calculateElementPos(x, y, 36);
    }

    public static boolean isRobloxFront() {
        return getColor(new Point(27, 11)).equals(Color.WHITE);
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
            //System.out.println("Color: " + color  +"  "  +wanted);
            for(Color wanted : wantedColors) {
                if(color.equals(wanted)) return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
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
                TimeUnit.MILLISECONDS.sleep(10);
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
