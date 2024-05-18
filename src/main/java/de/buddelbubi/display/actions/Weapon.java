package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;

import java.awt.event.KeyEvent;

public class Weapon {

    public static void selectWeapon() {
        if(ScreenReader.isRobloxFront() && !VehicleAutomatisation.isDriving()) {
            ScreenReader.getROBOT().keyPress(KeyEvent.VK_1);
            ScreenReader.getROBOT().keyRelease(KeyEvent.VK_1);
        }
    }

}
