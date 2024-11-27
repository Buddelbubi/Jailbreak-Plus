package de.buddelbubi.misc;

public class KeyIdentifier {

    public static String getKey(Integer key) {

        switch (key) {
            case 17:
                return "w";
            case 18:
                return "e";
            case 21:
                return "y";
            case 28:
                return "enter";
            case 33:
                return "f";
            case 34:
                return "g";
            case 38:
                return "l";
            case 45:
                return "x";
            case 47:
                return "v";
            case 48:
                return "b";
            case 49:
                return "n";
            case 57:
                return "space";
        }
        return null;
    }

}
