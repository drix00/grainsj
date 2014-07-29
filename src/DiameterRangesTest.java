import java.awt.Color;

import junit.framework.TestCase;


public class DiameterRangesTest extends TestCase {
    int numberElements = 4;
    DiameterRanges diameterRanges = new DiameterRanges(numberElements);

    public void testGetSize() {
        DiameterRanges diameterRanges = new DiameterRanges();
        assertEquals(0, diameterRanges.getSize());
    }

    public void testConstructor() throws Exception {
        int numberElements = 4;
        DiameterRanges diameterRanges = new DiameterRanges(numberElements);

        assertEquals(numberElements, diameterRanges.getSize());
    }

    public void testGet() {
        for(int index=0; index<this.numberElements; index++) {
            int expectedId = index+1;
            DiameterRange diameterRange = diameterRanges.get(index);
            int actualId = diameterRange.getId();

            assertEquals(expectedId, actualId);
        }
    }

    public void testSet() {
        double minimum = 0.0;
        double step = 200.0;
        for(int index=0; index<this.numberElements; index++) {
            double maximum = minimum + step;
            int id = index+1;
            String unit = "pixel";
            Color color = Color.BLUE;
            DiameterRange diameterRange = new DiameterRange(id, minimum, maximum,
                                                            unit, color);
            diameterRanges.set(index, diameterRange);
            minimum = maximum;
        }

        minimum = 0.0;
        for(int index=0; index<this.numberElements; index++) {
            double maximum = minimum + step;
            DiameterRange diameterRange = diameterRanges.get(index);

            assertEquals(minimum, diameterRange.getMinimum());
            assertEquals(maximum, diameterRange.getMaximum());

            minimum = maximum;
        }
    }

    public void testAdd() {
        DiameterRanges diameterRanges = new DiameterRanges();
        double minimum = 0.0;
        double step = 200.0;
        for(int index=0; index<this.numberElements; index++) {
            double maximum = minimum + step;
            int id = index+1;
            String unit = "pixel";
            Color color = Color.BLUE;
            DiameterRange diameterRange = new DiameterRange(id, minimum, maximum,
                                                            unit, color);
            diameterRanges.add(diameterRange);
            minimum = maximum;
        }

        minimum = 0.0;
        for(int index=0; index<this.numberElements; index++) {
            double maximum = minimum + step;
            DiameterRange diameterRange = diameterRanges.get(index);

            assertEquals(minimum, diameterRange.getMinimum());
            assertEquals(maximum, diameterRange.getMaximum());

            minimum = maximum;
        }
    }

    public void testRemove() {
        diameterRanges.remove();

        assertEquals(3, diameterRanges.getSize());

        int expectedId = 1;
        int actualId = diameterRanges.get(0).getId();
        assertEquals(expectedId, actualId);

        expectedId = 2;
        actualId = diameterRanges.get(1).getId();
        assertEquals(expectedId, actualId);

        expectedId = 3;
        actualId = diameterRanges.get(2).getId();
        assertEquals(expectedId, actualId);
    }

    public void testGetColor() throws Exception {
        Color[] colors = RangeColors.getColors();

        Color expectedColor = colors[0];
        Color actualColor = diameterRanges.getColor(-10.0);
        assertEquals(expectedColor, actualColor);

        expectedColor = colors[0];
        actualColor = diameterRanges.getColor(0.0);
        assertEquals(expectedColor, actualColor);

        expectedColor = colors[0];
        actualColor = diameterRanges.getColor(99.999999);
        assertEquals(expectedColor, actualColor);

        expectedColor = colors[1];
        actualColor = diameterRanges.getColor(100.0);
        assertEquals(expectedColor, actualColor);

        expectedColor = colors[3];
        actualColor = diameterRanges.getColor(399.999);
        assertEquals(expectedColor, actualColor);

        expectedColor = colors[3];
        actualColor = diameterRanges.getColor(40156.0);
        assertEquals(expectedColor, actualColor);
    }

    public void testEquals() throws Exception {
        DiameterRanges diameterRanges2 = new DiameterRanges(numberElements);

        assertTrue(diameterRanges2.equals(diameterRanges));

        double minimum = 0.0;
        double maximum = 12.0;
        DiameterRanges diameterRanges3 = DiameterRangesFactory.createLinearRanges(numberElements, minimum, maximum);

        assertFalse(diameterRanges3.equals(diameterRanges));
        assertFalse(diameterRanges3.equals(diameterRanges2));

        DiameterRanges diameterRangesDifferent = new DiameterRanges(34);
        assertFalse(diameterRangesDifferent.equals(diameterRanges));

        assertFalse(diameterRangesDifferent.equals(new Double(34)));
    }

    public void testSetSize() throws Exception {
        int numberElement = 2;
        diameterRanges.setSize(numberElement);
        assertEquals(numberElement, diameterRanges.getSize());

        numberElement = 0;
        diameterRanges.setSize(numberElement);
        assertEquals(numberElement, diameterRanges.getSize());

        numberElement = 10;
        diameterRanges.setSize(numberElement);
        assertEquals(numberElement, diameterRanges.getSize());

        numberElement = 12;
        diameterRanges.setSize(numberElement);
        assertEquals(numberElement, diameterRanges.getSize());

        numberElement = 1;
        diameterRanges.setSize(numberElement);
        assertEquals(numberElement, diameterRanges.getSize());

        numberElement = -1;
        diameterRanges.setSize(numberElement);
        assertEquals(0, diameterRanges.getSize());
    }

    public void testRemoveAll() throws Exception {
        int initialNumberElement = numberElements;
        assertEquals(initialNumberElement, diameterRanges.getSize());

        diameterRanges.removeAll();
        assertEquals(0, diameterRanges.getSize());
    }
}
