package converter;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Objects;

public class MainWindow extends JFrame
{
    public MainWindow()
    {
        super("Number base converter");
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350, 350));
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        addComponents();

        pack();
        setVisible(true);
    }

    private void addComponents()
    {
        JLabel labelEnterNumber = new JLabel("Enter number");
        add(labelEnterNumber);

        textFieldEnterNumber = new JTextField();
        addTextFieldEnterNumberTextChangedListener();
        add(textFieldEnterNumber);

        JLabel labelFromBase = new JLabel("From base");
        add(labelFromBase);

        comboBoxFromBase = new JComboBox<>(Base.generateSupportedBases());
        comboBoxFromBase.setSelectedItem(new Base(10));
        add(comboBoxFromBase);

        JLabel labelToBase = new JLabel("To base");
        add(labelToBase);

        comboBoxToBase = new JComboBox<>(Base.generateSupportedBases());
        comboBoxToBase.setSelectedItem(new Base(16));
        add(comboBoxToBase);

        JButton buttonConvert = new JButton("Convert");
        buttonConvert.addActionListener(e -> processButtonConvertClick());
        add(buttonConvert);

        JLabel labelResultNumber = new JLabel("Result number");
        add(labelResultNumber);

        textFieldResult = new JTextField();
        add(textFieldResult);
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

    JComboBox<Base> comboBoxFromBase;
    JComboBox<Base> comboBoxToBase;
    JTextField textFieldEnterNumber;
    JTextField textFieldResult;
}
