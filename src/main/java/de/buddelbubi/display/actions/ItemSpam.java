package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.misc.Settings;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class ItemSpam {

    public static boolean doSpam = false;
    public static int ITEM = -1;

    static int timebetween = 7;

    public static void toggle() {

        if(ITEM == -1) return;

        if(doSpam) {
            System.out.println("Disabled Itemspam");
            doSpam = false;
        } else {
            System.out.println("Enabled Itemspam");
            doSpam = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Settings.IN_ACTION = true;
                    while(doSpam) {
                        if(!Settings.ENABLED) {
                            doSpam = false;
                            continue;
                        }
                        try {
                            ScreenReader.getROBOT().keyPress(ITEM);
                            ScreenReader.getROBOT().keyRelease(ITEM);
                            TimeUnit.MILLISECONDS.sleep(timebetween);
                            ScreenReader.getROBOT().mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            ScreenReader.getROBOT().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            TimeUnit.MILLISECONDS.sleep(timebetween);
                            ScreenReader.getROBOT().keyPress(ITEM);
                            ScreenReader.getROBOT().keyRelease(ITEM);
                            TimeUnit.MILLISECONDS.sleep(timebetween);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Settings.IN_ACTION = false;
                }
            }) {
            }.start();
        }
    }
}
