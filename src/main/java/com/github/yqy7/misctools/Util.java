package com.github.yqy7.misctools;

public class Util {
    public static String addPrefixZero(String hexStr, int charCount) {
        StringBuilder stringBuilder = new StringBuilder(hexStr);
        while (stringBuilder.length() < charCount) {
            stringBuilder.insert(0, '0');
        }
        return stringBuilder.toString();
    }

    public static Long decodeLong(String nm) throws NumberFormatException {
        int radix = 10;
        int index = 0;
        boolean negative = false;
        Long result;

        if (nm.length() == 0)
            throw new NumberFormatException("Zero length string");
        char firstChar = nm.charAt(0);
        // Handle sign, if present
        if (firstChar == '-') {
            negative = true;
            index++;
        } else if (firstChar == '+')
            index++;

        // Handle radix specifier, if present
        if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        }
        else if (nm.startsWith("#", index)) {
            index ++;
            radix = 16;
        }
        // 增加二进制的转换
        else if (nm.startsWith("0b", index) || nm.startsWith("0B", index)) {
            index += 2;
            radix = 2;
        }
        else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
            index ++;
            radix = 8;
        }

        if (nm.startsWith("-", index) || nm.startsWith("+", index))
            throw new NumberFormatException("Sign character in wrong position");

        try {
            result = Long.valueOf(nm.substring(index), radix);
            result = negative ? Long.valueOf(-result.longValue()) : result;
        } catch (NumberFormatException e) {
            // If number is Long.MIN_VALUE, we'll end up here. The next line
            // handles this case, and causes any genuine format error to be
            // rethrown.
            String constant = negative ? ("-" + nm.substring(index))
                : nm.substring(index);
            result = Long.valueOf(constant, radix);
        }
        return result;
    }
}
