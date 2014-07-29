import java.awt.Color;


public class DiameterRange {

    private int id;
    private double minimum;
    private double maximum;
    private String unit;
    private Color color;

    public DiameterRange() {
        this(0, 0.0, 100.0, "pixel", RangeColors.next());
    }

    public DiameterRange(int id, double minimum, double maximum,
            String unit, Color color) {
        this.setId(id);
        this.setMinimum(minimum);
        this.setMaximum(maximum);
        this.setUnit(unit);
        this.setColor(color);
    }

    public DiameterRange(int id, double minimum, double maximum) {
        this(id, minimum, maximum, "pixel", RangeColors.next());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.setId(id.intValue());
    }

    public int getId() {
        return id;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public void setMinimum(Double minimum) {
        this.setMinimum(minimum.doubleValue());
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public void setMaximum(Double maximum) {
        this.setMaximum(maximum.doubleValue());
    }

    public double getMaximum() {
        return maximum;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean equals(DiameterRange other) {

        if (id != other.getId()) {
            return false;
        }

        if (minimum != other.getMinimum()) {
            return false;
        }

        if (maximum != other.getMaximum()) {
            return false;
        }

        if (unit != other.getUnit()) {
            return false;
        }

        if (color != other.getColor()) {
            return false;
        }

        return true;
    }
}
