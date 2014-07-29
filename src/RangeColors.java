import java.awt.Color;

import ij.plugin.Colors;


public class RangeColors {
    static private int index = 0;

    static java.awt.Color [] colors = new java.awt.Color [] {
            java.awt.Color.BLUE
            , java.awt.Color.RED
            , java.awt.Color.YELLOW
            , java.awt.Color.GREEN
            , java.awt.Color.ORANGE
            , java.awt.Color.MAGENTA
            , java.awt.Color.PINK
            , java.awt.Color.CYAN
            };

    static String [] colorNames = new String [] {
            "Blue"
            , "Red"
            , "Yellow"
            , "Green"
            , "Orange"
            , "Magenta"
            , "Pink"
            , "Cyan"
            };

    static public Color[] getColors() {
        return colors;
    }

    static public String[] getColorNames() {
        return colorNames;
    }

    static public Color next() {
        index = index%colors.length;
        Color color = colors[index];
        index++;
        return color;

    }

    static public String getColorName(Color color) {
        for (int i = 0; i < colors.length; i++) {
            if (color.equals(colors[i])) {
                return colorNames[i];
            }
        }
        String colorName = createColorName(color);
        
        return colorName;
    }

    public static String createColorName(Color color) {
        String name = String.format("r%03d", color.getRed());
        name += String.format("g%03d", color.getGreen());
        name += String.format("b%03d", color.getBlue());
        
        return name;
    }

    static public Color getColor(int colorIndex) {
        int correctIndex = colorIndex%colors.length;

        return colors[correctIndex];
    }

    static public Color getColorByName(String colorText) {
        for(int index=0; index<colorNames.length; index++) {
            if(colorText.equalsIgnoreCase(colorNames[index])) {
                return colors[index];
            }
        }
        
        Color color = extractCorlorFromName(colorText);
        
        return color;
    }

    public static Color extractCorlorFromName(String colorText) {
        if(colorText.length() == 12) {
            int startIndex = 1;
            int red = Integer.parseInt(colorText.substring(startIndex, startIndex+3));
            startIndex = 5;
            int green = Integer.parseInt(colorText.substring(startIndex, startIndex+3));
            startIndex = 9;
            int blue = Integer.parseInt(colorText.substring(startIndex, startIndex+3));
            
            Color color = new Color(red, green, blue);
            return color;
        }
        
        return Color.BLUE;
    }

    static public void reset() {
        index = 0;
    }
}
