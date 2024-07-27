package de.buddelbubi;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.display.actions.CashReader;
import de.buddelbubi.display.robberies.Casino;
import de.buddelbubi.display.robberies.RobberyDetector;
import de.buddelbubi.input.KeyboardListener;
import de.buddelbubi.input.MouseListener;
import de.buddelbubi.ui.UI;

public class JailbreakPlus {

    private static final String VERSION = "r1"; //RELEASE #

    public static void main(String[] args) {

        System.out.println("Jailbreak " + VERSION + " Plus by Buddelbubi");
        ScreenReader.initRobot();
        UI.buildStatus();
        CashReader.init();
        //RobberyDetector.init();
        //Casino.init();
        new Thread(() -> {
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                throw new RuntimeException(e);
            }
            GlobalScreen.addNativeKeyListener(new KeyboardListener());
            GlobalScreen.addNativeMouseListener(new MouseListener());
        }).start();
    }

}