
public enum TypeOfDiameterRange {
    MANUAL ("Manual"),
    LINEAR ("Linear"),
    LOGARITHMIC ("Logarithmic");
    private String label;
    
    TypeOfDiameterRange (String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
