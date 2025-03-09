package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;

import java.awt.event.KeyEvent;

public class Weapon {

    public static void selectWeapon(int key) {
        if(ScreenReader.isRobloxFront() && !VehicleAutomatisation.isDriving()) {
            ScreenReader.getROBOT().keyPress(key);
            ScreenReader.getROBOT().keyRelease(key);
        }
    }

}
