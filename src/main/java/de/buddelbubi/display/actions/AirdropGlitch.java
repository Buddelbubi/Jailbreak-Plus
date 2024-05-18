package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.misc.Settings;
import de.buddelbubi.ui.UI;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class AirdropGlitch {

    private static int time = 50;
    public static void start() {

        time = 50;

        Color bar = new Color(240, 243, 249);
        if(!ScreenReader.getROBOT().getPixelColor(0, 0).equals(bar)) {
            ScreenReader.getROBOT().keyPress(KeyEvent.VK_F11);
            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_F11);
        }
        ScreenReader.getROBOT().keyPress(KeyEvent.VK_E);
        Point point = new Point(0,0);
        if(!ScreenReader.awaitColor(point, bar, 10)) return;
        ScreenReader.moveMouse(point);
        ScreenReader.getROBOT().mousePress(InputEvent.BUTTON3_DOWN_MASK);
        try {
            while(time >= 0) {
                if(Settings.ENABLED) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    time--;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ScreenReader.getROBOT().mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        ScreenReader.getROBOT().keyPress(KeyEvent.VK_F11);
        ScreenReader.getROBOT().keyRelease(KeyEvent.VK_F11);
        if(Settings.ENABLED) {
            try {
                TimeUnit.MILLISECONDS.sleep(1010);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ScreenReader.getROBOT().keyRelease(KeyEvent.VK_E);

    }

}
