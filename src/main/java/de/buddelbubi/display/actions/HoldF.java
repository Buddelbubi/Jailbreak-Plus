package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.input.KeyboardListener;
import de.buddelbubi.misc.Settings;
import lombok.Getter;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class HoldF {

    @Getter
    private static long lastHit = 0l;

    public static void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(Settings.ENABLED && ScreenReader.isRobloxFront()) {
                        if(KeyboardListener.PRESSED_KEYS.contains("f")) {
                            try {
                                ScreenReader.getROBOT().keyPress(KeyEvent.VK_F);
                                lastHit = System.currentTimeMillis();
                                ScreenReader.getROBOT().keyRelease(KeyEvent.VK_F);
                                lastHit = System.currentTimeMillis();
                                TimeUnit.MILLISECONDS.sleep(510);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }).start();
    }

}
