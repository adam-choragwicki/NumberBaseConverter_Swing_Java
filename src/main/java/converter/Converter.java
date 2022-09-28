package converter;

import config.Config;
import errorhandling.InvalidNumberBaseException;
import errorhandling.InvalidNumberRepresentationException;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Converter
{
    public Converter(String sourceBaseString, String targetBaseString) throws InvalidNumberBaseException
    {
        try
        {
            sourceBase = Integer.parseInt(sourceBaseString);
            targetBase = Integer.parseInt(targetBaseString);
        }
        catch (NumberFormatException numberFormatException)
        {
            throwInvalidNumberBase();
        }

        if (!(sourceBase >= Config.MIN_BASE && sourceBase <= Config.MAX_BASE) || !(targetBase >= Config.MIN_BASE && targetBase <= Config.MAX_BASE))
        {
            throwInvalidNumberBase();
        }
    }

    public String convertNumber(String numberString) throws InvalidNumberRepresentationException
    {
        String decimalResultString = convertBaseXToDecimal(numberString, sourceBase);
        return convertDecimalToBaseX(decimalResultString, targetBase);
    }

    private void throwInvalidNumberBase() throws InvalidNumberBaseException
    {
        System.out.printf("Wrong number base. Expected integer in range <%d,%d>", Config.MIN_BASE, Config.MAX_BASE);
        throw new InvalidNumberBaseException();
    }

    private String convertBaseXToDecimal(final String inputNumberString, final int sourceBase) throws InvalidNumberRepresentationException
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

    private String convertBaseXIntegerToDecimalInteger(String baseXInteger, int sourceBase) throws InvalidNumberRepresentationException
    {
        try
        {
            BigInteger integer = new BigInteger(baseXInteger, sourceBase);
            return integer.toString();
        }
        catch (NumberFormatException numberFormatException)
        {
            throw new InvalidNumberRepresentationException(baseXInteger, sourceBase);
        }
    }

    private String convertBaseXFractionToDecimalFraction(final String baseXFraction, int sourceBase) throws InvalidNumberRepresentationException
    {
        BigDecimal sourceBaseBig = BigDecimal.valueOf(sourceBase);

        MathContext precision = new MathContext(7);

        BigDecimal factor = BigDecimal.ONE.divide(sourceBaseBig, precision);
        BigDecimal decimalFractionalPart = BigDecimal.ZERO;

        BigInteger fractionalPartAsInteger;

        try
        {
            fractionalPartAsInteger = new BigInteger(baseXFraction, sourceBase);
        }
        catch (NumberFormatException numberFormatException)
        {
            throw new InvalidNumberRepresentationException(baseXFraction, sourceBase);
        }

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

    private String convertDecimalToBaseX(final String inputDecimalString, final int targetBase)
    {
        if (inputDecimalString.contains("."))
        {
            String[] numberParts = inputDecimalString.split("\\.");

            String resultBaseXIntegerPart = convertDecimalIntegerToBaseXInteger(numberParts[0], targetBase);
            String resultBaseXFractionalPart = convertDecimalFractionToBaseXFraction(numberParts[1], targetBase);

            return resultBaseXIntegerPart + "." + resultBaseXFractionalPart;
        }
        else
        {
            return convertDecimalIntegerToBaseXInteger(inputDecimalString, targetBase);
        }
    }

    private String convertDecimalIntegerToBaseXInteger(String decimalInteger, int targetBase)
    {
        return new BigInteger(decimalInteger).toString(targetBase).toUpperCase();
    }

    private String convertDecimalFractionToBaseXFraction(String decimalFraction, int targetBase)
    {
        BigDecimal targetBaseBig = BigDecimal.valueOf(targetBase);

        StringBuilder resultBaseXFractionalPart = new StringBuilder();

        BigDecimal inputFractionalPart = new BigDecimal("0." + decimalFraction);

        /* If value of fractional digits is 0 */
        if (inputFractionalPart.equals(BigDecimal.ZERO))
        {
            resultBaseXFractionalPart.append("00000");
        }
        else
        {
            final int TARGET_SIGNIFICANT_DIGITS_COUNT = 5;
            int significantDigitsCount = 0;

            while ((inputFractionalPart.compareTo(BigDecimal.ZERO) > 0) && significantDigitsCount < 5)
            {
                inputFractionalPart = inputFractionalPart.multiply(targetBaseBig);

                char baseXDigit = Character.toUpperCase(Character.forDigit(inputFractionalPart.intValue(), targetBaseBig.intValue()));
                resultBaseXFractionalPart.append(baseXDigit);

                /* If resulting number is higher than 1.0, subtract the integer part */
                if (inputFractionalPart.compareTo(BigDecimal.ONE) > 0)
                {
                    inputFractionalPart = inputFractionalPart.subtract(new BigDecimal(inputFractionalPart.toBigInteger()));
                }

                ++significantDigitsCount;
            }

            /* Fill resulting fraction up with zeroes up to TARGET_SIGNIFICANT_DIGITS_COUNT */
            if (significantDigitsCount != TARGET_SIGNIFICANT_DIGITS_COUNT)
            {
                resultBaseXFractionalPart.append("0".repeat(TARGET_SIGNIFICANT_DIGITS_COUNT - significantDigitsCount));
            }
        }

        return resultBaseXFractionalPart.toString();
    }

    private int sourceBase;
    private int targetBase;
}
