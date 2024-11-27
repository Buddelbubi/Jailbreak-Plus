package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.input.MouseListener;
import de.buddelbubi.misc.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import static de.buddelbubi.display.ScreenReader.ROBLOX_IU_SHIFT;

public class CashReader {

    public static JFrame MONEY = null;
    public static JLabel LABEL = new JLabel();

    public static void updateMoney() {

        Point origin = MouseInfo.getPointerInfo().getLocation();
        Point money = ScreenReader.calculateElementPos(0, 0.6, 0);
        money.x += ScreenReader.SCREEN.height * 0.4 * 0.4 * 0.75;
        money.y -= money.x-money.x/3;
        ScreenReader.moveMouse(money);
        ScreenReader.click();
        int ui_y = (int) ((ScreenReader.SCREEN.height-46)*0.25f);
        int ui_x = (int) (ui_y*2.88f);
        Point p = new Point((ScreenReader.SCREEN.width - ui_x) / 2, (((ScreenReader.SCREEN.height - (ui_y + 44) ) /2) + ScreenReader.ROBLOX_IU_SHIFT));
        Point middle = new Point(p);
        middle.y += (ui_x / 20);
        middle.x += ui_x/2;
        System.out.println(ScreenReader.getColor(middle));
        if(!awaitGreen(middle, 10)) return;
        p.x += ui_x - (ui_x*0.05f) - (ui_x/20) * 5;
        Rectangle captureRect = new Rectangle(p.x, p.y, (ui_x/20) * 5, ui_x/20);
        BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
        while(awaitGreen(middle, 1)) {
            ScreenReader.moveMouse(money);
            ScreenReader.click();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ScreenReader.moveMouse(origin);
        BufferedImage image = new BufferedImage(capture.getWidth() , capture.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(capture.getRGB(x, y));
                image.setRGB(x, y, capture.getRGB(x, y));
                if(c.getRGB() < -1) {
                    image.setRGB(x, y, 0x00000000);
                } else image.setRGB(x, y, 0xFFFFFFFF);
            }
        }

        //Outline so its visible at day
        for(int x = 1; x < image.getWidth()-1; x++) {
            for(int y = 1; y < image.getHeight()-1; y++) {
                if(image.getRGB(x, y) == 0xFFFFFFFF){
                    for(int x1 = -1; x1 <= 1; x1++) {
                        for(int y1 = -1; y1 <= 1; y1++) {
                            if(image.getRGB(x + x1, y + y1) != 0xFFFFFFFF){
                                image.setRGB(x + x1, y+ y1,  0xFF000000);
                            }
                        }
                    }
                }
            }
        }
        LABEL.setSize(image.getWidth(), image.getHeight());
        LABEL.setIcon(new ImageIcon(image));
        display();
    }

    private static void display() {
        if(MONEY == null) {
            MONEY = new JFrame();
            MONEY.setSize(LABEL.getWidth(), LABEL.getHeight());
            MONEY.add(LABEL);
            MONEY.setLocation(ScreenReader.SCREEN.width - LABEL.getWidth(), -5);
            MONEY.setUndecorated(true);
            MONEY.setAlwaysOnTop(true);
            MONEY.setFocusableWindowState(false);
            MONEY.setAutoRequestFocus(false);
            MONEY.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            MONEY.setBackground(new Color(0,0,0,0));
            MONEY.setVisible(true);
        }
    }

    public static void init() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean gotMoney = false;

                while (true) {

                    if(ScreenReader.isRobloxFront() && Settings.ENABLED) {

                        if(MONEY == null && !MouseListener.RIGHT_IN_USE && !ScreenReader.isFirstPerson() && !Settings.IN_ACTION) {
                            Settings.IN_ACTION = true;
                            updateMoney();
                            Settings.IN_ACTION = false;
                        }

                        Rectangle captureRect = new Rectangle(ScreenReader.SCREEN.width/2, (int) (ScreenReader.SCREEN.height*0.6), 200, 100);
                        BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
                        boolean found = false;
                        for(int x = 0; x < capture.getWidth(); x++) {
                            for(int y = 0; y < capture.getHeight(); y++) {
                                if(capture.getRGB(x, y) == -12271011) {
                                    found = true;
                                    break;
                                }
                            }
                        }

                        if(gotMoney && !found && !MouseListener.RIGHT_IN_USE && !ScreenReader.isFirstPerson()) {
                            Settings.IN_ACTION = true;
                            updateMoney();
                            Settings.IN_ACTION = false;
                            gotMoney = false;
                        }
                        if(found) gotMoney = true;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public static boolean awaitGreen(Point p, int tries) {
        for(int i = 0; i < tries; i++) {
            Color color = ScreenReader.getColor(p);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            //Those values were taken from a screenshot of the money UI in 1440p
            if(r > 160 && r < 180 && g > 240 && b < 95 && b > 82) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(Settings.SCREEN_WAIT_MILLIS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
