import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;


public class PrepareImageForOutlineAnalysis {

    ImagePlus outlineImage = null;

    public PrepareImageForOutlineAnalysis(ImagePlus outlineImage) {
        this.outlineImage = outlineImage;
    }

    public void run() {
        transformToBinaryImage();

        if(isOutlineBlack()){
            invertImage();
        }
    }

    private void transformToBinaryImage() {
        ImageConverter imageConverter = new ImageConverter(this.outlineImage);
        imageConverter.convertToGray8();

        GrainsJ_.updateImage(this.outlineImage);

        ImageProcessor imp = this.outlineImage.getProcessor();
        imp.autoThreshold();

        GrainsJ_.logAutoThresholdValue(imp);

        GrainsJ_.updateImage(this.outlineImage);
    }

    private boolean isOutlineBlack() {
        int[] histogram = getOutlineImageHistogram();

        int blackCounts = getBlackCounts(histogram);

        int whiteCounts = getWhiteCounts(histogram);

        return (blackCounts < whiteCounts);
    }

    private int[] getOutlineImageHistogram() {
        ImageProcessor imp = this.outlineImage.getProcessor();
        int[] histogram = imp.getHistogram();
        return histogram;
    }

    private int getBlackCounts(int[] histogram) {
        int blackIndex = 0;
        int blackCounts = histogram[blackIndex];
        return blackCounts;
    }

    private int getWhiteCounts(int[] histogram) {
        int whiteIndex = histogram.length-1;
        int whiteCounts = histogram[whiteIndex];
        return whiteCounts;
    }

    private void invertImage() {
        ImageProcessor imp = this.outlineImage.getProcessor();

        GrainsJ_.logBlackWhiteCounts(imp, "before");

        imp.invert();

        GrainsJ_.logBlackWhiteCounts(imp, "after");
        GrainsJ_.updateImage(this.outlineImage);
    }

    public ImagePlus getOutlineImage() {
        return this.outlineImage;
    }
}
