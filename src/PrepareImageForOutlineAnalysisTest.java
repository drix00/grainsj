import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.io.File;

import junit.framework.TestCase;


public class PrepareImageForOutlineAnalysisTest extends TestCase {
    ImagePlus outlineImage = null;
    PrepareImageForOutlineAnalysis pioa = null;

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();

        String imagesFilepath = "./images/OutlineSmall.tif";
        outlineImage = new ImagePlus(imagesFilepath);
        pioa = new PrepareImageForOutlineAnalysis(new ImagePlus(imagesFilepath));
    }

    public void testConstructor() throws Exception {
        ImagePlus notTransformedImage = pioa.getOutlineImage();

        int expectedBitDepth = 8;
        int actualBitDepth = notTransformedImage.getBitDepth();
        assertEquals(expectedBitDepth, actualBitDepth);

        int expectedType = ImagePlus.GRAY8;
        int actualType = notTransformedImage.getType();
        assertEquals(expectedType, actualType);
    }

    public void testRun() throws Exception {
        pioa.run();

        ImagePlus transformedImage = pioa.getOutlineImage();

        assertNotSame(outlineImage, transformedImage);

        int expectedBlackCounts = 1647766;
        int expectedWhiteCounts = 62522;

        ImageProcessor imp = transformedImage.getProcessor();
        int[] histogram = imp.getHistogram();
        int actualBlackCounts = histogram[0];
        int actualWhiteCounts = histogram[255];

        assertEquals(expectedBlackCounts, actualBlackCounts);
        assertEquals(expectedWhiteCounts, actualWhiteCounts);
    }

    public void testIsOutlineBlack() throws Exception {
        ImageProcessor imp = this.outlineImage.getProcessor();
        imp.invert();

        pioa = new PrepareImageForOutlineAnalysis(this.outlineImage);
        pioa.run();

        ImagePlus transformedImage = pioa.getOutlineImage();
        imp = transformedImage.getProcessor();

        int expectedBlackCounts = 1643723;
        int expectedWhiteCounts = 66565;

        int[] histogram = imp.getHistogram();
        int actualBlackCounts = histogram[0];
        int actualWhiteCounts = histogram[255];

        assertEquals(expectedBlackCounts, actualBlackCounts);
        assertEquals(expectedWhiteCounts, actualWhiteCounts);
    }
}
