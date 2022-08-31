package converter;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        boolean test = false;

        if (test)
        {
            runTests();
        }
        else
        {
            Scanner scanner = new Scanner(System.in);
            Converter converter = new Converter();

            while (true)
            {
                System.out.print("Enter number to be converted to decimal in format: {source base} {number} ");
                String sourceBaseString = scanner.next();
                String numberString = scanner.next();

                try
                {
                    int sourceBase = parseSourceBase(sourceBaseString);

                    String result = converter.convertBaseXToDecimal(numberString, sourceBase);
                    System.out.print("Conversion result: " + result);
                }
                catch (WrongNumberRepresentationException wrongNumberRepresentationException)
                {
                    System.out.print("Wrong representation for number in given base");
                }
                catch (WrongNumberBaseException wrongNumberBaseException)
                {
                    System.out.print("Wrong source base, expected integer");
                }
                finally
                {
                    System.out.println();
                    System.out.println();
                }
            }
        }
    }

    private static int parseSourceBase(String sourceBaseString) throws WrongNumberBaseException
    {
        try
        {
            int sourceBase = Integer.parseInt(sourceBaseString);

            final int MIN_BASE = 2;
            final int MAX_BASE = 36;

            if (sourceBase >= MIN_BASE && sourceBase <= MAX_BASE)
            {
                return sourceBase;
            }
            else
            {
                System.out.printf("Wrong source base %d. Expected integer from %d to %d", sourceBase, MIN_BASE, MAX_BASE);
            }
        }
        catch (NumberFormatException numberFormatException)
        {
            throw new WrongNumberBaseException();
        }

        return -1;
    }

    static void runTests()
    {
        testConversion("2", "101", "5");
        testConversion("3", "101", "10");
        testConversion("4", "101", "17");
        testConversion("5", "101", "26");
        testConversion("6", "101", "37");
        testConversion("7", "101", "50");
        testConversion("8", "101", "65");
        testConversion("9", "101", "82");
        testConversion("10", "101", "101");
        testConversion("11", "101", "122");
        testConversion("12", "101", "145");
        testConversion("13", "101", "170");
        testConversion("14", "101", "197");
        testConversion("15", "101", "226");
        testConversion("16", "101", "257");
        testConversion("20", "101", "401");
        testConversion("25", "101", "626");
        testConversion("30", "101", "901");
        testConversion("36", "101", "1297");

        /* Letters as digits */
        testConversion("20", "abchj", "1693159");
        testConversion("25", "abchno", "102151849");
        testConversion("30", "tabc", "792342");
        testConversion("36", "zxcv", "1676191");

        /* Mix of numbers and letters */
        testConversion("36", "zXcX", "1676193");
        testConversion("36", "Xa5X", "1552821");
    }

    static void testConversion(String sourceBaseString, String numberString, String expectedResult)
    {
        Converter converter = new Converter();

        System.out.printf("%s in base %s is equal to %s in decimal\n", numberString, sourceBaseString, expectedResult);
        System.out.println("testing...");

        try
        {
            String result = converter.convertBaseXToDecimal(numberString, Integer.parseInt(sourceBaseString));

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

class WrongNumberBaseException extends Exception
{
    public WrongNumberBaseException()
    {
        super();
    }
}
