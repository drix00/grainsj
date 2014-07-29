
public class DiameterRangesFactory {
    static public DiameterRanges createLinearRanges(int numberElements, double minimum
            , double maximum) {

        DiameterRanges diameterRanges = new DiameterRanges();

        double step = (maximum - minimum)/numberElements;
        double min = minimum;
        for (int index=0; index < numberElements; index++) {
            int id = index + 1;

            double max = min + step;
            DiameterRange range = new DiameterRange(id, min, max);

            diameterRanges.add(range);

            min = max;
        }

        return diameterRanges;
    }

    static public DiameterRanges createLogarithmicRanges(int numberElements, double minimum
            , double maximum) {
        if (minimum < 1.0) {
            minimum = 1.0;
        }

        DiameterRanges diameterRanges = new DiameterRanges();

        double logMinimum = Math.log(minimum);
        double logMaximum = Math.log(maximum);
        double nominator = logMaximum - logMinimum;
        double denominator = numberElements;
        double step = nominator/denominator;

        double min = logMinimum;
        for (int index=0; index < numberElements; index++) {
            int id = index + 1;

            double max = min + step;
            DiameterRange range = new DiameterRange(id, Math.exp(min), Math.exp(max));

            diameterRanges.add(range);

            min = max;
        }

        return diameterRanges;
    }
}
