package de.buddelbubi.display.actions;

import de.buddelbubi.display.ScreenReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HackTheComputer {

    private static Color[][] field;
    private static HashMap<Color, Point> start = new HashMap<>();
    private static  HashMap<Color, Point> finish = new HashMap<>();

    public static void run() {

        readField();

    }

    private static void readField() {

        start.clear();
        finish.clear();
        Point upper_corner = ScreenReader.calculateElementPos(0.35, 0.2);
        upper_corner.y -= 50;
        int sizepx = ScreenReader.calculateElementPos(0.3, 0.3).x;
        Point lower_corner = new Point(upper_corner.x + sizepx, upper_corner.y + sizepx);
        Rectangle captureRect = new Rectangle(upper_corner.x, upper_corner.y, sizepx, sizepx);
        BufferedImage capture = ScreenReader.getROBOT().createScreenCapture(captureRect);
        Color background = new Color(10, 10, 10);
        Color unused = new Color(25, 25, 25);
        // now check if its a 7x7 grid. If not, its a 5x5 grid



        int gridsize = 7;
        for (int i = 0; i <= 6; i++) {
            Color c = background;
            shift:
            for (int o = -3; o <= 3; o++) {
                Color color = new Color(capture.getRGB((sizepx / 2) + o, (sizepx / (gridsize * 2)) + (i * (sizepx / gridsize))));
                if (!color.equals(background)) {
                    c = color;
                    break shift;
                }
            }
            if (c.equals(background)) {
                gridsize = 5;
                break;
            }
        }
        field = new Color[gridsize][gridsize];
        for (int x = 0; x < gridsize; x++) {
            for (int y = 0; y < gridsize; y++) {
                Color c = unused;
                int highest = 0;
                ////DOES NOT WORK YET
                in:
                for (int o = -5; o <= 5; o++) {
                    Color color = new Color(capture.getRGB((sizepx / (gridsize * 2)) + (x * (sizepx / gridsize)) + o, (sizepx / (gridsize * 2)) + (y * (sizepx / gridsize))));
                    int var = color.getRed() + color.getGreen() + color.getBlue();
                    if(var <= 75 || color.equals(background) || color.equals(unused)) continue in;
                    if(var > highest) {
                        highest = var;
                        c = color;
                    }
                }
                field[x][y] = c.equals(unused) ? null : c;
                if(!c.equals(unused)) {
                    Point cur = new Point(x,y);
                    if(start.containsKey(c)) {
                        System.out.println("Finish: " +c + " " + cur + " -> " + start.get(c));
                        finish.put(c,cur);
                    } else {
                        System.out.println("Start: " +c + " " + cur);
                        start.put(c, cur);
                    }

                    for(int i = -10; i <=10; i++) {
                        for(int b = -10; b <=10; b++) {
                            capture.setRGB((sizepx / (gridsize * 2)) + (x * (sizepx / gridsize)) + i, (sizepx / (gridsize * 2)) + (y * (sizepx / gridsize)) +b, c.getRGB());
                        }
                    }
                }
            }
        }

        List<Color> done = new ArrayList<>();
        HashMap<Color, List<Point>> distances = new HashMap<>();
        for(Color c : start.keySet()) {
            List<Point> path = calculateShortestPath(c);
            System.out.println(path.size() + " PATHH");
            for(Point p : path) {
                System.out.println(p);
                for(int i = -10; i <=10; i++) {
                    for(int b = -10; b <=10; b++) {
                        capture.setRGB((sizepx / (gridsize * 2)) + (p.x * (sizepx / gridsize)) + i, (sizepx / (gridsize * 2)) + (p.y * (sizepx / gridsize)) +b, c.getRGB());
                    }
                }

            }
        }

        for (int x = 0; x < gridsize; x++) {
            for (int y = 0; y < gridsize; y++) {
                for(int i = -10; i <=10; i++) {
                    for(int b = -10; b <=10; b++) {
                        if(field[x][y] != null) capture.setRGB((sizepx / (gridsize * 2)) + (x * (sizepx / gridsize)) + i, (sizepx / (gridsize * 2)) + (y * (sizepx / gridsize)) +b, Color.PINK.getRGB());
                    }
                }
            }
        }

        try {
            ImageIO.write(capture, "png", new File("modified_example.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static List<Point> calculateShortestPath(Color c) {
        System.out.println("Searching path..");
        List<Point> nodes = new ArrayList<>();

        Point sp = start.get(c);
        Point ep = finish.get(c);
        System.out.println(sp.distance(ep));
        if(sp.distance(ep) == 1) nodes.add(ep);
        nodes.add(sp);
        while(!nodes.contains(ep)) {
            int shortest = Integer.MAX_VALUE;
            Point bestPoint = null;
            for(int i = 0; i<2; i++) {
                for(int x = -1; x <= 1; x++) {
                    Point cur = (Point) nodes.get(nodes.size()-1).clone();
                    int newC = 0;
                    if(i == 0) {
                        cur.x+=x;
                        newC = cur.x;
                    } else {
                        cur.y+=x;
                        newC = cur.y;
                    }
                    if(newC >= 0 && newC < field.length) {
                        if(field[cur.x][cur.y] == null) {
                            int distance = Math.abs(ep.x - cur.x) + Math.abs(ep.y - cur.y);
                            int shortest2 = Integer.MAX_VALUE;

                            if(distance < shortest) {
                                shortest = distance;
                                bestPoint = cur;
                            }
                        } else {
                            try {
                                if(cur.distance(ep) < 1) {
                                    bestPoint = ep;
                                }
                            } catch (Exception e) {
                                System.out.println(c + " No end?");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if(!nodes.contains(bestPoint)) nodes.add(bestPoint);
        }

        return nodes;

    }

}
