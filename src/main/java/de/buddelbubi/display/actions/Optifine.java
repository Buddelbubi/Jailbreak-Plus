package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.misc.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class Optifine {

    private static boolean scrolling = false;
    private static void toggleZoom() {
        ScreenReader.getROBOT().keyPress(KeyEvent.VK_SHIFT);
        ScreenReader.getROBOT().keyPress(KeyEvent.VK_P);
        ScreenReader.getROBOT().keyRelease(KeyEvent.VK_SHIFT);
        ScreenReader.getROBOT().keyRelease(KeyEvent.VK_P);
    }

    public static void stopZoom() {
        if(scrolling) {
            scrolling = false;
            toggleZoom();
        }
    }
    public static void startZoom() {
        if(ScreenReader.isFirstPerson() && ScreenReader.isRobloxFront()) {
            scrolling = true;

            toggleZoom();
            Point garage = ScreenReader.calculateElementPos(0, 0.6, 0);
            garage.x += ScreenReader.SCREEN.height*0.4*0.4*0.75;
            //if(!ScreenReader.awaitColor(garage, new Color(254, 255, 255), 20)) return;
            for(int i = 0; i < Settings.ZOOM_SCROLL_AMOUNT; i ++) {
                if(!scrolling) break;
                ScreenReader.getROBOT().mouseWheel(-1);
            }
        }
    }

}
