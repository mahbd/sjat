package com.sajjadsjat.utils;

public class H {
    public static int stringToNumber(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
