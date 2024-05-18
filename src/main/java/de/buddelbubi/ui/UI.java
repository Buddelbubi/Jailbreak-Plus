package de.buddelbubi.ui;

import de.buddelbubi.display.ScreenReader;
import de.buddelbubi.misc.Settings;

import javax.swing.*;
import java.awt.*;

public class UI {

    public static JFrame STATUS = null;

    public static void buildStatus() {

        if(STATUS == null) {

            STATUS = new JFrame("STATUS");
            STATUS.setSize(Settings.STATUS_SIZE, Settings.STATUS_SIZE);
            STATUS.setUndecorated(true);
            STATUS.setLocation(0, ScreenReader.SCREEN.height-STATUS.getHeight());
            STATUS.setAlwaysOnTop(true);
            STATUS.getContentPane().setBackground(Color.GREEN);
            STATUS.setVisible(true);
            STATUS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

    }

}
