import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;


public class GrainsJDialog {
    GrainsJParameters parameters;

    private GenericDialog gd;
    private String[] windowTitles;

    private int[] windowIDs;

    public GrainsJDialog(GrainsJParameters parameters) {
        if (!isImageOpened()) {
            IJ.open();
        }

        this.parameters = parameters;
        this.parameters.read();

        this.windowTitles = getWindowTitles();

        this.gd = createDialog();

    }

    private boolean isImageOpened() {
        return WindowManager.getImageCount() > 0;
    }

    private String[] getWindowTitles() {
        this.windowIDs = getWindowIDs();
        int numberWindows = this.windowIDs.length;

        String[] newWindowTitles = new String[numberWindows];

        for (int i=0; i<numberWindows; i++){
            ImagePlus im = WindowManager.getImage(this.windowIDs[i]);

            if (im == null){
                newWindowTitles[i] = "Untitled";
            }
            else {
                newWindowTitles[i] = im.getShortTitle();
            }
        }

        return newWindowTitles;
    }

    private int[] getWindowIDs() {
        int[] windowList = WindowManager.getIDList();

        if(windowList == null) {
            IJ.noImage();

            return new int [] {};
        }

        return windowList;
    }

    private GenericDialog createDialog() {
        GenericDialog genericDialog = new GenericDialog("Grains Outline Analysis");

        genericDialog.addChoice("Outline Image:", this.windowTitles, this.windowTitles[0]);

        if (this.windowTitles.length >= 2) {
            //genericDialog.addChoice("Original Image:", this.windowTitles, this.windowTitles[1]);
        }

        genericDialog.addCheckbox("Include edge particle:", parameters.isIncludeEdge());

        genericDialog.addCheckbox("Include particle perimeter:", parameters.isIncludePerimeter());

        genericDialog.addNumericField("Minimum particle size:", parameters.getMinimumParticleSize(), 2);

        if(IJ.versionLessThan("1.42f")) {
            genericDialog.addMessage("Some grains will not be colored, you need ImageJ version 1.42f or newer.");
        }

        return genericDialog;
    }

    public boolean run(String arg) {
        if (!arg.equalsIgnoreCase("NoGUI")) {
            this.gd.showDialog();

            if (this.gd.wasCanceled()) {
                IJ.error("PlugIn canceled!");
                return false;
            }
        }

        getDialogValues();

        this.parameters.save();

        return true;
    }

    private void getDialogValues() {
        setOutlineImageFromDialog();

        setOriginalImageFromDialog();

        boolean includeEdge = this.gd.getNextBoolean();
        parameters.setIncludeEdge(includeEdge);

        boolean includePerimeter = this.gd.getNextBoolean();
        parameters.setIncludePerimeter(includePerimeter);

        double minimumParticleSize = this.gd.getNextNumber();
        parameters.setMinimumParticleSize(minimumParticleSize);

    }

    private void setOutlineImageFromDialog() {
        int image1Idx = this.gd.getNextChoiceIndex();
        ImagePlus outlineImage = WindowManager.getImage(this.windowIDs[image1Idx]);
        this.parameters.setOutlineImage(outlineImage);
        if (GrainsJ_.logging) {
            String message = "Outline image: " + outlineImage.getTitle();
            IJ.log(message);
        }
    }

    private void setOriginalImageFromDialog() {
        if (this.windowTitles.length >= 2) {
            int image2Idx = this.gd.getNextChoiceIndex();
            ImagePlus originalImage = WindowManager.getImage(this.windowIDs[image2Idx]);
            this.parameters.setOriginalImage(originalImage);
            if (GrainsJ_.logging) {
                String message = "Original image: " + originalImage.getTitle();
                IJ.log(message);
            }
        }
    }

}
