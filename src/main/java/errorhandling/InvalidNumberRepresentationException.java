package errorhandling;

public class InvalidNumberRepresentationException extends Exception
{
    public InvalidNumberRepresentationException(String number, int base)
    {
        System.out.printf("Number %s cannot be represented in base %d", number, base);
    }
}
