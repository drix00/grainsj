import java.awt.Color;
import java.util.prefs.Preferences;

import ij.ImagePlus;


public class GrainsJParameters {
    private static final String NUMBER_COLORS = "numberColors";
    private int numberColors = 3;

    private static final String TYPE_OF_DIAMETER_RANGE = "typeOfDiameterRange";
    private TypeOfDiameterRange typeOfDiameterRange = TypeOfDiameterRange.LINEAR;

    private static final String INCLUDE_EDGE = "includeEdge";
    private boolean includeEdge = false;

    private static final String INCLUDE_PERIMETER = "includePerimeter";
    private boolean includePerimeter = true;

    private static final String MINIMUM_PARTICLE_SIZE = "minimumParticleSize";
    private double minimumParticleSize = 0.0;

    //private static final String DIAMETER_RANGES_NUMBER = "DiameterRangesNumber";
    private static final String DIAMETER_RANGES_MINIMUM = "DiameterRangesMinium_";
    private static final String DIAMETER_RANGES_MAXIMUM = "DiameterRangesMaximum_";
    private static final String DIAMETER_RANGES_COLOR = "DiameterRangesColor_";
    private DiameterRanges diameterRanges = new DiameterRanges();

    private static final String BACKGROUND_COLOR = "BackgroundColor";
    private Color backgroundColor = Color.BLACK;
    private static final String OUTLINE_COLOR = "OutlineColor";
    private Color outlineColor = Color.WHITE;

    private ImagePlus outlineImage = null;
    private ImagePlus originalImage = null;

    private String imageUnit = "pixel";

    public GrainsJParameters(ImagePlus outlineImage, ImagePlus originalImage) {
        this.read();

        this.setOutlineImage(outlineImage);
        this.setOriginalImage(originalImage);
    }

    public void setDefaultValues() {
        this.numberColors = 3;
        this.typeOfDiameterRange = TypeOfDiameterRange.MANUAL;
        this.includeEdge = false;
        this.includePerimeter = true;
        this.minimumParticleSize = 0.0;
    }

    public ImagePlus getOutlineImage() {
        return this.outlineImage;
    }

    public void setOutlineImage(ImagePlus outlineImage) {
        this.outlineImage = outlineImage;
    }

    public ImagePlus getOriginalImage() {
        return this.originalImage;
    }

    public void setOriginalImage(ImagePlus originalImage) {
        this.originalImage = originalImage;
    }

    public void setIncludeEdge(boolean includeEdge) {
        this.includeEdge = includeEdge;
    }

    public boolean isIncludeEdge() {
        return includeEdge;
    }

    public void setIncludePerimeter(boolean includePerimeter) {
        this.includePerimeter = includePerimeter;
    }

    public boolean isIncludePerimeter() {
        return includePerimeter;
    }

    public void setNumberColors(int numberColors) {
        this.numberColors = numberColors;
        this.diameterRanges.setSize(numberColors);
    }

    public int getNumberColors() {
        return numberColors;
    }

    public void setTypeOfDiameterRange(TypeOfDiameterRange typeOfDiameterRange) {
        this.typeOfDiameterRange = typeOfDiameterRange;
    }

    public void setTypeOfDiameterRange(String typeOfDiameterRange) {
        for (TypeOfDiameterRange type : TypeOfDiameterRange.values()){
            if (typeOfDiameterRange.equals(type.getLabel())) {
                this.setTypeOfDiameterRange(type);
            }
        }
    }

    public TypeOfDiameterRange getTypeOfDiameterRange() {
        return typeOfDiameterRange;
    }

    public void setMinimumParticleSize(double minimumParticleSize) {
        this.minimumParticleSize = minimumParticleSize;
    }

    public double getMinimumParticleSize() {
        return minimumParticleSize;
    }

    public void setDiameterRanges(DiameterRanges diameterRanges) {
        this.diameterRanges = diameterRanges;
        this.setNumberColors(this.diameterRanges.getSize());
    }

