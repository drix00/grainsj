
public class GrainArea {

    public static double computeDiameterFromArea(double area) {
        double radius = computeRadiusFromArea(area);
        double diameter = computeDiameterFromRadius(radius);
        return diameter;
    }

    public static double computeRadiusFromArea(double area) {
        if (area > 0.0) {
            double squaredRadius = area/Math.PI;
            double radius = Math.sqrt(squaredRadius);
            return radius;
        }
        
        return 0.0;
    }

    public static double computeDiameterFromRadius(double radius) {
        return 2.0*radius;
    }
    
}
