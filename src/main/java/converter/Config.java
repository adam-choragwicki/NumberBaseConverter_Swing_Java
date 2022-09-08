package converter;

import java.awt.*;

record Config()
{
    static Font labelFont = new Font("Default", Font.BOLD, 30);
    static Font textFieldFont = new Font("Default", Font.PLAIN, 20);
    static Font comboBoxFont = new Font("Default", Font.PLAIN, 25);
    static int MIN_BASE = 2;
    static int MAX_BASE = 36;
}
