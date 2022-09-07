package converter;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        runConverterGuiVersion();
    }

    private static void runConverterGuiVersion()
    {
        new MainWindow();
    }

    private static void runConverterTextVersion()
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
