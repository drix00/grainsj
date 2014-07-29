import ij.ImagePlus;
import junit.framework.TestCase;


public class GrainsJParametersTest extends TestCase {

    private ImagePlus outlineImage;
    private ImagePlus originalImage;
    private GrainsJParameters originalParameters;
    private GrainsJParameters parameters;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        outlineImage = null;
        originalImage = null;

        originalParameters = new GrainsJParameters(outlineImage, originalImage);
        originalParameters.read();

        parameters = new GrainsJParameters(outlineImage, originalImage);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        originalParameters.save();
    }

    public void testGrainsJParameters() {
        assertEquals(outlineImage, parameters.getOutlineImage());
        assertEquals(originalImage, parameters.getOriginalImage());

    }

    public void testSetIsIncludeEdge() {
        parameters.setIncludeEdge(true);
        assertTrue(parameters.isIncludeEdge());

        parameters.setIncludeEdge(false);
        assertFalse(parameters.isIncludeEdge());

    }

    public void testSetIsIncludePerimeter() {
        parameters.setIncludePerimeter(true);
        assertTrue(parameters.isIncludePerimeter());

        parameters.setIncludePerimeter(false);
        assertFalse(parameters.isIncludePerimeter());

    }

    public void testSetGetNumberColors() {
        int numberColors = 10232;
        parameters.setNumberColors(numberColors);
        assertEquals(numberColors, parameters.getNumberColors());
    }

    public void testSetTypeOfDiameterRange() {
        TypeOfDiameterRange typeOfDiameterRange = TypeOfDiameterRange.LOGARITHMIC;
        parameters.setTypeOfDiameterRange(typeOfDiameterRange);
        assertEquals(typeOfDiameterRange, parameters.getTypeOfDiameterRange());
    }

    public void testSetTypeOfDiameterRangeString() {
        TypeOfDiameterRange typeOfDiameterRange = TypeOfDiameterRange.LOGARITHMIC;
        parameters.setTypeOfDiameterRange(typeOfDiameterRange.getLabel());
        assertEquals(typeOfDiameterRange, parameters.getTypeOfDiameterRange());
    }

    public void testSetGetMinimumParticleSize() {
        double minimumParticleSize = 34.567;
        parameters.setMinimumParticleSize(minimumParticleSize);
        assertEquals(minimumParticleSize, parameters.getMinimumParticleSize(), 1.0e-6);
    }

    public void testReadSave() throws Exception {
        int numberElements = 3;
        double minimum = 0.0;
        double maximum = 356.025;

        DiameterRanges diameterRanges = DiameterRangesFactory.createLinearRanges(numberElements, minimum, maximum);
        parameters.setDiameterRanges(diameterRanges);
        parameters.save();

        GrainsJParameters newParameters =  new GrainsJParameters(outlineImage, originalImage);
        newParameters.read();
        DiameterRanges actualDiameterRanges = newParameters.getDiameterRanges();

        assertEquals(diameterRanges, actualDiameterRanges);
    }
}
