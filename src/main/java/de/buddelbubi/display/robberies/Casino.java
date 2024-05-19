package de.buddelbubi.display.robberies;

import de.buddelbubi.display.ScreenReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Casino {

    private static int clicked = 0;
    private static long lastClick = 0;

    public static void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(RobberyDetector.getRobbery().equals("casino")) {
                        if (ScreenReader.isRobloxFront()) {
                            Point start = ScreenReader.calculateElementPos(0.25, 0.15);
                            Point end = ScreenReader.calculateElementPos(0.5, 0.7);
                            Rectangle captureRect = new Rectangle(start.x, start.y, end.x, end.y- 36);
                            BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
                            boolean found = false;
                            for (int x = 0; x < capture.getWidth(); x++) {
                                for (int y = 0; y < capture.getHeight(); y++) {
                                    if (new Color(capture.getRGB(x, y)).equals(new Color(0, 255, 1))) {

                                        long diff = System.currentTimeMillis() - lastClick;
                                        lastClick = System.currentTimeMillis();
                                        if (clicked < 3) {
                                            if (diff < 8000) {
                                                clicked++;
                                                ScreenReader.getROBOT().keyPress(KeyEvent.VK_E);
                                                ScreenReader.getROBOT().keyRelease(KeyEvent.VK_E);
                                            }
                                        } else clicked = 0;
                                    }
                                }
                            }
                        }
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

}
