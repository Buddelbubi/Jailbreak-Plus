package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class ItemSpam {

    public static boolean doSpam = false;
    public static int ITEM = -1;
    public static long lastCTRL = 0;
    public static boolean doCTRL = true; //CTRL Boosts increase speed by alot, but movement is more unpredictable!

    static int timebetween = 9;

    public static void toggle() {

        if(ITEM == -1) return;

        if(doSpam) {
            doSpam = false;
        } else {
            doSpam = true;

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
                            if(doCTRL) {
                                if (System.currentTimeMillis() - lastCTRL > 4900) {
                                    new Thread(new Runnable() {

                                        @Override
                                        public void run() {

                                            try {
                                                ScreenReader.getROBOT().keyPress(KeyEvent.VK_SPACE);
                                                TimeUnit.MILLISECONDS.sleep(100);
                                                ScreenReader.getROBOT().keyRelease(KeyEvent.VK_SPACE);
                                                ScreenReader.getROBOT().keyPress(KeyEvent.VK_CONTROL);
                                                ScreenReader.getROBOT().keyRelease(KeyEvent.VK_CONTROL);
                                                lastCTRL = System.currentTimeMillis();
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }).start();
                                }
                            }
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
