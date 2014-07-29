import junit.framework.TestCase;


public class GrainAreaTest extends TestCase {

    double expectedRadius = 2.0;
    double area = this.expectedRadius*this.expectedRadius*Math.PI;
    double expectedDiameter = 4.0;
    
    double zeroArea = 0.0;
    double expectedZeroDiameter = 0.0;
    double expectedZeroRadius = 0.0;
    
    double negativeArea = -this.area;
    
    public void testComputeDiameterFromArea() {
        double actualDiameter = GrainArea.computeDiameterFromArea(this.area);
        assertEquals(this.expectedDiameter, actualDiameter, 0.01);
    }

    public void testComputeDiameterFromAreaZeroArea() {
        double actualDiameter = GrainArea.computeDiameterFromArea(this.zeroArea);
        assertEquals(this.expectedZeroDiameter, actualDiameter, 0.01);
    }

    public void testComputeDiameterFromAreaNegativeArea() {
        double actualDiameter = GrainArea.computeDiameterFromArea(this.negativeArea);
        assertEquals(this.expectedZeroDiameter, actualDiameter, 0.01);
    }

    public void testComputeRadiusFromArea() {
        double actualRadius = GrainArea.computeRadiusFromArea(this.area);
        assertEquals(this.expectedRadius, actualRadius, 0.01);
    }

    public void testComputeRadiusFromAreaZeroArea() {
        double actualRadius = GrainArea.computeRadiusFromArea(this.area);
        assertEquals(this.expectedRadius, actualRadius, 0.01);
    }

    public void testComputeRadiusFromAreaNegativeArea() {
        double actualRadius = GrainArea.computeRadiusFromArea(this.negativeArea);
        assertEquals(this.expectedZeroRadius, actualRadius, 0.01);
    }

    public void testComputeDiameterFromRadius() {
        double actualDiameter = GrainArea.computeDiameterFromRadius(this.expectedRadius);
        assertEquals(this.expectedDiameter, actualDiameter, 0.01);
    }

    public void testComputeDiameterFromRadiusZeroRadius() {
        double actualDiameter = GrainArea.computeDiameterFromRadius(this.expectedZeroRadius);
        assertEquals(this.expectedZeroDiameter, actualDiameter, 0.01);
    }

    public void testComputeDiameterFromRadiusNegativeRadius() {
        double negativeRadius = -2.0;
        double expectedDiameterForNegativeRadius = -4.0;
        
        double actualDiameter = GrainArea.computeDiameterFromRadius(negativeRadius);
        assertEquals(expectedDiameterForNegativeRadius, actualDiameter, 0.01);
    }

}
