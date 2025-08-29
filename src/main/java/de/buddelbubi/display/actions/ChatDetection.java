package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.misc.Settings;

import java.awt.*;
import java.util.Set;

public class ChatDetection {

    public static boolean CHAT = false;

    private static boolean inChat() {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        Point chatbox = ScreenReader.calculateElementPos(0.3, 0.3);
        return mouse.x <= chatbox.x && mouse.y <= chatbox.y;
    }

    public static void handleClick() {

        if(!ScreenReader.isRobloxFront()) return;
        if(Settings.IN_ACTION) return;

        if(!Settings.ENABLED && !CHAT) return;

        if(inChat()) {
            CHAT = true;
            if(Settings.ENABLED) Settings.toggleEnable();
        } else {
            if(CHAT) {
                relaxChat();
            }
        }

    }

    public static void relaxChat() {
        CHAT = false;
        if(!Settings.ENABLED) Settings.toggleEnable();
    }

}
