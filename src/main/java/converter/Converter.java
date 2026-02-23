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
    private static final int TARGET_SIGNIFICANT_DIGITS_COUNT = 5;

    public Converter(String sourceBaseString, String targetBaseString) throws InvalidNumberBaseException
    {
        try
        {
            sourceBase = Integer.parseInt(sourceBaseString);
            targetBase = Integer.parseInt(targetBaseString);
        } catch (NumberFormatException numberFormatException)
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
        ParsedNumber parsedNumber = parseAndValidateInput(numberString);

        // preserve sign for inputs like "-0.1" by handling it separately from the magnitude.
        String decimalResultString = convertBaseXToDecimal(parsedNumber.magnitude, sourceBase);
        String result = convertDecimalToBaseX(decimalResultString, targetBase);

        // remove trailing zeros from the fractional part in the final output (e.g., "2.80000" -> "2.8").
        result = trimTrailingZeros(result);
        return applySign(result, parsedNumber.negative);
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
        } catch (NumberFormatException numberFormatException)
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
        } catch (NumberFormatException numberFormatException)
        {
            throw new InvalidNumberRepresentationException(baseXFraction, sourceBase);
        }

        // avoid int overflow when checking for zero in large fractional parts.
        if (fractionalPartAsInteger.signum() == 0)
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

        if (inputFractionalPart.equals(BigDecimal.ZERO))
        {
            // avoid fixed-width zero padding; a zero fraction should yield no digits.
            return "";
        }

        int significantDigitsCount = 0;

        while ((inputFractionalPart.compareTo(BigDecimal.ZERO) > 0) && significantDigitsCount < TARGET_SIGNIFICANT_DIGITS_COUNT)
        {
            inputFractionalPart = inputFractionalPart.multiply(targetBaseBig);

            char baseXDigit = Character.toUpperCase(Character.forDigit(inputFractionalPart.intValue(), targetBaseBig.intValue()));
            resultBaseXFractionalPart.append(baseXDigit);

            if (inputFractionalPart.compareTo(BigDecimal.ONE) >= 0)
            {
                // if the resulting number is >= 1.0, subtract the integer part
                inputFractionalPart = inputFractionalPart.subtract(new BigDecimal(inputFractionalPart.toBigInteger()));
            }

            ++significantDigitsCount;
        }

        return resultBaseXFractionalPart.toString();
    }

    private ParsedNumber parseAndValidateInput(String numberString) throws InvalidNumberRepresentationException
    {
        if (numberString == null || numberString.isEmpty())
        {
            // reject empty inputs
            throw new InvalidNumberRepresentationException(String.valueOf(numberString), sourceBase);
        }

        boolean negative = false;
        int startIndex = 0;
        char firstChar = numberString.charAt(0);

        if (firstChar == '-' || firstChar == '+')
        {
            negative = firstChar == '-';
            startIndex = 1;
        }

        String magnitude = getString(numberString, startIndex);

        return new ParsedNumber(negative, magnitude);
    }

    private String getString(String numberString, int startIndex) throws InvalidNumberRepresentationException
    {
        String magnitude = numberString.substring(startIndex);

        if (magnitude.isEmpty())
        {
            // reject sign-only input.
            throw new InvalidNumberRepresentationException(numberString, sourceBase);
        }

        int firstDotIndex = magnitude.indexOf('.');
        if (firstDotIndex != -1)
        {
            // reject leading/trailing dot and multiple dots.
            if (firstDotIndex == 0 || firstDotIndex == magnitude.length() - 1 || magnitude.indexOf('.', firstDotIndex + 1) != -1)
            {
                throw new InvalidNumberRepresentationException(numberString, sourceBase);
            }
        }
        return magnitude;
    }

    private String applySign(String magnitude, boolean negative)
    {
        if (!negative || isZeroMagnitude(magnitude))
        {
            return magnitude;
        }

        return "-" + magnitude;
    }

    private boolean isZeroMagnitude(String magnitude)
    {
        for (int i = 0; i < magnitude.length(); ++i)
        {
            char c = magnitude.charAt(i);

            if (c != '.' && c != '0')
            {
                return false;
            }
        }

        return true;
    }

    private String trimTrailingZeros(String number)
    {
        int dotIndex = number.indexOf('.');
        if (dotIndex == -1)
        {
            return number;
        }

        int endIndex = number.length() - 1;
        while (endIndex > dotIndex && number.charAt(endIndex) == '0')
        {
            --endIndex;
        }

        // if the fractional part is all zeros, drop the dot entirely.
        if (endIndex == dotIndex)
        {
            return number.substring(0, dotIndex);
        }

        return number.substring(0, endIndex + 1);
    }

    private record ParsedNumber(boolean negative, String magnitude)
    {
    }

    private int sourceBase;
    private int targetBase;
}
