package com.safitech.homeworx.utility;

public class StringUtils {
    public static String toInitCap(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
