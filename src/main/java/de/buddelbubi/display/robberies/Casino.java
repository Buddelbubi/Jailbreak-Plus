package de.buddelbubi.display.robberies;

import de.buddelbubi.display.ScreenReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static de.buddelbubi.display.ScreenReader.ROBLOX_IU_SHIFT;

public class Casino {

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
                            Rectangle captureRect = new Rectangle(start.x, start.y, end.x, end.y-ROBLOX_IU_SHIFT);
                            BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
                            for (int x = 0; x < capture.getWidth(); x++) {
                                for (int y = 0; y < capture.getHeight(); y++) {
                                    Color color = new Color(capture.getRGB(x, y));
                                    if (color.equals(new Color(0, 255, 1))) {
                                        long diff = System.currentTimeMillis() - lastClick;
                                        lastClick = System.currentTimeMillis();
                                        if (diff < 500) {
                                            ScreenReader.getROBOT().keyPress(KeyEvent.VK_E);
                                            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_E);
                                            break;
                                        }
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
