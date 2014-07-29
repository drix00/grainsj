import ij.IJ;
import ij.ImagePlus;
import junit.framework.TestCase;


public class GrainsJTest extends TestCase {
    ImagePlus outlineImage = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String imagesFilepath = "./images/OutlineSmall.tif";
        outlineImage = IJ.openImage(imagesFilepath);
        outlineImage.show();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        outlineImage.close();
    }

    public void testConstructor() throws Exception {
        GrainsJ_ grainsJ = new GrainsJ_();
    }

    public void testRun() throws Exception {
        GrainsJ_ grainsJ = new GrainsJ_();
        grainsJ.run("NoGUI");
    }

// TODO: Write more tests to improve code coverage.
}
