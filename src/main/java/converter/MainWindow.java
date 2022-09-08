package converter;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Objects;
import java.util.Vector;

public class MainWindow extends JFrame
{
    public MainWindow()
    {
        super("Number base converter");
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350, 350));

        final Vector<Base> basesSupported = generateSupportedBasesStrings();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel labelEnterNumber = new JLabel("Enter number");
        mainPanel.add(labelEnterNumber);

        textFieldEnterNumber = new JTextField();
        addTextFieldEnterNumberTextChangedListener();
        mainPanel.add(textFieldEnterNumber);

        JLabel labelFromBase = new JLabel("From base");
        mainPanel.add(labelFromBase);

        comboBoxFromBase = new JComboBox<>(basesSupported);
        comboBoxFromBase.setSelectedItem(new Base(10));
        mainPanel.add(comboBoxFromBase);

        JLabel labelToBase = new JLabel("To base");
        mainPanel.add(labelToBase);

        comboBoxToBase = new JComboBox<>(basesSupported);
        comboBoxToBase.setSelectedItem(new Base(16));
        mainPanel.add(comboBoxToBase);

        JButton buttonConvert = new JButton("Convert");
        buttonConvert.addActionListener(e -> processButtonConvertClick());
        mainPanel.add(buttonConvert);

        JLabel labelResultNumber = new JLabel("Result number");
        mainPanel.add(labelResultNumber);

        textFieldResult = new JTextField();
        mainPanel.add(textFieldResult);

        add(mainPanel);

        pack();
        setVisible(true);
    }

    private void addTextFieldEnterNumberTextChangedListener()
    {
        textFieldEnterNumber.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                processUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                processUpdate();
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                processUpdate();
            }

            private void processUpdate()
            {
                if (textFieldEnterNumber.getForeground() != Color.black)
                {
                    textFieldEnterNumber.setForeground(Color.black);
                }
            }
        });
    }

    private void processButtonConvertClick()
    {
        Base sourceBase = (Base) comboBoxFromBase.getSelectedItem();
        Base targetBase = (Base) comboBoxToBase.getSelectedItem();
        String number = textFieldEnterNumber.getText();

        if (number.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please enter number to be converted");
        }
        else
        {
            try
            {
                Converter converter = new Converter(String.valueOf(Objects.requireNonNull(sourceBase).getNumber()), String.valueOf(Objects.requireNonNull(targetBase).getNumber()));
                String result = converter.convertNumber(number);
                textFieldResult.setText(result);
            }
            catch (InvalidNumberRepresentationException | InvalidNumberBaseException exception)
            {
                textFieldEnterNumber.setForeground(Color.red);
                JOptionPane.showMessageDialog(this, "Number %s cannot be represented in base %s".formatted(number, sourceBase), "", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    JComboBox<Base> comboBoxFromBase;
    JComboBox<Base> comboBoxToBase;
    JTextField textFieldEnterNumber;
    JTextField textFieldResult;
}
