package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.misc.Settings;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AdminCommands {

    private static final int CIRCUMFLEX = 0x2B;

    public static boolean execute(String key) {
        try {
            Settings.IN_ACTION = true;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String[] commands = switch (key) {
                case "num_1" -> new String[] {
                        "trainspeed 500"
                };
                case "num_2" -> new String[] {
                        "trainspeed 0"
                };
                case "num_3" -> new String[] {
                        "trainspeed 50"
                };
                case "num_6" -> new String[] {
                        "time 7",
                        "rain off"
                };
                case "num_7" -> new String[]{
                        "give . AK47",
                        "give . Sword",
                        "give . ForcefieldLauncher",
                        "give . Flintlock"
                };
                case "num_8" -> new String[] {
                        "giveCinematic *",
                        "drowning off",
                        "pits off",
                        "setEquipTime sword 0"
                };
                case "num_9" -> new String[] {
                        "trainspeed 999999"
                };
                default -> new String[]{};
            };
            if(commands.length == 0) {
                Settings.IN_ACTION = false;
                return false;
            }
            ScreenReader.getROBOT().keyPress(KeyEvent.VK_DEAD_CIRCUMFLEX);
            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_DEAD_CIRCUMFLEX);
            TimeUnit.MILLISECONDS.sleep(5);
            ScreenReader.getROBOT().keyPress(KeyEvent.VK_BACK_SPACE);
            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_BACK_SPACE);
            TimeUnit.MILLISECONDS.sleep(5);
            ScreenReader.getROBOT().keyPress(KeyEvent.VK_BACK_SPACE);
            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_BACK_SPACE);
            TimeUnit.MILLISECONDS.sleep(5);
            ScreenReader.getROBOT().keyPress(KeyEvent.VK_BACK_SPACE);
            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_BACK_SPACE);
            for(String command : commands) {
                StringSelection stringSelection = new StringSelection(command);
                clipboard.setContents(stringSelection, null);
                TimeUnit.MILLISECONDS.sleep(15);
                ScreenReader.getROBOT().keyPress(KeyEvent.VK_CONTROL);
                ScreenReader.getROBOT().keyPress(KeyEvent.VK_V);
                TimeUnit.MILLISECONDS.sleep(10);
                ScreenReader.getROBOT().keyRelease(KeyEvent.VK_CONTROL);
                ScreenReader.getROBOT().keyRelease(KeyEvent.VK_V);
                TimeUnit.MILLISECONDS.sleep(10);
                ScreenReader.getROBOT().keyPress(KeyEvent.VK_ENTER);
                ScreenReader.getROBOT().keyRelease(KeyEvent.VK_ENTER);
                TimeUnit.MILLISECONDS.sleep(20);
            }
            ScreenReader.getROBOT().keyPress(KeyEvent.VK_DEAD_CIRCUMFLEX);
            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_DEAD_CIRCUMFLEX);
        } catch (Exception ignore) {
        }
        Settings.IN_ACTION = false;
        return true;
    }
}
