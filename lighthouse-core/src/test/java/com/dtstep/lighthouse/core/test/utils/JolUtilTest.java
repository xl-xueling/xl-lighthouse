package com.dtstep.lighthouse.core.test.utils;

import com.dtstep.lighthouse.core.expression.embed.AviatorHandler;
import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JolUtilTest {

    private static final Pattern PATTERN = Pattern.compile("(?<!')\\b([a-zA-Z_][a-zA-Z0-9_]*)(?=\\s*\\(|\\s*[,;]|$)");

    /**
     * 查找符合条件的所有匹配
     * @param text 输入文本
     * @return 匹配结果列表
     */
    public static List<String> findMatches(String text) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(text);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }



    public static List<String> extractValidIdentifiers(String text) {
        // 正则表达式匹配符合要求的标识符
        String regex = "(?<!')\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b(?=\\s*\\(|\\s*[,;]|$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        List<String> matches = new ArrayList<>();

        // 查找所有符合条件的匹配
        while (matcher.find()) {
            String match = matcher.group(1);
            if (match.matches(".*[a-zA-Z].*")) { // 至少包含一个字母
                matches.add(match);
            }
        }

        // 过滤出符合条件的最长标识符
        List<String> longestMatches = new ArrayList<>();
        int maxLength = 0;

        for (String match : matches) {
            // 如果字符串后面是左括号，则跳过
            if (text.contains(match + "(")) {
                continue; // 跳过以左括号结尾的标识符
            }

            // 更新最长标识符
            if (match.length() > maxLength) {
                longestMatches.clear();
                longestMatches.add(match);
                maxLength = match.length();
            } else if (match.length() == maxLength) {
                longestMatches.add(match);
            }
        }

        // 从函数调用中提取有效标识符
        Matcher functionMatcher = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(").matcher(text);
        while (functionMatcher.find()) {
            String param = functionMatcher.group(1); // 提取函数参数名
            longestMatches.add(param); // 添加函数名
        }

        // 最终筛选出最长标识符
        List<String> finalMatches = new ArrayList<>();
        maxLength = 0;
        for (String match : longestMatches) {
            if (match.length() > maxLength) {
                finalMatches.clear();
                finalMatches.add(match);
                maxLength = match.length();
            } else if (match.length() == maxLength) {
                finalMatches.add(match);
            }
        }

        return finalMatches;
    }

    @Test
    public void testJolLayOut() throws Exception {
        String input = "ass23;section(score(,'1,2,3')";

        List<String> matches = extractValidIdentifiers2(input);
        System.out.println("Valid matches: " + matches);
    }

    private static final Pattern columnExtractPattern = Pattern.compile("(?<!')\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b(?!\\s*\\()");

    public static List<String> extractValidIdentifiers2(String input) {
        List<String> validMatches = new ArrayList<>();
        Matcher matcher = columnExtractPattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group(1);
            if (isValidMatch(match)) {
                validMatches.add(match);
            }
        }

        return validMatches;
    }

    private static boolean isValidMatch(String match) {
        return match.matches(".*[a-zA-Z].*");
    }
}
