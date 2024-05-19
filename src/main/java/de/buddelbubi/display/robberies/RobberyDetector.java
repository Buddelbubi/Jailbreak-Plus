package de.buddelbubi.display.robberies;

import de.buddelbubi.display.ScreenReader;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RobberyDetector {

    @Getter
    private static String robbery = "none";

    private static final Map<String, int[]> robberies = Map.ofEntries(
            Map.entry("city_bank", new int[]{26, 20, 53}),
            Map.entry("jewelry", new int[]{24, 33, 41}),
            Map.entry("museum", new int[]{32, 41, 25}),
            Map.entry("tomb", new int[]{54, 30, 15}),
            Map.entry("casino", new int[]{50, 24, 24}),
            Map.entry("crater_bank", new int[]{10, 14, 74}),
            Map.entry("oil_rig", new int[]{4, 0, 94}),
            Map.entry("power_plant", new int[]{17, 32, 50}),
            Map.entry("ceo", new int[]{5, 24, 69}),
            Map.entry("prison", new int[]{25, 64, 10})
    );
    private static final float[] filter = {255/97f, 255/139.84f, 255/185f};

    private static String readRobbery() {

        Point map_upper_corner = ScreenReader.calculateElementPos(0, 0.6, 0);
        int sidepanel_hight = (int) (ScreenReader.SCREEN.height * 0.4);
        int sidepanel_width = (int) (sidepanel_hight * 0.4);
        int minimap_size = (int) (sidepanel_width*0.9)-10;
        map_upper_corner.x = (int) (sidepanel_width*0.05)+5;
        map_upper_corner.y += (sidepanel_hight/2) - minimap_size + 5;
        Rectangle captureRect = new Rectangle(map_upper_corner.x, map_upper_corner.y, minimap_size, minimap_size-10);
        BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
        int pixels = capture.getHeight()*capture.getWidth();

        int r = 0, g = 0, b = 0;

        boolean dark = false;

        int below100 = 0;
        for(int x = 0; x < capture.getWidth(); x++) {
            for(int y = 0; y < capture.getHeight(); y++) {
                Color c = new Color(capture.getRGB(x, y));
                if(c.getRed() < 100 && c.getGreen() < 100 && c.getBlue() < 100) below100++;
            }
        }
        below100 = (int) ((below100 / (double) pixels) * 100);
        if(below100 > 60) dark = true;
        for(int x = 0; x < capture.getWidth(); x++) {
            for(int y = 0; y < capture.getHeight(); y++) {
                Color c = new Color(capture.getRGB(x, y));
                int rc = (int) (c.getRed() * (dark ? filter[0] : 1));
                int gc = (int) (c.getGreen() * (dark ? filter[1] : 1));
                int bc = (int) (c.getBlue() * (dark ? filter[2] : 1));
                if(rc > gc){
                    if(rc > bc) {
                        r++;
                    } else b++;
                } else {
                    if(gc > bc) {
                        g++;
                    } else b++;
                }
            }
        }
        r = (int) ((r / (double) pixels) * 100);
        g = (int) ((g / (double) pixels) * 100);
        b = (int) ((b / (double) pixels) * 100);
        System.out.println(r + " " + g + " " + b);
        String robbery = "none";
        int bestScore = Integer.MAX_VALUE;
        for(String s : robberies.keySet()) {
            int[] p = robberies.get(s);
            int score = Math.abs(p[0] - r) + Math.abs(p[1] - g) + Math.abs(p[2] - b);
            if(score < bestScore) {
                bestScore = score;
                robbery = s;
            }
        }
        System.out.println(robbery + " " + bestScore + " " + dark + " " + below100);
        return robbery;
    }

    public static void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    robbery = readRobbery();
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

}
