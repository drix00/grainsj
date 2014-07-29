import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

// TODO: Add option to read rois set file.
public class GrainsOutline {
    private ImagePlus outlineImage = null;
    private double minimumParticleSize = 0.0;
    private boolean includeEdge = false;
    private boolean includePerimeter = true;

    ResultsTable outlineAnalysisResults = null;
    ResultsTable outlineAnalysisAllResults = new ResultsTable();

    RoiManager roiManager = new RoiManager();

    HashMap<Integer, Double> diameters = new HashMap<Integer, Double>();
    ArrayList<Double> sortedDiameters = new ArrayList<Double>();
    ArrayList<Double> cumulativeDiameters = new ArrayList<Double>();

    public GrainsOutline(GrainsJParameters parameters) {
        this.outlineImage = parameters.getOutlineImage();
        this.minimumParticleSize = parameters.getMinimumParticleSize();
        this.includeEdge = parameters.isIncludeEdge();
        this.includePerimeter = parameters.isIncludePerimeter();
    }

    public void analyse() {
        if (this.runParticleAnalyzer()) {
            if (GrainsJ_.logging) {
                String message = "Particle analyze succefull.";
                IJ.log(message);
            }

            diameters = computeDiameter();
            computeCumulativeDiameter();
        }
        else {
            String message = "Error during particle analyze.";
            IJ.log(message);
        }
    }

    private void computeCumulativeDiameter() {
        double sum = 0.0;

        for (Integer key : diameters.keySet()) {
            Double diameter = diameters.get(key);
            sortedDiameters.add(diameter);

            sum += (double)diameter;
        }

        Collections.sort(sortedDiameters);

        double partialTotal = 0.0;
        for (Iterator<Double> iterator = sortedDiameters.iterator(); iterator.hasNext();) {
            Double diameter = iterator.next();
            partialTotal += (double) diameter/sum;
            cumulativeDiameters.add(partialTotal);
        }
    }

    private boolean runParticleAnalyzer() {
        int options = getParticleAnalyzerOptions();

        int measurements = getParticleAnalyzerMeasurements();

        double minSize = this.minimumParticleSize;
        double maxSize = Double.MAX_VALUE;

        ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(options, measurements, this.outlineAnalysisAllResults, minSize, maxSize);

        return particleAnalyzer.analyze(this.outlineImage);
    }

    private int getParticleAnalyzerMeasurements() {
        int measurements = Measurements.AREA;
        measurements |= Measurements.AREA_FRACTION;
        measurements |= Measurements.CENTER_OF_MASS;
        measurements |= Measurements.CENTROID;
        measurements |= Measurements.PERIMETER;
        measurements |= Measurements.RECT;
        measurements |= Measurements.CIRCULARITY;
        measurements |= Measurements.ELLIPSE;
        measurements |= Measurements.FERET;
        measurements |= Measurements.LABELS;
        measurements |= Measurements.SCIENTIFIC_NOTATION;

//        measurements |= Measurements.INVERT_Y;
        return measurements;
    }

    private int getParticleAnalyzerOptions() {
        int options = ParticleAnalyzer.SHOW_PROGRESS;
        options |= options |= ParticleAnalyzer.CLEAR_WORKSHEET;
        options |= ParticleAnalyzer.RECORD_STARTS;
        options |= ParticleAnalyzer.ADD_TO_MANAGER;

        if (!includeEdge) {
            options |= ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES;
        }
//        options |= ParticleAnalyzer.INCLUDE_HOLES;

        if (GrainsJ_.debugging) {
//            options |= ParticleAnalyzer.DISPLAY_SUMMARY;
//            options |= ParticleAnalyzer.SHOW_RESULTS;

//            options |= ParticleAnalyzer.SHOW_NONE;
//            options |= ParticleAnalyzer.SHOW_OUTLINES;
//            options |= ParticleAnalyzer.SHOW_MASKS;
//            options |= ParticleAnalyzer.ELLIPSE;
//            options |= ParticleAnalyzer.SHOW_ROI_MASKS;
        }
        return options;
    }

    public HashMap<Integer, Roi> getRois() {
        HashMap<Integer, Roi> rois = new HashMap<Integer, Roi>();

        this.roiManager = RoiManager.getInstance();
        loggingNumberOfRois();

        Roi [] roisAsArray = this.roiManager.getRoisAsArray();
        loggingNumberOfRoisAsArray(roisAsArray);
        Hashtable<String, Roi> roisAsHashTable = this.roiManager.getROIs();
        logNumberOfRoisAsHashTable(roisAsHashTable);

        testRoisAndRowsOrder();

        int numberOfGrains = this.outlineAnalysisAllResults.getCounter();
        for (int rowIndex=0; rowIndex < numberOfGrains; rowIndex++){
            int x = (int) this.outlineAnalysisAllResults.getValue("X", rowIndex);
            int y = (int) this.outlineAnalysisAllResults.getValue("Y", rowIndex);

            int roiIndex = rowIndex;
            Roi roi = roisAsArray[roiIndex];

            rois.put(Integer.valueOf(rowIndex), roi);

            if (!rois.containsKey(Integer.valueOf(rowIndex))) {
                String message = "Row not added in map: " + Integer.toString(rowIndex);
                message += "\tX " + Integer.toString(x);
                message += "\tY " + Integer.toString(y);
                IJ.log(message);
            }
        }

        loggingNumberOfRoisAsMap(rois);

        return rois;
    }

    private void logNumberOfRoisAsHashTable(Hashtable<String, Roi> roisAsHashTable) {
        if (GrainsJ_.logging) {
            int numberOfRois = roisAsHashTable.size();
            String message = "Number of rois (as HashTable): " + Integer.toString(numberOfRois);
            IJ.log(message);
        }
    }

