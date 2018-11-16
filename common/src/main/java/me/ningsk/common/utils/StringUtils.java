package me.ningsk.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
    public static boolean isBlank(String str)
    {
        return (str == null) || (str.trim().length() == 0);
    }

    public static boolean isEmpty(String str)
    {
        return ((str == null) || (str.length() == 0)) && (isBlank(str));
    }

    public static int length(CharSequence str)
    {
        return str == null ? 0 : str.length();
    }

    public static String nullStrToEmpty(Object str)
    {
        return (str instanceof String) ? (String)str : str == null ? "" : str.toString();
    }

    public static String capitalizeFirstLetter(String str)
    {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);

        return str.length() +
                Character.toUpperCase(c) +
                str.substring(1);
    }

    public static String utf8Encode(String str)
    {
        if ((!isEmpty(str)) && (str.getBytes().length != str.length())) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    public static String utf8Encode(String str, String defultReturn)
    {
        if ((!isEmpty(str)) && (str.getBytes().length != str.length())) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    public static String getHrefInnerHtml(String href)
    {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    public static String htmlEscapeCharsToString(String source)
    {
        return isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .replaceAll("&quot;", "\"");
    }

    public static String fullWidthToHalfWidth(String s)
    {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == '　') {
                source[i] = ' ';
            }
            else if ((source[i] >= 65281) && (source[i] <= 65374))
                source[i] = ((char)(source[i] - 65248));
            else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    public static String halfWidthToFullWidth(String s)
    {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = '　';
            }
            else if ((source[i] >= '!') && (source[i] <= '~'))
                source[i] = ((char)(source[i] + 65248));
            else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    public static String sqliteEscape(String keyWord)
    {
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static String subString(String src)
    {
        int last_slash = src.lastIndexOf(47);
        if (last_slash >= 0) {
            src = src.substring(last_slash + 1);
        }
        return src;
    }
}
