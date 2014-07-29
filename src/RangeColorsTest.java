import java.awt.Color;

import junit.framework.TestCase;


public class RangeColorsTest extends TestCase {

    public void testNext() {
        RangeColors.reset();
        Color[] colors = RangeColors.getColors();

        for (int i = 0; i < colors.length; i++) {
            Color expectedColor = colors[i];
            Color actualColor = RangeColors.next();

            assertEquals(expectedColor, actualColor);
        }
    }

    public void testNextLargeIteration() throws Exception {
        for (int i = 0; i < 100; i++) {
            Color actualColor = RangeColors.next();
        }
    }

    public void testGetColorByName() throws Exception {
        Color [] colors = RangeColors.getColors();
        String [] colorNames = RangeColors.getColorNames();

        assertEquals(colors.length, colorNames.length);

        for(int index=0; index<colors.length; index++) {
            Color color = RangeColors.getColorByName(colorNames[index]);
            assertEquals(colors[index], color);
        }
        
        Color expectedColor = new Color(34, 56, 78);
        Color actualColor = RangeColors.getColorByName("r034g056b078");
        assertEquals(expectedColor, actualColor);
    }

    public void testGetColorByNameBadInput() throws Exception {
        Color color = RangeColors.getColorByName("Black");
        assertEquals(Color.BLUE, color);
    }

    public void testGetColorName() throws Exception {
        Color [] colors = RangeColors.getColors();
        String [] colorNames = RangeColors.getColorNames();

        assertEquals(colors.length, colorNames.length);

        for(int index=0; index<colors.length; index++) {
            String colorName = RangeColors.getColorName(colors[index]);
            assertEquals(colorNames[index], colorName);
        }
        
        Color color = new Color(34, 56, 78);
        String expectedName = "r034g056b078";
        String actualName = RangeColors.getColorName(color);
        assertEquals(expectedName, actualName);
    }

    public void testGetColorNameBadInput() throws Exception {
        String colorName = RangeColors.getColorName(Color.BLACK);
        assertEquals("r000g000b000", colorName);
    }

    public void testGetColor() throws Exception {
        Color color = RangeColors.getColor(0);
        assertEquals(Color.BLUE, color);

        color = RangeColors.getColor(1);
        assertEquals(Color.RED, color);

        color = RangeColors.getColor(7);
        assertEquals(Color.CYAN, color);

        color = RangeColors.getColor(8);
        assertEquals(Color.BLUE, color);

        color = RangeColors.getColor(9);
        assertEquals(Color.RED, color);
    }

    public void testCreateColorName() throws Exception {
        String expectedName = "r000g000b255";
        String actualName = RangeColors.createColorName(Color.BLUE);
        assertEquals(expectedName, actualName);

        expectedName = "r000g255b000";
        actualName = RangeColors.createColorName(Color.GREEN);
        assertEquals(expectedName, actualName);

        expectedName = "r255g000b000";
        actualName = RangeColors.createColorName(Color.RED);
        assertEquals(expectedName, actualName);

        expectedName = "r255g255b000";
        actualName = RangeColors.createColorName(Color.YELLOW);
        assertEquals(expectedName, actualName);
    }

    public void testExtractCorlorFromName() throws Exception {
        Color expectedColor = Color.BLUE;
        Color actualColor = RangeColors.extractCorlorFromName("r000g000b255");
        assertEquals(expectedColor, actualColor);

        expectedColor = Color.GREEN;
        actualColor = RangeColors.extractCorlorFromName("r000g255b000");
        assertEquals(expectedColor, actualColor);

        expectedColor = Color.RED;
        actualColor = RangeColors.extractCorlorFromName("r255g000b000");
        assertEquals(expectedColor, actualColor);

        expectedColor = Color.YELLOW;
        actualColor = RangeColors.extractCorlorFromName("r255g255b000");
        assertEquals(expectedColor, actualColor);
    }
}
