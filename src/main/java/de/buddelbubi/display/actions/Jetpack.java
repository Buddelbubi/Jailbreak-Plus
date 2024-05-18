package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.input.MouseListener;
import de.buddelbubi.misc.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Jetpack {
    private static final int TOLERANCE = 2;
    private static long lastJump = 0l;
    private static long lastTried = 0l;

    public static void pressedSpace() {

       if(Settings.AUTOMATIC_JETPACK_REFILL) {

           if(System.currentTimeMillis() - lastJump < 500) {

               if(System.currentTimeMillis() - lastTried > 1000) {

                   if(!MouseListener.RIGHT_IN_USE) {

                       lastTried = System.currentTimeMillis();

                       Point origin = MouseInfo.getPointerInfo().getLocation();
                       Point refill_button = ScreenReader.calculateElementPos(0.5, 0.7);
                       refill_button.y += 20 + TOLERANCE;
                       //if(!ScreenReader.awaitColor(refill_button, new Color(185,0,0))) return;
                       ScreenReader.moveMouse(refill_button);
                       ScreenReader.click();
                       Point refill_select = ScreenReader.calculateElementPos(0.5, 0.5);
                       refill_select.y += (170/2) - (26 - TOLERANCE*2);
                       if(!ScreenReader.awaitColor(refill_select, new Color(33,33,33))) return;
                       refill_select.x += -150 + 6 + TOLERANCE*2;
                       ScreenReader.moveMouse(refill_select);
                       ScreenReader.click();
                       if(!ScreenReader.awaitColor(refill_select, new Color(33,33,33), 5)) ScreenReader.moveMouse(origin);

                   }

               }

               lastJump = 0;

           } else lastJump = System.currentTimeMillis();

       }

    }

}
