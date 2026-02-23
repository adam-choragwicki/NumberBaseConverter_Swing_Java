package converter;

import errorhandling.InvalidNumberBaseException;
import errorhandling.InvalidNumberRepresentationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.util.Scanner;

class ConverterTest
{
    @Test
    @DisplayName("Zero conversion")
    void convertZero() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Scanner scanner = readTestDataFile("/zero_conversion_test_data.txt");

        while (scanner.hasNext())
        {
            TestConversionData testConversionData = readTestConversionData(scanner);
            testConversion(testConversionData);
        }
    }

    @Test
    @DisplayName("Base X to decimal conversion")
    void convertBaseXToDecimal() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Scanner scanner = readTestDataFile("/base_x_to_decimal_conversion_test_data.txt");

        while (scanner.hasNext())
        {
            TestConversionData testConversionData = readTestConversionData(scanner);
            testConversion(testConversionData);
        }
    }

    @Test
    @DisplayName("Decimal to base X conversion")
    void convertDecimalToBaseX() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Scanner scanner = readTestDataFile("/decimal_to_base_x_conversion_test_data.txt");

        while (scanner.hasNext())
        {
            TestConversionData testConversionData = readTestConversionData(scanner);
            testConversion(testConversionData);
        }
    }

    @Test
    @DisplayName("Base X integer to base Y integer conversion")
    void convertInteger() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Scanner scanner = readTestDataFile("/base_x_to_base_y_integer_conversion_test_data.txt");

        while (scanner.hasNext())
        {
            TestConversionData testConversionData = readTestConversionData(scanner);
            testConversion(testConversionData);
        }
    }

    @Test
    @DisplayName("Base X integer to base Y fractional conversion")
    void convertFractional() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Scanner scanner = readTestDataFile("/base_x_to_base_y_fractional_conversion_test_data.txt");

        while (scanner.hasNext())
        {
            TestConversionData testConversionData = readTestConversionData(scanner);
            testConversion(testConversionData);
        }
    }

    @Test
    @DisplayName("Invalid number base exception")
    void throwOnWrongBase()
    {
        assertThrows(InvalidNumberBaseException.class, () -> new Converter("1", "5"));
        assertThrows(InvalidNumberBaseException.class, () -> new Converter("2", "37"));
        assertThrows(InvalidNumberBaseException.class, () -> new Converter("0", "-5"));
        assertThrows(InvalidNumberBaseException.class, () -> new Converter("0", "100"));
    }

    @Test
    @DisplayName("Invalid number representation exception")
    void throwOnInvalidNumberRepresentation() throws InvalidNumberBaseException
    {
        Converter converter = new Converter("2", "10");
        assertThrows(InvalidNumberRepresentationException.class, () -> converter.convertNumber("123"));
    }

    private Scanner readTestDataFile(String inputTestFilePath)
    {
        InputStream inputStream = ConverterTest.class.getResourceAsStream(inputTestFilePath);
        assert inputStream != null;
        return new Scanner(inputStream);
    }

    private void testConversion(TestConversionData testConversionData) throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Converter converter = new Converter(testConversionData.sourceBaseString, testConversionData.targetBaseString);
        String result = converter.convertNumber(testConversionData.numberString);

        assertEquals(normalizeExpected(testConversionData.expectedResult), result);
    }

    private record TestConversionData(String sourceBaseString, String targetBaseString, String numberString, String expectedResult)
    {

    }

    private TestConversionData readTestConversionData(Scanner scanner)
    {
        return new TestConversionData(scanner.next(), scanner.next(), scanner.next(), scanner.next());
    }

    private String normalizeExpected(String expected)
    {
        int dotIndex = expected.indexOf('.');
        if (dotIndex == -1)
        {
            return expected;
        }

        int endIndex = expected.length() - 1;
        while (endIndex > dotIndex && expected.charAt(endIndex) == '0')
        {
            --endIndex;
        }

        if (endIndex == dotIndex)
        {
            return expected.substring(0, dotIndex);
        }

        return expected.substring(0, endIndex + 1);
    }

    @Test
    @DisplayName("No trailing zeros in fractional output")
    void noTrailingZerosInOutput() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Converter converter = new Converter("10", "16");
        assertEquals("2.8", converter.convertNumber("2.5"));

        Converter converterBase10 = new Converter("10", "10");
        assertEquals("2", converterBase10.convertNumber("2.0"));
    }

    @Test
    @DisplayName("Preserve negative sign for fractional values")
    void preservesNegativeFractionalSign() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Converter converter = new Converter("10", "10");
        assertEquals("-0.1", converter.convertNumber("-0.1"));
    }

    @Test
    @DisplayName("Exact 1.0 fractional step")
    void exactOneFractionalStep() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Converter converter = new Converter("10", "2");
        assertEquals("0.1", converter.convertNumber("0.5"));
    }

    @Test
    @DisplayName("Reject malformed fractional inputs")
    void rejectMalformedFractionalInputs() throws InvalidNumberBaseException
    {
        Converter converter = new Converter("10", "10");
        assertThrows(InvalidNumberRepresentationException.class, () -> converter.convertNumber("1."));
        assertThrows(InvalidNumberRepresentationException.class, () -> converter.convertNumber("1.2.3"));
        assertThrows(InvalidNumberRepresentationException.class, () -> converter.convertNumber(".1"));
    }

    @Test
    @DisplayName("Large fractional part should not be treated as zero")
    void largeFractionalPartNotZero() throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        Converter converter = new Converter("10", "10");
        assertEquals("0.42949", converter.convertNumber("0.4294967296"));
    }
}
