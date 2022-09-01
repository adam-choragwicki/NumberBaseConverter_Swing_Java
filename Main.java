package converter;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws InvalidNumberBaseException
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

    static void runTests() throws InvalidNumberBaseException
    {
        testConversion("2", "10", "101", "5");
        testConversion("3", "10", "101", "10");
        testConversion("4", "10", "101", "17");
        testConversion("5", "10", "101", "26");
        testConversion("6", "10", "101", "37");
        testConversion("7", "10", "101", "50");
        testConversion("8", "10", "101", "65");
        testConversion("9", "10", "101", "82");
        testConversion("10", "10", "101", "101");
        testConversion("11", "10", "101", "122");
        testConversion("12", "10", "101", "145");
        testConversion("13", "10", "101", "170");
        testConversion("14", "10", "101", "197");
        testConversion("15", "10", "101", "226");
        testConversion("16", "10", "101", "257");
        testConversion("20", "10", "101", "401");
        testConversion("25", "10", "101", "626");
        testConversion("30", "10", "101", "901");
        testConversion("36", "10", "101", "1297");

        /* Letters as digits */
        testConversion("20", "10", "abchj", "1693159");
        testConversion("25", "10", "abchno", "102151849");
        testConversion("30", "10", "tabc", "792342");
        testConversion("36", "10", "zxcv", "1676191");

        /* Mix of numbers and letters */
        testConversion("36", "10", "zXcX", "1676193");
        testConversion("36", "10", "Xa5X", "1552821");

        /* Fractions to decimal*/
        testConversion("5", "10", "123.4321", "38.93760");
        testConversion("10", "10", "123456.789", "123456.78900");
        testConversion("15", "10", "0.789", "0.50488");
        testConversion("36", "10", "abcd.52a", "481261.14064");

        /* Fractions other than decimal */
        testConversion("5", "8", "2431.33324", "556.57716");
        testConversion("5", "11", "12.23340", "7.60664");

        testConversion("11", "32", "784.58189816588", "tb.gmhgj");
        testConversion("14", "23", "38c.741da886a0d061", "17m.bmfdi");

        testConversion("17", "5", "328.7171fe6cg8g0c4433", "12114.20202");
        testConversion("17", "35", "g7.7c27ff2a8f1928fa2", "7y.fuv74");
    }

    static void testConversion(String sourceBaseString, String targetBaseString, String numberString, String expectedResult) throws InvalidNumberBaseException
    {
        try
        {
            System.out.printf("%s in base %s is equal to %s in base %s\n", numberString, sourceBaseString, expectedResult, targetBaseString);
            System.out.println("testing...");

            Converter converter = new Converter(sourceBaseString, targetBaseString);
            String result = converter.convertNumber(numberString);

            if (result.equals(expectedResult))
            {
                System.out.println("CORRECT");
            }
            else
            {
                System.out.println("WRONG, result is " + result);
                System.out.println("Expected output is " + expectedResult);
            }
        }
        catch (InvalidNumberBaseException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            System.out.println("EXCEPTION");
        }
        finally
        {
            System.out.println();
        }
    }
}
