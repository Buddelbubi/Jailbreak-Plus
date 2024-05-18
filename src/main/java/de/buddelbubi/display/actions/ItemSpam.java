package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class ItemSpam {

    public static boolean doSpam = false;
    public static int ITEM = KeyEvent.VK_9;

    static int timebetween = 15;

    public static void toggle() {

        if(doSpam) {
            doSpam = false;
        } else {
            doSpam = true;
            ScreenReader.getROBOT().keyPress(ITEM);
            ScreenReader.getROBOT().keyRelease(ITEM);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(doSpam) {

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
                }
            }) {

            }.start();
        }

    }

}