    public DiameterRanges getDiameterRanges() {
        return this.diameterRanges;
    }

    public void setImageUnit(String imageUnit) {
        this.imageUnit = imageUnit;
    }

    public String getImageUnit() {
        return imageUnit;
    }

    public void read() {
        Preferences prefs = Preferences.userNodeForPackage(GrainsJParameters.class);

        setNumberColors(prefs.getInt(NUMBER_COLORS, numberColors));

        String type = prefs.get(TYPE_OF_DIAMETER_RANGE, typeOfDiameterRange.getLabel());
        setTypeOfDiameterRange(type);

        setIncludeEdge(prefs.getBoolean(INCLUDE_EDGE, includeEdge));

        setIncludePerimeter(prefs.getBoolean(INCLUDE_PERIMETER, includePerimeter));

        setMinimumParticleSize(prefs.getDouble(MINIMUM_PARTICLE_SIZE, minimumParticleSize));

        int numberDiameterRanges = numberColors;

        diameterRanges = new DiameterRanges(numberDiameterRanges);
        for (int index = 0; index < numberDiameterRanges; index++) {
            int id = index+1;
            String suffix = Integer.toString(id);

            DiameterRange diameterRange = new DiameterRange();
            diameterRange.setId(id);

            String key = DIAMETER_RANGES_MINIMUM + suffix;
            double minimum = prefs.getDouble(key, diameterRange.getMinimum());
            diameterRange.setMinimum(minimum);

            key = DIAMETER_RANGES_MAXIMUM + suffix;
            double maximum = prefs.getDouble(key, diameterRange.getMaximum());
            diameterRange.setMaximum(maximum);

            key = DIAMETER_RANGES_COLOR + suffix;
            String colorText = RangeColors.getColorName(diameterRange.getColor());
            colorText = prefs.get(key, colorText);
            Color color = RangeColors.getColorByName(colorText);
            diameterRange.setColor(color);

            diameterRanges.set(index, diameterRange);
        }

        String key = BACKGROUND_COLOR;
        String colorText = RangeColors.getColorName(this.getBackgroundColor());
        colorText = prefs.get(key, colorText);
        Color color = RangeColors.getColorByName(colorText);
        this.setBackgroundColor(color);

        key = OUTLINE_COLOR;
        colorText = RangeColors.getColorName(this.getOutlineColor());
        colorText = prefs.get(key, colorText);
        color = RangeColors.getColorByName(colorText);
        this.setOutlineColor(color);
    }

    public void save() {
        Preferences prefs = Preferences.userNodeForPackage(GrainsJParameters.class);

        prefs.putInt(NUMBER_COLORS, numberColors);
        prefs.put(TYPE_OF_DIAMETER_RANGE, typeOfDiameterRange.getLabel());
        prefs.putBoolean(INCLUDE_EDGE, includeEdge);
        prefs.putBoolean(INCLUDE_PERIMETER, includePerimeter);
        prefs.putDouble(MINIMUM_PARTICLE_SIZE, minimumParticleSize);

        int numberDiameterRanges = numberColors;

        for (int index = 0; index < numberDiameterRanges; index++) {
            int id = index+1;
            String suffix = Integer.toString(id);

            DiameterRange diameterRange = diameterRanges.get(index);

            String key = DIAMETER_RANGES_MINIMUM + suffix;
            prefs.putDouble(key, diameterRange.getMinimum());

            key = DIAMETER_RANGES_MAXIMUM + suffix;
            prefs.putDouble(key, diameterRange.getMaximum());

            key = DIAMETER_RANGES_COLOR + suffix;
            String colorText = RangeColors.getColorName(diameterRange.getColor());
            prefs.put(key, colorText);
        }

        String key = BACKGROUND_COLOR;
        String colorText = RangeColors.getColorName(this.getBackgroundColor());
        prefs.put(key, colorText);

        key = OUTLINE_COLOR;
        colorText = RangeColors.getColorName(this.getOutlineColor());
        prefs.put(key, colorText);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color color) {
        this.outlineColor = color;
    }

}
