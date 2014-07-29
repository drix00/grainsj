import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ByteProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.HashMap;

public class GrainsGeometricDistribution {

    ImagePlus outlineImage = null;
    TypeOfDiameterRange typeOfDiameterRange;

    int numberColors = 5;

    //CreateDiameterRanges diameterRange;
    private double maximumDiameter;
    private double minimumDiameter;
    DiameterRanges diameterRanges;
    private boolean includePerimeter;

    private GrainsJParameters parameters = null;
    private ImageProcessor outlineMask;

    public GrainsGeometricDistribution(GrainsJParameters parameters) {
        this(parameters, null);
    }

    public GrainsGeometricDistribution(GrainsJParameters parameters, ImageProcessor outlineMask) {
        this.outlineImage = parameters.getOutlineImage();
        this.numberColors = parameters.getNumberColors();
        this.typeOfDiameterRange = parameters.getTypeOfDiameterRange();

        this.diameterRanges = parameters.getDiameterRanges();
        this.includePerimeter = parameters.isIncludePerimeter();

        this.parameters = parameters;

        this.outlineMask = outlineMask;
    }

    private void transformToColorImage() {
        ImageConverter imageConverter = new ImageConverter(this.outlineImage);
        imageConverter.convertToRGB();

        if (GrainsJ_.debugging) {
            this.outlineImage.updateAndDraw();
        }
    }

    public void fillGrains(HashMap<Integer,Roi> rois, HashMap<Integer,Double> diameters) {
        transformToColorImage();

        fillBackgroundAndOutlineColor();

        minimumDiameter = computeMinimumDiameter(diameters);
        maximumDiameter = computeMaximumDiameter(diameters);

        if (GrainsJ_.logging) {
            String message = "Maximum diameter: " + Double.toString(maximumDiameter);
            IJ.log(message);
        }

        if (GrainsJ_.logging) {
            int numberOfRois = rois.size();
            String message = "Number of rois (as map): " + Integer.toString(numberOfRois);
            IJ.log(message);

            int numberOfDiameters = diameters.size();
            message = "Number of diameters (as map): " + Integer.toString(numberOfDiameters);
            IJ.log(message);
        }

        for (Integer key : rois.keySet()) {
            double diameter = diameters.get(key).doubleValue();
            java.awt.Color color = this.diameterRanges.getColor(diameter);

            Roi roi = rois.get(key);

            fillGrain(roi, color);
        }

        removeRoiOutlinesAndLabels();

        transformToLUTColorImage();

        //this.showColorRange();
    }

//    private void showColorRange() {
//        ResultsTable colorRangeTable = new ResultsTable();
//
//        String minimumDiameterHeading = "Minimum";
//        String maximumDiameterHeading = "Maximum";
//        String colorHeading = "Color";
//        String numberGrainsHeading = "Grains";
//
//        colorRangeTable.getFreeColumn(minimumDiameterHeading);
//        colorRangeTable.getFreeColumn(maximumDiameterHeading);
//        colorRangeTable.getFreeColumn(numberGrainsHeading);
//
//        double[] ranges = this.diameterRange.getRanges();
//
//        for (int index = 0; index < ranges.length; index++) {
//            double lowerDiameter = ranges[index];
//            double upperDiameter = 0.0;
//
//            if (index < ranges.length-1) {
//                upperDiameter = ranges[index+1];
//            }else{
//                upperDiameter = this.maximumDiameter;
//            }
//
//            colorRangeTable.incrementCounter();
//            colorRangeTable.addValue(minimumDiameterHeading, lowerDiameter);
//            colorRangeTable.addValue(maximumDiameterHeading, upperDiameter);
//
//            double meanDiameter = (lowerDiameter+ upperDiameter)/2.0;
//            int colorIndex = this.diameterRange.findIndex(meanDiameter);
//            java.awt.Color color = RangeColors.getColor(colorIndex);
//            String colorName = RangeColors.getColorName(color);
//            colorRangeTable.addLabel(colorHeading, colorName);
//
//            colorRangeTable.addValue(numberGrainsHeading, 0.0);
//        }
//
//        String windowTitle = "Color diameter range";
//        colorRangeTable.show(windowTitle);
//    }

    private void fillBackgroundAndOutlineColor() {
        ImageProcessor imageProcessor = this.outlineImage.getProcessor();
        imageProcessor.resetRoi();

        imageProcessor.setColor(this.parameters.getBackgroundColor());
        imageProcessor.fill();
        this.outlineImage.updateAndDraw();

        imageProcessor.resetRoi();
        imageProcessor.setColor(this.parameters.getOutlineColor());
        imageProcessor.fill(outlineMask);
    }

    private void transformToLUTColorImage() {
        ImageConverter imageConverter = new ImageConverter(this.outlineImage);
        int numberIndexedColors = this.numberColors + 2;
        imageConverter.convertRGBtoIndexedColor(numberIndexedColors);

        if (GrainsJ_.debugging) {
            this.outlineImage.updateAndDraw();
        }
    }

    static public double computeMinimumDiameter(HashMap<Integer, Double> diameters) {
        double minimumDiameter = 0.0;

        for (Integer key : diameters.keySet()) {
            double diameter = diameters.get(key).doubleValue();

            minimumDiameter = Math.min(minimumDiameter, diameter);
        }

        return minimumDiameter;
    }

    static public double computeMaximumDiameter(HashMap<Integer, Double> diameters) {
        double maximumDiameter = 0.0;

        for (Integer key : diameters.keySet()) {
            double diameter = diameters.get(key).doubleValue();

            maximumDiameter = Math.max(maximumDiameter, diameter);
        }

        return maximumDiameter;
    }

    private void fillGrain(Roi roi, Color color) {
        ImageProcessor imp = this.outlineImage.getProcessor();
        imp.setColor(color);
        imp.resetRoi();
        imp.setRoi(roi);
        ImageProcessor mask = roi.getMask();
        imp.fill(mask);


        if (GrainsJ_.debugging) {
            this.outlineImage.updateAndDraw();
        }
    }

    private void removeRoiOutlinesAndLabels() {
        if(!GrainsJ_.debugging) {
            ImageCanvas imageCanvas = this.outlineImage.getCanvas();
            if (imageCanvas!=null) {
                imageCanvas.setShowAllROIs(false);
            }
        }
    }

}
