import junit.framework.TestCase;


public class DiameterRangesFactoryTest extends TestCase {
    public void testCreateLinear() throws Exception {
        int numberElements = 4;
        double minimum = 0.0;
        double maximum = 100.0;

        DiameterRanges diameterRanges = DiameterRangesFactory.createLinearRanges(numberElements, minimum, maximum);

        double [] minimumsReference = {0.0, 25.0, 50.0, 75.0};
        double [] maximumsReference = {25.0, 50.0, 75.0, 100.0};

        assertEquals(numberElements, diameterRanges.getSize());
        assertEquals(minimumsReference.length, diameterRanges.getSize());
        assertEquals(maximumsReference.length, diameterRanges.getSize());

        for (int index=0; index < numberElements; index++) {
            minimum = diameterRanges.get(index).getMinimum();
            assertEquals(minimumsReference[index], minimum, 1.0e-6);

            maximum = diameterRanges.get(index).getMaximum();
            assertEquals(maximumsReference[index], maximum, 1.0e-6);
        }
    }

    public void testCreateLogarithmic() throws Exception {
        int numberElements = 4;
        double minimum = 0.0;
        double maximum = 1.0e4;

        DiameterRanges diameterRanges = DiameterRangesFactory.createLogarithmicRanges(numberElements, minimum, maximum);

        double [] minimumsReference = {1.0, 10.0, 100.0, 1000.0};
        double [] maximumsReference = {10.0, 100.0, 1000.0, 10000.0};

        assertEquals(numberElements, diameterRanges.getSize());
        assertEquals(minimumsReference.length, diameterRanges.getSize());
        assertEquals(maximumsReference.length, diameterRanges.getSize());

        for (int index=0; index < numberElements; index++) {
            minimum = diameterRanges.get(index).getMinimum();
            assertEquals(minimumsReference[index], minimum, 1.0e-6);

            maximum = diameterRanges.get(index).getMaximum();
            assertEquals(maximumsReference[index], maximum, 1.0e-6);
        }
    }
}
