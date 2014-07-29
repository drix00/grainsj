import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ij.IJ;
import ij.gui.Roi;
import ij.plugin.frame.PlugInFrame;
import ij.process.ImageProcessor;

public class GrainsGeometricDistributionFrame extends PlugInFrame {
    GrainsJParameters parameters;
    HashMap<Integer, Roi> rois;
    HashMap<Integer, Double> diameters;
    //DiameterRanges diameterRanges = new DiameterRanges();

    protected Container optionsBox = Box.createVerticalBox();
    protected Container colorRangesBox = Box.createVerticalBox();
    protected JButton closeButton;
    protected JButton applyButton;
    protected JSpinner numberColorsSpinner;
    protected JComboBox rangeTypeComboBox;
    private ColorPanel backgroundColorPanel;
    private ColorPanel outlineColorPanel;

    private ImageProcessor outlineMask;

    public GrainsGeometricDistributionFrame(GrainsJParameters parameters, GrainsOutline grainsOutline) {
        super("Grains geometric distribution");

        this.parameters = parameters;

        rois = grainsOutline.getRois();
        diameters = grainsOutline.getDiameters();

        outlineMask = getOutlineMask();

        createOptions();

        createButtons();

        setValues();

        this.pack();
    }

    private ImageProcessor getOutlineMask() {
        ImageProcessor mask = this.parameters.getOutlineImage().getProcessor().convertToByte(false);
        return mask;
    }

    private void createLinearRanges(int numberElements, HashMap<Integer, Double> diameters) {
        double minimum = GrainsGeometricDistribution.computeMinimumDiameter(diameters);
        double maximum = GrainsGeometricDistribution.computeMaximumDiameter(diameters);

        this.parameters.setDiameterRanges(DiameterRangesFactory.createLinearRanges(numberElements, minimum, maximum));
    }

    private void createLogarithmicRanges(int numberElements, HashMap<Integer, Double> diameters) {
        double minimum = GrainsGeometricDistribution.computeMinimumDiameter(diameters);
        double maximum = GrainsGeometricDistribution.computeMaximumDiameter(diameters);

        this.parameters.setDiameterRanges(DiameterRangesFactory.createLogarithmicRanges(numberElements, minimum, maximum));
    }

    private void createOptions() {
        createRangeTypeComboBox();

        createNumberColorsSpinner();

        createColorRanges();

        createBackgroundLineColors();

        this.add(optionsBox, BorderLayout.CENTER);
    }

    private void createRangeTypeComboBox() {
        JPanel panel = new JPanel();
        rangeTypeComboBox = new JComboBox();

        for (TypeOfDiameterRange type : TypeOfDiameterRange.values()){
            rangeTypeComboBox.addItem(type.getLabel());
        }
        rangeTypeComboBox.addActionListener(new RangeTypeAction());
        String rangeTypeLabel = this.parameters.getTypeOfDiameterRange().getLabel();
        rangeTypeComboBox.setSelectedItem(rangeTypeLabel);

        panel.add(new JLabel("Color range type: ", SwingConstants.RIGHT));
        panel.add(rangeTypeComboBox);
        optionsBox.add(panel);
    }

    private void createNumberColorsSpinner() {
        JPanel panel = new JPanel();
        int initial = this.parameters.getNumberColors();
        int min = 1;
        int max = Integer.MAX_VALUE;
        int increment = 1;
        SpinnerNumberModel model = new SpinnerNumberModel(initial, min, max, increment);
        numberColorsSpinner = new JSpinner(model);
        model.addChangeListener(new NumberColorsChange());

        panel.add(new JLabel("Number of colors: ", SwingConstants.RIGHT));
        panel.add(numberColorsSpinner);
        optionsBox.add(panel);
    }

    private void createColorRanges() {
        updateColorRanges();

        optionsBox.add(colorRangesBox);
    }

