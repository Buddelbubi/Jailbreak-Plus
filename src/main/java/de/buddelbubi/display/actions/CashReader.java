package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.input.MouseListener;
import de.buddelbubi.misc.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CashReader {
    public static JFrame MONEY = null;
    public static JLabel LABEL = new JLabel();
    public static void updateMoney() {

        Point origin = MouseInfo.getPointerInfo().getLocation();

        Point money = ScreenReader.calculateElementPos(0, 0.6, 0);
        money.x += ScreenReader.SCREEN.height * 0.4 * 0.4 * 0.75;
        money.y -= money.x- money.x/3;
        ScreenReader.moveMouse(money);
        ScreenReader.click();
        int ui_y = (int) ((ScreenReader.SCREEN.height-36)*0.25f);
        int ui_x = (int) (ui_y*2.88f);
        Point p = new Point((ScreenReader.SCREEN.width - ui_x) / 2, (((ScreenReader.SCREEN.height - (ui_y + 36) ) /2) + 36));
        Point middle = new Point(p);
        middle.y += ui_x / 20;
        middle.x += ui_x/2;
        if(!ScreenReader.awaitColor(middle, new Color(175, 254, 88), 10)) return;
        p.x += ui_x - (ui_x*0.05f) - (ui_x/20) * 5;
        Rectangle captureRect = new Rectangle(p.x, p.y, (ui_x/20) * 5, ui_x/20);
        BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
        ScreenReader.moveMouse(money);
        ScreenReader.click();
        if(!ScreenReader.awaitColor(middle, new Color(175, 254, 88), 10));
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

                        if(MONEY == null && !MouseListener.RIGHT_IN_USE && !ScreenReader.isFirstPerson()) updateMoney();

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

                        try {
                            ImageIO.write(capture, "png", new File("modified_example.png"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if(gotMoney && !found && !MouseListener.RIGHT_IN_USE && !ScreenReader.isFirstPerson()) {
                            updateMoney();
                            gotMoney = false;
                        }
                        if(found) gotMoney = true;

                    }
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

}
