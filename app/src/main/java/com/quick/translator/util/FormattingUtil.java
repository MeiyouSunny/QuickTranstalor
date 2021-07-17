package com.quick.translator.util;

/**
 * Created by 夜雨飘零 on 2017/9/15.
 */

public class FormattingUtil {

    /**
     * 判断字符串的最后一个字符是不是指定的标点符号
     *
     * @param wordStr 每一行的字符串
     * @return 返回结果是boolean
     */
    public static boolean getIsPunctuation(String wordStr) {
        String[] punctuation = {".", "?", "。", "!", "？", "！"};
        boolean isPunctuation = false;
        for (String aPunctuation : punctuation) {
            if (wordStr.substring(wordStr.length() - 1).equals(aPunctuation)) {
                isPunctuation = true;
            }
        }
        return isPunctuation;
    }

    public static String openJSON(String json) {
        String[] space = new String[20];
        space[0] = "";
        for (int i = 0; i < space.length - 1; i++) {
            space[i + 1] = space[i] + "  ";
        }

        StringBuilder sb = new StringBuilder();
        int spaceSum = 0;
        char[] jsonChars = json.toCharArray();
        for (char jsonChar1 : jsonChars) {
            String jsonChar = String.valueOf(jsonChar1);

            if (jsonChar.equals("{") || jsonChar.equals("[")) {
                sb.append("\n").append(space[spaceSum]);
                spaceSum++;
            }
            if (jsonChar.equals("}") || jsonChar.equals("]")) {
                spaceSum--;
                sb.append("\n").append(space[spaceSum]);
            }
            sb.append(jsonChar);
            if (jsonChar.equals(",")) {
                sb.append("\n").append(space[spaceSum]);
            }
        }
        return sb.toString();
    }

    public static String getFormattingDate(String date){
        return date.substring(0, 4) + "年" +
                date.substring(4, 6) + "月" +
                date.substring(6, 8) + "日";
    }
}
