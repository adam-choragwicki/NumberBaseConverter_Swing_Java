package converter;

import java.math.BigInteger;

public class Converter
{
    public String convertBaseXToDecimal(final String inputNumberString, final int sourceBase) throws WrongNumberRepresentationException
    {
        return convertBaseXIntegerToDecimalInteger(inputNumberString, sourceBase);
    }

    private String convertBaseXIntegerToDecimalInteger(String baseXInteger, int sourceBase) throws WrongNumberRepresentationException
    {
        try
        {
            BigInteger integer = new BigInteger(baseXInteger, sourceBase);
            return integer.toString();
        }
        catch(NumberFormatException numberFormatException)
        {
            throw new WrongNumberRepresentationException();
        }
    }
}

class WrongNumberRepresentationException extends Exception
{
    public WrongNumberRepresentationException()
    {
        super();
    }
}