    private void updateColorRanges() {
        TypeOfDiameterRange type = this.getTypeOfDiameterRange();
        if (type == TypeOfDiameterRange.MANUAL) {
            colorRangesBox.setEnabled(true);
        } else if (type == TypeOfDiameterRange.LINEAR) {
            createLinearRanges(this.parameters.getNumberColors(), diameters);
            colorRangesBox.setEnabled(false);
        } else if (type == TypeOfDiameterRange.LOGARITHMIC) {
            createLogarithmicRanges(this.parameters.getNumberColors(), diameters);
            colorRangesBox.setEnabled(false);
        }

        int numberColors = this.getNumberColors();
        int numberPanels = colorRangesBox.getComponentCount();
        DiameterRanges diameterRanges = this.parameters.getDiameterRanges();
        if (numberPanels < numberColors) {
            for (int i=numberPanels; i < numberColors; i++) {
                DiameterRange diameterRange = diameterRanges.get(i);
                RangePanel panel = new RangePanel(diameterRange);
                colorRangesBox.add(panel);
            }
        } else if (numberPanels > numberColors) {
            for (int i=numberPanels-1; i >= numberColors; i--) {
                colorRangesBox.remove(i);
            }
        }

        if (type == TypeOfDiameterRange.MANUAL) {
            numberPanels = colorRangesBox.getComponentCount();
            for (int index=0; index < numberPanels; index++) {
                RangePanel panel = (RangePanel)colorRangesBox.getComponent(index);
                DiameterRange diameterRange = panel.getDiameterRange();
                diameterRanges.set(index, diameterRange);
            }

            this.parameters.setDiameterRanges(diameterRanges);
        } else {
            numberPanels = colorRangesBox.getComponentCount();
            for (int index=0; index < numberPanels; index++) {
                RangePanel panel = (RangePanel)colorRangesBox.getComponent(index);
                DiameterRange diameterRange = diameterRanges.get(index);
                panel.setDiameterRange(diameterRange);
            }
        }

        this.validate();
        this.pack();
    }

    private void createBackgroundLineColors() {
        backgroundColorPanel = new ColorPanel("Background color: ", this.parameters.getBackgroundColor());
        optionsBox.add(backgroundColorPanel);

        outlineColorPanel = new ColorPanel("Outline color: ", this.parameters.getOutlineColor());
        optionsBox.add(outlineColorPanel);
    }

    public TypeOfDiameterRange getTypeOfDiameterRange() {
        return this.parameters.getTypeOfDiameterRange();
    }

    public int getNumberColors() {
        return this.parameters.getNumberColors();
    }

    public DiameterRanges getDiameterRanges() {
        return this.parameters.getDiameterRanges();
    }

    private void createButtons() {
        JPanel buttonPanel = new JPanel();
        applyButton = new JButton("Apply");
        closeButton = new JButton("Close");

        buttonPanel.add(applyButton);
        buttonPanel.add(closeButton);

        applyButton.addActionListener(new ButtonApplyAction());
        closeButton.addActionListener(new ButtonCloseAction());

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setValues() {
        numberColorsSpinner.setValue(this.parameters.getNumberColors());

        int numberPanels = colorRangesBox.getComponentCount();
        for (int index=0; index < numberPanels; index++) {
            RangePanel panel = (RangePanel)colorRangesBox.getComponent(index);
            DiameterRange diameterRange = this.parameters.getDiameterRanges().get(index);
            panel.setDiameterRange(diameterRange);
            panel.setValues();
        }
    }

    public void getValues() {
        Integer value = (Integer)numberColorsSpinner.getValue();
        this.parameters.setNumberColors(value.intValue());

        DiameterRanges diameterRanges = this.parameters.getDiameterRanges();
        int numberPanels = colorRangesBox.getComponentCount();
        for (int index=0; index < numberPanels; index++) {
            RangePanel panel = (RangePanel)colorRangesBox.getComponent(index);
            panel.getValues();
            DiameterRange diameterRange = panel.getDiameterRange();
            diameterRanges.set(index, diameterRange);
        }

        this.parameters.setDiameterRanges(diameterRanges);

        this.parameters.setBackgroundColor(backgroundColorPanel.getColor());
        this.parameters.setOutlineColor(outlineColorPanel.getColor());
    }

    class RangeTypeAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String typeOfDiameterRange = (String)((JComboBox)event.getSource()).getSelectedItem();
            IJ.log("typeOfDiameterRange: " + typeOfDiameterRange);
            parameters.setTypeOfDiameterRange(typeOfDiameterRange);

            updateColorRanges();
        }
    }

    class NumberColorsChange implements ChangeListener {
        public void stateChanged(ChangeEvent event) {
            int numberColors = ((SpinnerNumberModel)event.getSource()).getNumber().intValue();
            parameters.setNumberColors(numberColors);

            updateColorRanges();
        }
    }

    class ButtonCloseAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            close();
        }
    }

    class ButtonApplyAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            getValues();
            parameters.save();

            GrainsGeometricDistribution grainsGeometricDistribution = new GrainsGeometricDistribution(parameters, outlineMask);

            grainsGeometricDistribution.fillGrains(rois, diameters);

        }
    }

    @Override
    public void close() {
        parameters.save();
        super.close();
    }
}
