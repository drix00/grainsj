import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import junit.framework.TestCase;


public class GrainsOutlineTest extends TestCase {
    GrainsJParameters parameters = null;
    private GrainsOutline grainsOutline;
    private static final int NUMBER_GRAINS = 86;

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();

        String imagesFilepath = "./images/OutlineSmallPreProcessed.tif";
        ImagePlus outlineImage = new ImagePlus(imagesFilepath);
        parameters = new GrainsJParameters(outlineImage, null);
        parameters.setDefaultValues();
        
        grainsOutline = new GrainsOutline(parameters);
        grainsOutline.analyse();
    }

    public void testGrainsOutline() {
    }

    public void testAnalyse() {
        HashMap<Integer, Double> diameters = grainsOutline.getDiameters();

        assertEquals(NUMBER_GRAINS, diameters.size());
    }

    public void testGetRois() {
        HashMap<Integer, Roi> rois = grainsOutline.getRois();

        assertEquals(NUMBER_GRAINS, rois.size());
    }

    public void testGetDiameters() {
        HashMap<Integer, Double> diameters = grainsOutline.getDiameters();
        assertEquals(NUMBER_GRAINS, diameters.size());

        boolean isOrdered = checkOrderedList(new ArrayList<Double>(diameters.values()));
        assertFalse(isOrdered);
    }

    public void testGetSortedDiameters() {
        ArrayList<Double> sortedDiameters = grainsOutline.getSortedDiameters();
        assertEquals(NUMBER_GRAINS, sortedDiameters.size());

        boolean isOrdered = checkOrderedList(sortedDiameters);
        assertTrue(isOrdered);
    }

    private boolean checkOrderedList(ArrayList<Double> sortedDiameters) {
        boolean isOrdered = true;
        Iterator iterator = sortedDiameters.iterator();
        Double smaller = (Double) iterator.next();
        while (iterator.hasNext()) {
            Double bigger = (Double) iterator.next();

            isOrdered = isOrdered & (smaller <= bigger);
            smaller = bigger;
        }

        return isOrdered;
    }

    public void testGetCumulativeDiameters() {
        ArrayList<Double> cumulativeDiameters = grainsOutline.getCumulativeDiameters();
        assertEquals(NUMBER_GRAINS, cumulativeDiameters.size());

        boolean isOrdered = checkOrderedList(cumulativeDiameters);
        assertTrue(isOrdered);
    }

    public void testGetSortedDiametersAsArray() {
        double[] sortedDiameters = grainsOutline.getSortedDiametersAsArray();
        assertEquals(NUMBER_GRAINS, sortedDiameters.length);

        boolean isOrdered = checkOrderedList(sortedDiameters);
        assertTrue(isOrdered);
    }

    private boolean checkOrderedList(double[] sortedDiameters) {
        boolean isOrdered = true;
        for (int index=0; index < sortedDiameters.length-1; index++) {
            double smaller = sortedDiameters[index];
            double bigger = sortedDiameters[index+1];

            isOrdered = isOrdered & (smaller <= bigger);
        }

        return isOrdered;
    }

    public void testGetCumulativeDiametersAsArray() {
        double[] cumulativeDiameters = grainsOutline.getCumulativeDiametersAsArray();
        assertEquals(NUMBER_GRAINS, cumulativeDiameters.length);

        boolean isOrdered = checkOrderedList(cumulativeDiameters);
        assertTrue(isOrdered);
    }

    public void testGetOutlineAnalysisTableAll() {
        ResultsTable resultsTable = grainsOutline.getOutlineAnalysisTableAll();
        assertEquals(NUMBER_GRAINS, resultsTable.getCounter());
    }

    public void testGetOutlineAnalysisTable() {
        ResultsTable resultsTable = grainsOutline.getOutlineAnalysisTable();
        assertEquals(NUMBER_GRAINS, resultsTable.getCounter());
    }

}
