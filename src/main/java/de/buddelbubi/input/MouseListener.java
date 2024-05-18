package de.buddelbubi.input;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.display.actions.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MouseListener implements NativeMouseListener {

    public static boolean RIGHT_IN_USE = false;
    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {

    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        System.out.println(nativeMouseEvent.getButton());
        if(nativeMouseEvent.getButton() == 2) {
            RIGHT_IN_USE = true;
        } else if (nativeMouseEvent.getButton() == 1) {
            ChatDetection.handleClick();
        } else if(nativeMouseEvent.getButton() == 4) {
            Optifine.startZoom();
        }
        if(nativeMouseEvent.getButton() == 5) {
            Weapon.selectWeapon();
        }

    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        if(nativeMouseEvent.getButton() == 2) {
            RIGHT_IN_USE = false;
            if(VehicleAutomatisation.isDriving() && VehicleAutomatisation.ENTERED) {
                VehicleAutomatisation.enteredVehicle();
            }
        } else if(nativeMouseEvent.getButton() == 4) {
            Optifine.stopZoom();
        }
    }

    private static boolean OVERWRITE_MOUSE = false;

    public static boolean overwriteMouse() {
        if(RIGHT_IN_USE) {
            if(OVERWRITE_MOUSE) {
                OVERWRITE_MOUSE = false;
                System.out.println("Mouse is locked");
                ScreenReader.getROBOT().mousePress(InputEvent.BUTTON3_DOWN_MASK);
            } else {
                OVERWRITE_MOUSE = true;
                System.out.println("Mouse is unlocked");
                ScreenReader.getROBOT().mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            }
            return true;
        }
        return false;
    }


}
