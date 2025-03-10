package de.buddelbubi.input;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.display.actions.*;
import de.buddelbubi.display.robberies.RobberyDetector;
import de.buddelbubi.misc.KeyIdentifier;
import de.buddelbubi.misc.Settings;

import java.util.ArrayList;
import java.util.List;

public class KeyboardListener implements NativeKeyListener {

    public static final List<String> PRESSED_KEYS = new ArrayList<>();

    public KeyboardListener() {
        System.out.println("Loaded Keylistener");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

        int code = nativeKeyEvent.getKeyCode();
        String key = KeyIdentifier.getKey(code);
//        if(code >= 2 && code <= 10 && PRESSED_KEYS.contains("g") && !Settings.IN_ACTION) {
//            ItemSpam.ITEM = code + 47;
//            System.out.println("Locked in Item " + code);
//        }

        if(key == null) return;
        if(!PRESSED_KEYS.contains(key)) {
            PRESSED_KEYS.add(key);
        } else return;

        // Enable or disable Jailbreak Plus with y and x
        if(key.equals("y") || key.equals("x")) {
            if(PRESSED_KEYS.contains("y") && PRESSED_KEYS.contains("x")) {
                Settings.toggleEnable();
            }
        }

        if(!Settings.ENABLED) return;

        if(!ScreenReader.isRobloxFront()) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                switch (key) {
                    case "v":
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                VehicleAutomatisation.spawnFirstFavorite();
                            }
                        }).start();
                        break;
                    case "e":
                        if(!VehicleAutomatisation.isDriving()) {
                            VehicleAutomatisation.enteredVehicle();
                        }
                        break;
                    case "b":
                        AirdropGlitch.start();
                        break;
                    case "l":
                        VehicleAutomatisation.lockVehicle(true);
                        break;
                    case "h":
                        Optifine.startZoom();
                        break;
                    case "n":
                        //HackTheComputer.run();
                        break;
//                    ## They added a cooldown. Therefore, Item Spam is useless. If you have a usecase, please open an issue.
//                    case "g":
//                        if(!VehicleAutomatisation.isDriving()) {
//                            ItemSpam.toggle();
//                        }
//                        break;
                    case "enter":
                        ChatDetection.relaxChat();
                        break;
                }
            }
        }).start();

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        String key = KeyIdentifier.getKey(nativeKeyEvent.getKeyCode());
        if(key == null) return;
        if(key.equals("h")) Optifine.stopZoom();
        PRESSED_KEYS.remove(key);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        //Can be ignored
    }
}