    private void testRoisAndRowsOrder() {
        if (GrainsJ_.debugging) {
            Roi [] roisAsArray = this.roiManager.getRoisAsArray();

            int numberOfRois = roisAsArray.length;
            int numberOfGrains = this.outlineAnalysisAllResults.getCounter();

            if (GrainsJ_.logging) {
                IJ.log("testRoisAndRowsOrder");
                String message = "Number of rois:\t" + Integer.toString(numberOfRois);
                IJ.log(message);
                message = "Number of grains:\t" + Integer.toString(numberOfGrains);
                IJ.log(message);
            }

            if (numberOfRois == numberOfGrains) {
                for (int rowIndex=0; rowIndex < numberOfGrains; rowIndex++){
                    int x = (int) this.outlineAnalysisAllResults.getValue("X", rowIndex);
                    int y = (int) this.outlineAnalysisAllResults.getValue("Y", rowIndex);

                    Roi roi = roisAsArray[rowIndex];

                    if(roi.contains(x, y) == false){
                        String message = "Grain " + Integer.toString(rowIndex+1) + " not in roi:";
                        message += "\t(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
                        message += "\t" + roi.toString();
                        IJ.log(message);

                    }
                }
            }
        }
    }
    private void loggingNumberOfRoisAsMap(HashMap<Integer, Roi> rois) {
        if (GrainsJ_.logging) {
            int numberOfRois = rois.size();
            String message = "Number of rois (as map): " + Integer.toString(numberOfRois);
            IJ.log(message);
        }
    }

    private void loggingNumberOfRoisAsArray(Roi[] roisAsArray) {
        if (GrainsJ_.logging) {
            int numberOfRois = roisAsArray.length;
            String message = "Number of rois (as array): " + Integer.toString(numberOfRois);
            IJ.log(message);
        }
    }

    private void loggingNumberOfRois() {
        if (GrainsJ_.logging) {
            if (this.roiManager != null) {
               int numberOfRois = this.roiManager.getCount();
               String message = "Number of rois: " + Integer.toString(numberOfRois);
               IJ.log(message);

               message = "First roi name: " + RoiManager.getName("0");
               IJ.log(message);
            }
            else {
                String message = "Can not get roi manager.";
                IJ.log(message);

            }
        }
    }

    public HashMap<Integer, Double> getDiameters() {
        return diameters;
    }

    public ArrayList<Double> getSortedDiameters() {
        return sortedDiameters;
    }

    public ArrayList<Double> getCumulativeDiameters() {
        return sortedDiameters;
    }

    public double[] getSortedDiametersAsArray() {
        return toArray(sortedDiameters);
    }

    public double[] getCumulativeDiametersAsArray() {
        return toArray(cumulativeDiameters);
    }

    private double[] toArray(ArrayList<Double> list) {
        double [] newArray = new double[list.size()];
        int index = 0;
        for (Iterator<Double> iterator = list.iterator(); iterator.hasNext();) {
            double value = iterator.next().doubleValue();
            newArray[index] = value;
            index += 1;
        }

        return newArray;
    }

    private HashMap<Integer, Double> computeDiameter() {
        HashMap<Integer, Double> diameters = new HashMap<Integer, Double>();

        if (GrainsJ_.logging) {
            int numberOfColumns = this.outlineAnalysisAllResults.getLastColumn();
            String message = "Number of columns: " + Integer.toString(numberOfColumns);
            IJ.log(message);
        }

        int numberOfGrains = this.outlineAnalysisAllResults.getCounter();
        String diameterLabel = this.getDiameterLabel();
        int diameterColumnIndex = this.outlineAnalysisAllResults.getFreeColumn(diameterLabel);

        for (int rowIndex = 0; rowIndex < numberOfGrains; rowIndex++) {
            double area = this.outlineAnalysisAllResults.getValue("Area", rowIndex);
            if (includePerimeter) {
                double perimeter = this.outlineAnalysisAllResults.getValue("Perim.", rowIndex);
                area += perimeter;
            }

            double diameter = GrainArea.computeDiameterFromArea(area);
            this.outlineAnalysisAllResults.setValue(diameterColumnIndex, rowIndex, diameter);
            diameters.put(Integer.valueOf(rowIndex), Double.valueOf(diameter));
        }

        return diameters;
    }

    private String getDiameterLabel() {
        String label = "Diameter";

        Calibration calibration = this.outlineImage.getCalibration();
        String unit = calibration.getUnit();
        if (GrainsJ_.logging) {
            String message = "Scale unit: " + unit;
            IJ.log(message);
        }

        label += " (" + unit + ")";
        return label;
    }

    public ResultsTable getOutlineAnalysisTableAll() {
        return this.outlineAnalysisAllResults;
    }

    public ResultsTable getOutlineAnalysisTable() {
        if (this.outlineAnalysisResults == null) {
            this.createDiameterResultsTable();
        }
        return this.outlineAnalysisResults;
    }

    private void createDiameterResultsTable() {
        int numberOfGrains = this.outlineAnalysisAllResults.getCounter();

        if (numberOfGrains > 0) {
            this.outlineAnalysisResults = new ResultsTable();

            String diameterLabel = this.getDiameterLabel();
            int diameterColumnIndex = this.outlineAnalysisResults.getFreeColumn(diameterLabel);
            for (int rowIndex = 0; rowIndex < numberOfGrains; rowIndex++) {
                double diameter = this.outlineAnalysisAllResults.getValue(diameterLabel, rowIndex);
                this.outlineAnalysisResults.incrementCounter();
                this.outlineAnalysisResults.addValue(diameterColumnIndex, diameter);
            }
        }
    }

}
