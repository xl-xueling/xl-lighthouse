package com.dtstep.lighthouse.core.formula;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TemplateUtil {

    public static String[] split(String str) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        boolean inBrackets = false;
        boolean inParentheses = false;
        StringBuilder builder = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == ';' && !inQuotes && !inBrackets && !inParentheses) {
                String temp = builder.toString().trim();
                if(!StringUtil.isEmpty(temp)){
                    result.add(temp);
                }
                builder.setLength(0);
            } else {
                builder.append(c);
                if (c == '\'') {
                    inQuotes = !inQuotes;
                } else if (c == '[') {
                    inBrackets = true;
                } else if (c == ']') {
                    inBrackets = false;
                } else if (c == '(') {
                    inParentheses = true;
                } else if (c == ')') {
                    inParentheses = false;
                }
            }
        }
        String temp = builder.toString().trim();
        if(!StringUtil.isEmpty(temp)){
            result.add(temp);
        }
        String[] resultArray = new String[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }

}
