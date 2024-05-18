package de.buddelbubi.display.robberies;

import de.buddelbubi.display.ScreenReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RobberyDetector {

    private static int[] museum = new int[]{3553635, 4244434, 2980521};
    private static int[] crater_bank = new int[]{2838620, 3729065, 4087675};
    private static int[] casino = new int[]{5262410, 4645206, 3418866};
    public static int[] getRobberyIdentity() {

        Point map_upper_corner = ScreenReader.calculateElementPos(0, 0.6, 0);
        int sidepanel_hight = (int) (ScreenReader.SCREEN.height * 0.4);
        int sidepanel_width = (int) (sidepanel_hight * 0.4);
        int minimap_size = (int) (sidepanel_width*0.9)-10;
        map_upper_corner.x = (int) (sidepanel_width*0.05)+5;
        map_upper_corner.y += (sidepanel_hight/2) - minimap_size + 5;
        Rectangle captureRect = new Rectangle(map_upper_corner.x, map_upper_corner.y, minimap_size, minimap_size-10);
        BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);

        int r = 0, g = 0, b = 0;

        for(int x = 0; x < capture.getWidth(); x++) {
            for(int y = 0; y < capture.getHeight(); y++) {
                Color c = new Color(capture.getRGB(x, y));
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        int pixels = capture.getHeight()*capture.getWidth();
        System.out.println(r + " " + g + " " + b);
        return new int[]{r,g,b};
    }

}
