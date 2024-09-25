package de.buddelbubi.misc;

import de.buddelbubi.ui.UI;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Settings {

    //Do not change this. This is a switch to block new actions if another action is running.
    public static boolean IN_ACTION = false;

    public static boolean ENABLED = true;
    public static boolean AUTOMATIC_GARAGE = true;
    public static boolean AUTOMATIC_LOCK = true;
    public static int STATUS_SIZE = 30;
    public static int ZOOM_SCROLL_AMOUNT = 700;

    public static int SCREEN_WAIT_MILLIS = 10; //Increasing will help performance. Feel free to adjust to your hardware.

    public static boolean toggleEnable() {
        ENABLED = !ENABLED;
        UI.STATUS.getContentPane().setBackground(ENABLED ? Color.GREEN : Color.RED);
        return ENABLED;
    }

}
