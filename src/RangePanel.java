import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class RangePanel extends JPanel {
    public class MaximumTextActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFormattedTextField textField = (JFormattedTextField)e.getSource();
            double value = ((Double)textField.getValue()).doubleValue();

            diameterRange.setMaximum(value);
        }

    }

    public class MinimumTextActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFormattedTextField textField = (JFormattedTextField)e.getSource();
            double value = ((Double)textField.getValue()).doubleValue();

            diameterRange.setMinimum(value);
        }

    }

    private DiameterRange diameterRange = null;
    private JLabel idLabel;
    private JFormattedTextField minimumText;
    private JFormattedTextField maximumText;
    private JPanel colorPanel;

    public RangePanel(DiameterRange diameterRange) {
        this.diameterRange = diameterRange;

        createComponents();
        
        setValues();
    }

    public void setDiameterRange(DiameterRange diameterRange) {
        this.diameterRange = diameterRange;
        
        setValues();
    }

    public DiameterRange getDiameterRange() {
        getValues();
        return diameterRange;
    }

    private void createComponents() {
        createNameLabel();

        createMinimumText();

        createToLabel();

        createMaximumText();

        createUnitLabel();

        createColorLabel();
    }

    private void createNameLabel() {
        idLabel = new JLabel("Range "+(this.diameterRange.getId())+" :", SwingConstants.RIGHT);
        this.add(idLabel);
    }

    private void createMinimumText() {
        minimumText = new JFormattedTextField(new Double(diameterRange.getMinimum()));
        minimumText.setColumns(10);
        minimumText.setHorizontalAlignment(SwingConstants.RIGHT);
        minimumText.addActionListener(new MinimumTextActionListener());
        this.add(minimumText);
    }

    private void createToLabel() {
        this.add(new JLabel(" to "));
    }

    private void createMaximumText() {
        maximumText = new JFormattedTextField(new Double(diameterRange.getMaximum()));
        maximumText.setColumns(10);
        maximumText.setHorizontalAlignment(SwingConstants.RIGHT);
        maximumText.addActionListener(new MaximumTextActionListener());
        this.add(maximumText);
    }

    private void createUnitLabel() {
        this.add(new JLabel(this.diameterRange.getUnit()));
    }

    private void createColorLabel() {
        colorPanel = new JPanel();
        colorPanel.setBackground(this.diameterRange.getColor());
        colorPanel.addMouseListener(new ColorMouseListener());
        this.add(colorPanel);
    }
    
    public void setValues() {
        String text = Integer.toString(this.diameterRange.getId());
        idLabel.setText(text);
        minimumText.setValue(this.diameterRange.getMinimum());
        maximumText.setValue(this.diameterRange.getMaximum());
        colorPanel.setBackground(this.diameterRange.getColor());
    }
    
    public void getValues() {
        String text = idLabel.getText();
        this.diameterRange.setId(new Integer(text));
        
        this.diameterRange.setMinimum((Double) minimumText.getValue());
        this.diameterRange.setMaximum((Double) maximumText.getValue());
        this.diameterRange.setColor(colorPanel.getBackground());
    }
    
    class ColorMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            diameterRange.setColor(JColorChooser.showDialog(null, "Choose the range color", diameterRange.getColor()));
            JPanel panel = (JPanel)e.getSource();
            panel.setBackground(diameterRange.getColor());
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
