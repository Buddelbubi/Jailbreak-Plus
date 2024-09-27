package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.misc.Settings;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class NearbyCopDetector {

    public static JFrame NOTIFICATION = null;
    public static JLabel LABEL = new JLabel();

    private static Long lastCop = System.currentTimeMillis();

    public static void init() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if(Settings.ENABLED && ScreenReader.isRobloxFront() && Settings.NEARBY_COP_DETECTOR) {

                        Point map_upper_corner = ScreenReader.calculateElementPos(0, 0.6, 0);
                        int sidepanel_hight = (int) (ScreenReader.SCREEN.height * 0.4);
                        int sidepanel_width = (int) (sidepanel_hight * 0.4);
                        int minimap_size = (int) (sidepanel_width*0.9);
                        map_upper_corner.x = (int) (sidepanel_width*0.05);
                        map_upper_corner.y += (sidepanel_hight/2) - minimap_size;
                        Rectangle captureRect = new Rectangle(map_upper_corner.x, map_upper_corner.y, minimap_size, minimap_size);
                        BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
                        int mid = minimap_size / 2;
                        Point middle = new Point(mid, mid);
                        int lowestDistance = Integer.MAX_VALUE;
                        if(new Color(capture.getRGB(mid, mid)).getBlue() < 190) {
                            for (int x = 0; x < capture.getWidth(); x++) {
                                for (int y = 0; y < capture.getHeight(); y++) {
                                    Color color = new Color(capture.getRGB(x, y));
                                    if (color.equals(new Color(42, 204, 255))) {
                                        Point point = new Point(x, y);
                                        int distance = (int) point.distance(middle);
                                        if (distance < lowestDistance) lowestDistance = distance;
                                        break;
                                    }
                                }
                            }
                        }
                        if(lowestDistance != Integer.MAX_VALUE) {
                            int meters = (int) (((double) lowestDistance / ((double) minimap_size/2f)) * 100);
                            String html = "<html><center><p style=\"color:#3895D0;\">Police is nearby<br/><p style=\"color:#FC282F;\">Distance: " + meters + "%</center></html>";
                            int width = (int) (ScreenReader.SCREEN.width*0.4), height = (int) (ScreenReader.SCREEN.height*0.13);

                            BufferedImage genImage = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                    .getDefaultScreenDevice().getDefaultConfiguration()
                                    .createCompatibleImage(width, height);

                            Graphics graphics = genImage.createGraphics();

                            JEditorPane jep = new JEditorPane("text/html", html);
                            jep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
                            jep.setFont(new Font("Arial Black",0 , height / 5));
                            jep.setSize(width, height);
                            jep.print(graphics);

                            BufferedImage image = new BufferedImage(genImage.getWidth() , genImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

                            for(int x = 0; x < image.getWidth(); x++) {
                                for(int y = 0; y < image.getHeight(); y++) {
                                    Color c = new Color(genImage.getRGB(x, y));
                                    if(c.equals(Color.WHITE)) {
                                        image.setRGB(x, y, 0x00000000);
                                    } else image.setRGB(x,y, genImage.getRGB(x,y));
                                }
                            }

                            int r = 1;
                            for(int x = 0; x < image.getWidth(); x++) {
                                for(int y = 0; y < image.getHeight(); y++) {
                                    Color c = new Color(genImage.getRGB(x, y));
                                    if(c.getRGB()!=0) {
                                        for(int x1 = -r; x1 <= r; x1++) {
                                            for(int y1 = -r; y1 <= r; y1++) {
                                                int newx = x+x1, newy = y+y1;
                                                if(newx > 0 && newx < genImage.getWidth() && newy > 0 && newy < genImage.getHeight()) {
                                                    Color c2 = new Color(genImage.getRGB(newx, newy));
                                                    if(c2.getRGB() == -1 && c.getRGB() != 0xFFFFFFFF) {
                                                        image.setRGB(newx, newy, 0xFFFFFFFF);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            //Outline so its visible at day


                            LABEL.setIcon(new ImageIcon(image));
                            LABEL.setSize(width, height);
                            display();
                            lastCop = System.currentTimeMillis();
                        } else {
                            if(System.currentTimeMillis() - lastCop > 3000) {
                                destroy();
                            }
                        }
                    } else destroy();
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    private static void display() {
        if(NOTIFICATION == null) {
            NOTIFICATION = new JFrame();
            NOTIFICATION.setSize(LABEL.getWidth(), LABEL.getHeight());
            NOTIFICATION.add(LABEL);
            NOTIFICATION.setLocation(ScreenReader.SCREEN.width/2 - LABEL.getWidth()/2, ScreenReader.SCREEN.height/5);
            NOTIFICATION.setUndecorated(true);
            NOTIFICATION.setAlwaysOnTop(true);
            NOTIFICATION.setFocusableWindowState(false);
            NOTIFICATION.setAutoRequestFocus(false);
            NOTIFICATION.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            NOTIFICATION.setBackground(new Color(0,0,0,0));
            NOTIFICATION.setVisible(true);
        }
    }

    private static void destroy() {
        if(NOTIFICATION != null) {
            NOTIFICATION.dispatchEvent(new WindowEvent(NOTIFICATION, WindowEvent.WINDOW_CLOSING));
            NOTIFICATION = null;
        }
    }
}
