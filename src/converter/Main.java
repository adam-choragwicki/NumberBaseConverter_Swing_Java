package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws InvalidNumberBaseException, InvalidNumberRepresentationException, FileNotFoundException
    {
        boolean test = false;

        if (test)
        {
            runTests();
        }
        else
        {
            Scanner scanner = new Scanner(System.in);

            while (true)
            {
                System.out.print("Enter number to be converted in format: {source base} {target base} {number} ");

                String sourceBaseString = scanner.next();
                String targetBaseString = scanner.next();
                String numberString = scanner.next();

                try
                {
                    Converter converter = new Converter(sourceBaseString, targetBaseString);

                    String result = converter.convertNumber(numberString);
                    System.out.print("Conversion result: " + result);
                }
                catch (InvalidNumberBaseException | InvalidNumberRepresentationException ignored)
                {

                }
                finally
                {
                    System.out.print("\n\n");
                }
            }
        }
    }

    static void runTests() throws InvalidNumberBaseException, InvalidNumberRepresentationException, FileNotFoundException
    {
        String[] inputTestFilePaths = new String[]{"tests/base_x_to_decimal_conversion_tests.txt", "tests/decimal_to_base_x_conversion_tests.txt", "tests/integer_conversion_tests.txt", "tests/fractional_conversion_tests.txt"};

        int correctCount = 0;
        int incorrectCount = 0;

        for (String inputTestFilePath : inputTestFilePaths)
        {
            File file = new File(inputTestFilePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext())
            {
                String sourceBaseString = scanner.next();
                String targetBaseString = scanner.next();
                String numberString = scanner.next();
                String expectedResult = scanner.next();

                if (testConversion(sourceBaseString, targetBaseString, numberString, expectedResult))
                {
                    ++correctCount;
                }
                else
                {
                    ++incorrectCount;
                }
            }
        }

        System.out.println("Correct: " + correctCount);
        System.out.println("Incorrect: " + incorrectCount);
    }

    static boolean testConversion(String sourceBaseString, String targetBaseString, String numberString, String expectedResult) throws InvalidNumberBaseException, InvalidNumberRepresentationException
    {
        System.out.printf("%s in base %s is equal to %s in base %s\n", numberString, sourceBaseString, expectedResult, targetBaseString);
        System.out.println("testing...");

        Converter converter = new Converter(sourceBaseString, targetBaseString);
        String result = converter.convertNumber(numberString);

        if (result.equals(expectedResult))
        {
            System.out.println("Correct");
            System.out.println();
            return true;
        }
        else
        {
            System.out.println("WRONG, result is " + result);
            System.out.println("Expected output is " + expectedResult);
            System.out.println();
            return false;
        }
    }
}
