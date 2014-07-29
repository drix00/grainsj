import java.util.HashMap;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.PlotWindow;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

// TODO - Add option to choose color.
// TODO - Save rois.
// TODO - Save results table.
// TODO - Save only diameter results.
// TODO - Analyze only from the roi.
// TODO - Use the scale if set.

public class GrainsJ_ implements PlugIn {

    static final boolean logging = false;
    static final boolean debugging = false;

    private GrainsJParameters parameters = new GrainsJParameters(null, null);

    ResultsTable outlineAnalysisResults = new ResultsTable();

    public void run(String arg) {
        if (runDialog(arg)) {
            runAnalysis();
        }
    }

    /**
     * @return Return false if cancel, true otherwise.
     */
    public boolean runDialog(String arg) {
        GrainsJDialog grainsJDialog = new GrainsJDialog(this.parameters);

        return grainsJDialog.run(arg);
    }

    private void runAnalysis() {
        setImageUnit();
        PrepareImageForOutlineAnalysis prepareOutlineImage = new PrepareImageForOutlineAnalysis(this.parameters.getOutlineImage());
        prepareOutlineImage.run();

        GrainsOutline grainsOutline = new GrainsOutline(this.parameters);

        grainsOutline.analyse();

        if(GrainsJ_.debugging) {
            HashMap<Integer, Roi> rois = grainsOutline.getRois();
            testRoisAndResultsTable(rois);
        }

        this.outlineAnalysisResults = grainsOutline.getOutlineAnalysisTable();

        this.outlineAnalysisResults.show("Grain size analysis");

        GrainsGeometricDistributionFrame grainsGeometricDistributionFrame = new GrainsGeometricDistributionFrame(this.parameters, grainsOutline);
        grainsGeometricDistributionFrame.setVisible(true);

        double[] sortedDiameters = grainsOutline.getSortedDiametersAsArray();
        double[] cumulativeDiameters = grainsOutline.getCumulativeDiametersAsArray();

        PlotWindow plotWindow = new PlotWindow("Cumulative distribution"
                                               , "Diameter"
                                               , "Cumulative", sortedDiameters, cumulativeDiameters);
        plotWindow.setLineWidth(4);
        plotWindow.draw();
    }

    private void setImageUnit() {
        Calibration calibration = this.parameters.getOutlineImage().getCalibration();
        String imageUnit = calibration.getUnit();
        this.parameters.setImageUnit(imageUnit);
    }

    private void testRoisAndResultsTable(HashMap<Integer,Roi> rois) {
        int numberOfGrains = this.outlineAnalysisResults.getCounter();
        int numberOfRois = rois.size();

        if (numberOfGrains == numberOfRois) {
            for (int rowIndex=0; rowIndex < numberOfGrains; rowIndex++){
                int x = (int) this.outlineAnalysisResults.getValue("X", rowIndex);
                int y = (int) this.outlineAnalysisResults.getValue("Y", rowIndex);

                if(rois.get(Integer.valueOf(rowIndex)).contains(x, y) == false){
                    String message = "Not in roi: " + Integer.toString(rowIndex);
                    IJ.log(message);
                }

            }
        }
    }

    static public void updateImage(ImagePlus image) {
        if (GrainsJ_.debugging) {
            image.updateAndDraw();
        }
    }

    /**
     * @param imp
     */
    static public void logAutoThresholdValue(ImageProcessor imp) {
        if (GrainsJ_.logging) {
            int thresholdValue = imp.getAutoThreshold();
            String message = "Auto threshold value: " + Integer.toString(thresholdValue);
            IJ.log(message);
        }
    }

    /**
     * @param imp
     * @param when
     */
    static public void logBlackWhiteCounts(ImageProcessor imp, String when) {
        if (GrainsJ_.logging) {
            int[] histogram = imp.getHistogram();

            int blackCounts = histogram[0];
            String message = "Black counts " + when + " invertion: " + Integer.toString(blackCounts);
            IJ.log(message);

            int whiteCounts = histogram[255];
            message = "White counts " + when + " invertion: " + Integer.toString(whiteCounts);
            IJ.log(message);
        }
    }

}
