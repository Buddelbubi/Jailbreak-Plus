package de.buddelbubi.misc;

public class KeyIdentifier {

    public static String getKey(Integer key) {
        switch (key) {
            case 11:
                return "num_0";
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
            case 35:
                return "h";
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
            case 56:
                return "alt";
            case 57:
                return "space";
        }
        if(key >= 2 && key <= 10) {
            return "num_" + (key-1);
        }
        return null;
    }

}
