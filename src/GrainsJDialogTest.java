import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import junit.framework.TestCase;


public class GrainsJDialogTest extends TestCase {
    GrainsJParameters parameters = new GrainsJParameters(null, null);
    ImagePlus outlineImage = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        WindowManager.closeAllWindows();
        
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
        GrainsJDialog dialog = new GrainsJDialog(parameters);
    }

    public void testRun() throws Exception {
        GrainsJDialog dialog = new GrainsJDialog(parameters);
        dialog.run("NoGUI");
    }
}
