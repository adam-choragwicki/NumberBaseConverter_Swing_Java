package converter;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Converter
{
    public String convertBaseXToDecimal(final String inputNumberString, final int sourceBase) throws WrongNumberRepresentationException
    {
        if (inputNumberString.contains("."))
        {
            String[] numberParts = inputNumberString.split("\\.");

            String resultDecimalIntegerPart = convertBaseXIntegerToDecimalInteger(numberParts[0], sourceBase);
            String resultDecimalFractionalPart = convertBaseXFractionToDecimalFraction(numberParts[1], sourceBase);

            return resultDecimalIntegerPart + "." + resultDecimalFractionalPart.substring(2);
        }
        else
        {
            return convertBaseXIntegerToDecimalInteger(inputNumberString, sourceBase);
        }
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

    private String convertBaseXFractionToDecimalFraction(final String baseXFraction, int sourceBase)
    {
        BigDecimal sourceBaseBig = BigDecimal.valueOf(sourceBase);

        MathContext precision = new MathContext(7);

        BigDecimal factor = BigDecimal.ONE.divide(sourceBaseBig, precision);
        BigDecimal decimalFractionalPart = BigDecimal.ZERO;

        BigInteger fractionalPartAsInteger = new BigInteger(baseXFraction, sourceBase);

        if (fractionalPartAsInteger.intValue() == 0)
        {
            decimalFractionalPart = new BigDecimal("0.0");
        }
        else
        {
            for (int i = 0, power = 1; i < baseXFraction.length(); ++i, ++power)
            {
                BigDecimal digit = BigDecimal.valueOf(Character.digit(baseXFraction.charAt(i), sourceBase));
                BigDecimal powerOfBase = factor.pow(power);
                BigDecimal digitMultipliedByPowerOfBase = digit.multiply(powerOfBase);

                decimalFractionalPart = decimalFractionalPart.add(digitMultipliedByPowerOfBase);
            }
        }

        decimalFractionalPart = decimalFractionalPart.setScale(6, RoundingMode.HALF_DOWN);

        return decimalFractionalPart.toString();
    }
}

class WrongNumberRepresentationException extends Exception
{
    public WrongNumberRepresentationException()
    {
        super();
    }
}
