import java.awt.Color;

import junit.framework.TestCase;


public class DiameterRangeTest extends TestCase {
    private DiameterRange diameterRange;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        int id = 2;
        double minimum = 34.6;
        double maximum = 5678.0;
        String unit = "nm";
        Color color = Color.BLUE;

        diameterRange = new DiameterRange(id, minimum, maximum, unit, color);
    }
    public void testDiameterRange() {
        int id = 2;
        double minimum = 34.6;
        double maximum = 5678.0;
        String unit = "nm";
        Color color = Color.BLUE;

        diameterRange = new DiameterRange(id, minimum, maximum, unit, color);

        assertEquals(id, diameterRange.getId());
        assertEquals(minimum, diameterRange.getMinimum(), 1.0e-6);
        assertEquals(maximum, diameterRange.getMaximum(), 1.0e-6);
        assertEquals(unit, diameterRange.getUnit());
        assertEquals(color, diameterRange.getColor());
        
        RangeColors.reset();
        diameterRange = new DiameterRange(id, minimum, maximum);

        assertEquals(id, diameterRange.getId());
        assertEquals(minimum, diameterRange.getMinimum(), 1.0e-6);
        assertEquals(maximum, diameterRange.getMaximum(), 1.0e-6);
        assertEquals("pixel", diameterRange.getUnit());
        assertEquals(Color.BLUE, diameterRange.getColor());

        RangeColors.reset();
        diameterRange = new DiameterRange();

        assertEquals(0, diameterRange.getId());
        assertEquals(0.0, diameterRange.getMinimum(), 1.0e-6);
        assertEquals(100.0, diameterRange.getMaximum(), 1.0e-6);
        assertEquals("pixel", diameterRange.getUnit());
        assertEquals(Color.BLUE, diameterRange.getColor());
    }

    public void testEquals() throws Exception {
        int id = 2;
        double minimum = 34.6;
        double maximum = 5678.0;
        String unit = "nm";
        Color color = Color.BLUE;

        DiameterRange diameterRangeSame = new DiameterRange(id, minimum, maximum, unit, color);
        assertTrue(diameterRangeSame.equals(diameterRange));

        DiameterRange diameterRangeDifferent = new DiameterRange(id, minimum, maximum, unit, color);
        diameterRangeDifferent.setId(1);
        assertFalse(diameterRangeDifferent.equals(diameterRange));

        diameterRangeDifferent = new DiameterRange(id, minimum, maximum, unit, color);
        diameterRangeDifferent.setMinimum(0.0);
        assertFalse(diameterRangeDifferent.equals(diameterRange));
        
        diameterRangeDifferent = new DiameterRange(id, minimum, maximum, unit, color);
        diameterRangeDifferent.setMaximum(100.0);
        assertFalse(diameterRangeDifferent.equals(diameterRange));
        
        diameterRangeDifferent = new DiameterRange(id, minimum, maximum, unit, color);
        diameterRangeDifferent.setUnit("pixel");
        assertFalse(diameterRangeDifferent.equals(diameterRange));
        
        diameterRangeDifferent = new DiameterRange(id, minimum, maximum, unit, color);
        diameterRangeDifferent.setColor(Color.BLACK);
        assertFalse(diameterRangeDifferent.equals(diameterRange));
    }
    
    public void testSetIdWithInterger() throws Exception {
        int id = 45;
        diameterRange.setId(new Integer(id));
        
        assertEquals(id, diameterRange.getId());
    }
    
    public void testSetMinimumWithDouble() throws Exception {
        double minimum = 0.0;
        diameterRange.setMinimum(new Double(minimum));
        
        assertEquals(minimum, diameterRange.getMinimum());
    }
    
    public void testSetMaximumWithDouble() throws Exception {
        double maximum = 100.0;
        diameterRange.setMaximum(new Double(maximum));
        
        assertEquals(maximum, diameterRange.getMaximum());
    }
}
