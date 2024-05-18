package de.buddelbubi;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.display.actions.CashReader;
import de.buddelbubi.input.KeyboardListener;
import de.buddelbubi.input.MouseListener;
import de.buddelbubi.ui.UI;

public class JailbreakPlus {

    public static void main(String[] args) {

        System.out.println("Jailbreak Plus by Buddelbubi");
        ScreenReader.initRobot();
        UI.buildStatus();
        CashReader.init();
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
        GlobalScreen.addNativeKeyListener(new KeyboardListener());
        GlobalScreen.addNativeMouseListener(new MouseListener());


    }

}