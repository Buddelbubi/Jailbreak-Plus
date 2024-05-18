package de.buddelbubi.misc;

import de.buddelbubi.ui.UI;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Settings {

    public static boolean IN_ACTION = false;
    public static boolean ENABLED = true;
    public static boolean AUTOMATIC_JETPACK_REFILL = true;
    public static boolean AUTOMATIC_GARAGE = true;
    public static boolean AUTOMATIC_LOCK = true;
    public static int STATUS_SIZE = 30;
    public static int ZOOM_SCROLL_AMOUNT = 700;

    public static boolean toggleEnable() {

        ENABLED = !ENABLED;
        UI.STATUS.getContentPane().setBackground(ENABLED ? Color.GREEN : Color.RED);
        return ENABLED;

    }

}
