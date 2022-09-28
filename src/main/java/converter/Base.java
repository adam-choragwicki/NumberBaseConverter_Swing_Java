package converter;

import config.Config;

import java.util.Objects;
import java.util.Vector;

public class Base
{
    public Base(int number)
    {
        this.number = number;

        if (number == 2)
        {
            description = "(binary)";
        }
        else if (number == 8)
        {
            description = "(octal)";
        }
        else if (number == 10)
        {
            description = "(decimal)";
        }
        else if (number == 16)
        {
            description = "(hexadecimal)";
        }
    }

    public int getNumber()
    {
        return number;
    }

    public static Vector<Base> generateSupportedBases()
    {
        Vector<Base> basesSupported = new Vector<>();

        for (int baseNumber = Config.MIN_BASE; baseNumber <= Config.MAX_BASE; ++baseNumber)
        {
            basesSupported.add(new Base(baseNumber));
        }

        return basesSupported;
    }

    @Override
    public String toString()
    {
        if (description != null)
        {
            return number + " " + description;
        }
        else
        {
            return String.valueOf(number);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Base base = (Base) o;

        return number == base.number;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(number);
    }

    private final int number;
    private String description;
}
