package com.dtstep.lighthouse.core.test.utils;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    private static final Pattern columnExtractPattern = Pattern.compile("(?![^']*'[^']*(?:'[^']*')*$)\\b(?=\\w*[a-zA-Z])\\w+\\b(?!\\s*\\()");

    @Test
    public void test() throws Exception {
        String s = "--action;page;dateFormat333('regTime','yyyy-MM-dd')";
        String a = replaceQuotedContent(s);
        System.out.println("a is:" + a);
        Matcher matcher = columnExtractPattern.matcher(a);
        while (matcher.find()) {
            String match = matcher.group();
            boolean isValidMatch = isValidMatch(match);
            if(isValidMatch){
                System.out.println("match:" + match);
            }
        }
        System.out.println("s is:" + s);
    }

    public static String replaceQuotedContent(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\'') {
                if (!inQuotes) {
                    inQuotes = true;
                    result.append('-');
                } else {
                    inQuotes = false;
                }
            } else {
                if (inQuotes) {
                    result.append('-');
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    private static boolean isValidMatch(String match) {
        return match.matches(".*[a-zA-Z].*");
    }
}
