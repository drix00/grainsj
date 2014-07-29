import junit.framework.TestCase;


public class TypeOfDiameterRangeTest extends TestCase {

    public void test_getLabel() {
        TypeOfDiameterRange typeManual = TypeOfDiameterRange.MANUAL;
        assertEquals("Manual", typeManual.getLabel());

        TypeOfDiameterRange typeLinear = TypeOfDiameterRange.LINEAR;
        assertEquals("Linear", typeLinear.getLabel());

        TypeOfDiameterRange typeLogarithmic = TypeOfDiameterRange.LOGARITHMIC;
        assertEquals("Logarithmic", typeLogarithmic.getLabel());
    }

    public void test_toString() {
        TypeOfDiameterRange typeManual = TypeOfDiameterRange.MANUAL;
        assertEquals("MANUAL", typeManual.toString());

        TypeOfDiameterRange typeLinear = TypeOfDiameterRange.LINEAR;
        assertEquals("LINEAR", typeLinear.toString());

        TypeOfDiameterRange typeLogarithmic = TypeOfDiameterRange.LOGARITHMIC;
        assertEquals("LOGARITHMIC", typeLogarithmic.toString());
    }

    public void test_compareTo() {
        TypeOfDiameterRange typeManual = TypeOfDiameterRange.MANUAL;
        TypeOfDiameterRange typeLinear = TypeOfDiameterRange.LINEAR;
        TypeOfDiameterRange typeLogarithmic = TypeOfDiameterRange.LOGARITHMIC;

        assertTrue(typeManual.compareTo(typeManual) == 0);
        assertTrue(typeManual.compareTo(typeLinear) < 0);
        assertTrue(typeManual.compareTo(typeLogarithmic) < 0);

        assertTrue(typeLinear.compareTo(typeManual) > 0);
        assertTrue(typeLinear.compareTo(typeLinear) == 0);
        assertTrue(typeLinear.compareTo(typeLogarithmic) < 0);

        assertTrue(typeLogarithmic.compareTo(typeManual) > 0);
        assertTrue(typeLogarithmic.compareTo(typeLinear) > 0);
        assertTrue(typeLogarithmic.compareTo(typeLogarithmic) == 0);
    }

    public void testValues() throws Exception {
        TypeOfDiameterRange [] typeValues = TypeOfDiameterRange.values();
        assertEquals(3, typeValues.length);
    }

    public void testOrdinal() throws Exception {
        TypeOfDiameterRange [] typeValues = TypeOfDiameterRange.values();

        for (int index=0; index < typeValues.length; index++) {
            assertEquals(index, typeValues[index].ordinal());
        }
    }
}
