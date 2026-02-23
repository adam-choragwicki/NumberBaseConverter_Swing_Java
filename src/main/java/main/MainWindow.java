package main;

import converter.*;
import config.*;
import errorhandling.*;

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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        addComponents();

        setSize(Config.windowWidth, Config.windowHeight);
        setMinimumSize(getSize());
        setVisible(true);
    }

    private void addComponents()
    {
        JLabel labelEnterNumber = new JLabel("Enter number", SwingConstants.CENTER);
        labelEnterNumber.setFont(Config.labelFont);
        labelEnterNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelEnterNumber);

        textFieldEnterNumber = new JTextField();
        textFieldEnterNumber.setFont(Config.textFieldFont);
        textFieldEnterNumber.setHorizontalAlignment(JTextField.CENTER);
        addTextFieldEnterNumberTextChangedListener();
        add(textFieldEnterNumber);

        JLabel labelFromBase = new JLabel("From base");
        labelFromBase.setFont(Config.labelFont);
        labelFromBase.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelFromBase);

        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        comboBoxFromBase = new JComboBox<>(Base.generateSupportedBases());
        comboBoxFromBase.setFont(Config.comboBoxFont);
        comboBoxFromBase.setRenderer(listRenderer);
        comboBoxFromBase.setSelectedItem(new Base(10));
        comboBoxFromBase.addActionListener(e -> processConversion());
        add(comboBoxFromBase);

        JLabel labelToBase = new JLabel("To base");
        labelToBase.setFont(Config.labelFont);
        labelToBase.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelToBase);

        comboBoxToBase = new JComboBox<>(Base.generateSupportedBases());
        comboBoxToBase.setFont(Config.comboBoxFont);
        comboBoxToBase.setRenderer(listRenderer);
        comboBoxToBase.setSelectedItem(new Base(16));
        comboBoxToBase.addActionListener(e -> processConversion());
        add(comboBoxToBase);

        JLabel labelResultNumber = new JLabel("Result number");
        labelResultNumber.setFont(Config.labelFont);
        labelResultNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelResultNumber);

        textFieldResult = new JTextField();
        textFieldResult.setFont(Config.textFieldFont);
        textFieldResult.setHorizontalAlignment(JTextField.CENTER);
        textFieldResult.setEditable(false);
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
                processConversion();
            }
        });
    }

    private void processConversion()
    {
        Base sourceBase = (Base) comboBoxFromBase.getSelectedItem();
        Base targetBase = (Base) comboBoxToBase.getSelectedItem();
        String number = textFieldEnterNumber.getText();

        if (number == null || number.isEmpty())
        {
            textFieldResult.setText("");
            return;
        }

        try
        {
            Converter converter = new Converter(String.valueOf(Objects.requireNonNull(sourceBase).getNumber()), String.valueOf(Objects.requireNonNull(targetBase).getNumber()));
            String result = converter.convertNumber(number);
            textFieldResult.setText(result);
            textFieldEnterNumber.setForeground(Color.black);
        }
        catch (InvalidNumberRepresentationException | InvalidNumberBaseException exception)
        {
            textFieldEnterNumber.setForeground(Color.red);
            textFieldResult.setText("");
        }
    }

    JComboBox<Base> comboBoxFromBase;
    JComboBox<Base> comboBoxToBase;
    JTextField textFieldEnterNumber;
    JTextField textFieldResult;
}
