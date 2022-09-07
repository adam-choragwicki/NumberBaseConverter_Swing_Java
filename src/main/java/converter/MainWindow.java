package converter;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class MainWindow extends JFrame
{
    public MainWindow()
    {
        super("Number base converter");
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350, 350));

        Vector<Base> basesSupported = generateSupportedBasesStrings();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel labelEnterNumber = new JLabel("Enter number");
        mainPanel.add(labelEnterNumber);

        JTextField textFieldEnterNumber = new JTextField();
        mainPanel.add(textFieldEnterNumber);

        JLabel labelFromBase = new JLabel("From base");
        mainPanel.add(labelFromBase);

        JComboBox<Base> comboBoxFromBase = new JComboBox<>(basesSupported);
        comboBoxFromBase.setSelectedItem(new Base(10));
        mainPanel.add(comboBoxFromBase);

        JLabel labelToBase = new JLabel("To base");
        mainPanel.add(labelToBase);

        JComboBox<Base> comboBoxToBase = new JComboBox<>(basesSupported);
        comboBoxToBase.setSelectedItem(new Base(16));
        mainPanel.add(comboBoxToBase);

        JButton buttonConvert = new JButton("Convert");
        mainPanel.add(buttonConvert);

        JLabel labelResultNumber = new JLabel("Result number");
        mainPanel.add(labelResultNumber);

        JTextField textFieldResult = new JTextField();
        mainPanel.add(textFieldResult);

        buttonConvert.addActionListener(e ->
        {
            Base sourceBase = (Base) comboBoxFromBase.getSelectedItem();
            Base targetBase = (Base) comboBoxToBase.getSelectedItem();
            String number = textFieldEnterNumber.getText();

            if(number.isEmpty())
            {
                textFieldEnterNumber.setBackground(Color.red);
                return;
            }
            else
            {
                textFieldEnterNumber.setBackground(Color.white);
            }

            try
            {
                textFieldEnterNumber.setForeground(Color.black);

                String result = new Converter(String.valueOf(sourceBase.getNumber()), String.valueOf(targetBase.getNumber())).convertNumber(number);
                textFieldResult.setText(result);
            }
            catch (InvalidNumberRepresentationException | InvalidNumberBaseException exception)
            {
                textFieldEnterNumber.setForeground(Color.red);
            }
        });

        add(mainPanel);

        pack();
        setVisible(true);
    }

    private Vector<Base> generateSupportedBasesStrings()
    {
        Vector<Base> basesSupported = new Vector<>();

        for (int baseNumber = Config.MIN_BASE; baseNumber <= Config.MAX_BASE; ++baseNumber)
        {
            basesSupported.add(new Base(baseNumber));
        }

        return basesSupported;
    }
}
