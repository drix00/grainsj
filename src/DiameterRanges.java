import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;


public class DiameterRanges {
    ArrayList<DiameterRange> diameterRanges = new ArrayList<DiameterRange>();

    public DiameterRanges() {
        this(0);
    }

    public DiameterRanges(int numberElements) {
        RangeColors.reset();

        for (int index=0; index<numberElements; index++) {
//            int id = index+1;
//            double minimum = 0.0;
//            double maximum = 100.0;
//            String unit = "pixel";
//            Color color = Color.BLUE;
//            DiameterRange diameterRange = new DiameterRange(id, minimum, maximum,
//                                                            unit, color);
//            diameterRanges.add(diameterRange);
            this.add(100.0);
        }
    }
    public int getSize() {
        return diameterRanges.size();
    }

    public void setSize(int numberElement) {
        if (numberElement <= 0) {
            this.removeAll();
            return;
        }

        int actualNumberElement = this.getSize();
        if (actualNumberElement < numberElement) {
            for (int i=actualNumberElement; i < numberElement; i++) {
                this.add(100.0);
            }
        } else if (actualNumberElement > numberElement) {
            for (int i=actualNumberElement-1; i >= numberElement; i--) {
                this.remove();
            }
        }
    }

    public DiameterRange get(int index) {
        return diameterRanges.get(index);
    }

    public void set(int index, DiameterRange diameterRange) {
        diameterRanges.set(index, diameterRange);
    }

    public void add(DiameterRange diameterRange) {
        diameterRanges.add(diameterRange);
    }

    public DiameterRange add(double step) {
        int id = diameterRanges.size()+1;
        double minimum = 0.0;
        if (id > 1) {
            minimum = diameterRanges.get(id-2).getMaximum();
        }
        double maximum = minimum + step;
        String unit = "pixel";
        Color color = RangeColors.next();
        DiameterRange diameterRange = new DiameterRange(id, minimum, maximum, unit, color);

        diameterRanges.add(diameterRange);

        return diameterRange;
    }

    public void remove() {
        int index = diameterRanges.size()-1;
        diameterRanges.remove(index);
    }

    public void removeAll() {
        diameterRanges.clear();
    }

    public Color getColor(double diameter) {
        for (Iterator iterator = diameterRanges.iterator(); iterator.hasNext();) {
            DiameterRange diameterRange = (DiameterRange) iterator.next();

            if (diameter < diameterRange.getMinimum()) {
                return diameterRange.getColor();
            }

            if ((diameterRange.getMinimum() <= diameter) && (diameter < diameterRange.getMaximum())) {
                return diameterRange.getColor();
            }
        }
        int index = diameterRanges.size()-1;
        Color lastColor = diameterRanges.get(index).getColor();
        return lastColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiameterRanges) {
            return this.equals((DiameterRanges)obj);
        }

        return super.equals(obj);
    }
    public boolean equals(DiameterRanges other) {

        if (diameterRanges.size() != other.getSize()) {
            return false;
        }

        for (int index = 0; index < diameterRanges.size(); index++) {
            if (!diameterRanges.get(index).equals(other.get(index))){
                return false;
            }

        }

        return true;
    }
}
