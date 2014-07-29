import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for default package");
        //$JUnit-BEGIN$
        suite.addTestSuite(GrainsGeometricDistributionFrameTest.class);
        suite.addTestSuite(GrainsJTest.class);
        suite.addTestSuite(DiameterRangesTest.class);
        suite.addTestSuite(GrainsOutlineTest.class);
        suite.addTestSuite(DiameterRangeTest.class);
        suite.addTestSuite(GrainsGeometricDistributionTest.class);
        suite.addTestSuite(RangeColorsTest.class);
        suite.addTestSuite(GrainAreaTest.class);
        suite.addTestSuite(PrepareImageForOutlineAnalysisTest.class);
        suite.addTestSuite(TypeOfDiameterRangeTest.class);
        suite.addTestSuite(GrainsJDialogTest.class);
        suite.addTestSuite(DiameterRangesFactoryTest.class);
        suite.addTestSuite(GrainsJParametersTest.class);
        suite.addTestSuite(RangePanelTest.class);
        //$JUnit-END$
        return suite;
    }

}
