package md.leonis.assistant.source.dsl.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.geeksforgeeks.org/converting-roman-numerals-decimal-lying-1-3999/
// Upgraded with UTF-8 ALL ROMAN NUMERAL (U+2160 - U+2188), up to 100000+, but untested yet
public class RomanToNumber {

    private static final Logger log = LoggerFactory.getLogger(RomanToNumber.class);

    // This function returns value of a Roman symbol
    private static int value(char chr) {
        char uchr = Character.toUpperCase(chr);
        if (uchr == 'I' || uchr == 'Ⅰ')
            return 1;
        if (uchr == 'Ⅱ')
            return 2;
        if (uchr == 'Ⅲ')
            return 3;
        if (uchr == 'Ⅳ')
            return 4;
        if (uchr == 'V' || uchr == 'Ⅴ')
            return 5;
        if (uchr == 'Ⅵ' || uchr == 'ↅ')
            return 6;
        if (uchr == 'Ⅶ')
            return 7;
        if (uchr == 'Ⅷ')
            return 8;
        if (uchr == 'Ⅸ')
            return 9;
        if (uchr == 'X' || uchr == 'Ⅹ')
            return 10;
        if (uchr == 'Ⅺ')
            return 11;
        if (uchr == 'Ⅻ')
            return 12;
        if (uchr == 'ↆ')
            return 15;
        if (uchr == 'L' || uchr == 'Ⅼ')
            return 50;
        if (uchr == 'C' || uchr == 'Ⅽ')
            return 100;
        if (uchr == 'D' || uchr == 'Ⅾ')
            return 500;
        if (uchr == 'M' || uchr == 'Ⅿ' || uchr == 'ↀ' || uchr == 'Ↄ')
            return 1000;
        if (uchr == 'ↁ')
            return 5000;
        if (uchr == 'ↂ')
            return 10000;
        if (uchr == 'ↇ')
            return 10000;
        if (uchr == 'ↈ')
            return 100000;
        log.warn("Incorrect char: " + uchr);
        return -1;
    }

    public static boolean isValidRomanNumeral(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (value(str.charAt(i)) < 0) {
                return false;
            }
        }
        return true;
    }

    // Finds decimal value of a given roman numeral
    public static int romanToDecimal(String str) {
        // Initialize result
        int res = 0;

        for (int i = 0; i < str.length(); i++) {
            // Getting value of symbol s[i]
            int s1 = value(str.charAt(i));

            // Getting value of symbol s[i+1]
            if (i + 1 < str.length()) {
                int s2 = value(str.charAt(i + 1));

                // Comparing both values
                if (s1 >= s2) {
                    // Value of current symbol is greater or equal to the next symbol
                    res = res + s1;
                } else {
                    res = res + s2 - s1;
                    i++; // Value of current symbol is less than the next symbol
                }
            } else {
                res = res + s1;
                i++;
            }
        }

        return res;
    }
}
