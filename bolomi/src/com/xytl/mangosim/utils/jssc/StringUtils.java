package com.xytl.mangosim.utils.jssc;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;

public class StringUtils
{
  private static final int PASSWORD_LENGTH = 7;
  private static final String PASSWORD_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
  public static final Random RANDOM = new Random();

  public static String trimWhitespace(String s) {
    if (s == null)
      return null;
    int start = 0;
    while ((start < s.length()) && (Character.isWhitespace(s.charAt(start))))
      start++;
    int end = s.length();
    while ((end > start) && (Character.isWhitespace(s.charAt(end - 1))))
      end--;
    return s.substring(start, end);
  }

  public static String mask(String s, char maskChar, int unmaskedLength)
  {
    if (s == null) {
      return null;
    }
    if (s.length() > unmaskedLength) {
      return org.apache.commons.lang.StringUtils.leftPad("", s.length() - unmaskedLength, '*') + s.substring(s.length() - unmaskedLength);
    }

    return org.apache.commons.lang.StringUtils.leftPad("", s.length(), '*');
  }

  public static String generatePassword()
  {
    return generatePassword(7);
  }

  public static String generatePassword(int length) {
    return generateRandomString(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890");
  }

  public static String generateRandomString(int length, String charSet) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++)
      sb.append(charSet.charAt(RANDOM.nextInt(charSet.length())));
    return sb.toString();
  }

  public static String escapeLT(String s) {
    if (s == null)
      return null;
    return s.replaceAll("<", "&lt;");
  }

  public static boolean globWhiteListMatchIgnoreCase(String[] values, String value) {
    if ((values == null) || (values.length == 0) || (value == null)) {
      return false;
    }
    int ast = 0;
    for (int i = 0; i < values.length; i++) {
      ast = values[i].indexOf("*");
      if (ast == -1) {
        if (values[i].equalsIgnoreCase(value)) {
          return true;
        }
      }
      else if ((value.length() >= ast) && 
        (values[i].substring(0, ast).equalsIgnoreCase(value.substring(0, ast)))) {
        return true;
      }

    }

    return false;
  }

  public static String replaceMacros(String s, Properties properties) {
    Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
    Matcher matcher = p.matcher(s);
    StringBuffer result = new StringBuffer();
    while (matcher.find()) {
      String group = matcher.group(1);
      matcher.appendReplacement(result, Matcher.quoteReplacement(ObjectUtils.toString(properties.get(group))));
    }
    matcher.appendTail(result);
    return result.toString();
  }

  public static String replaceMacros(String s, Map<String, ?> properties)
  {
    Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
    Matcher matcher = p.matcher(s);
    StringBuffer result = new StringBuffer();
    while (matcher.find()) {
      String group = matcher.group(1);
      matcher.appendReplacement(result, Matcher.quoteReplacement(ObjectUtils.toString(properties.get(group))));
    }
    matcher.appendTail(result);
    return result.toString();
  }

  public static String replaceMacro(String s, String name, String replacement)
  {
    return s.replaceAll(Pattern.quote("${" + name + "}"), replacement);
  }

  public static String replaceMacro(String s, String name, String content, String replacement) {
    return s.replaceAll(Pattern.quote("${" + name + ":" + content + "}"), replacement);
  }

  public static String getMacroContent(String s, String name) {
    Matcher matcher = Pattern.compile("\\$\\{" + Pattern.quote(name) + ":(.*?)\\}").matcher(s);
    if (matcher.find())
      return matcher.group(1);
    return null;
  }

  public static String truncate(String s, int length, String truncateSuffix) {
    if (s == null) {
      return s;
    }
    if (s.length() <= length) {
      return s;
    }
    s = s.substring(0, length);
    if (truncateSuffix == null)
      return s;
    return s + truncateSuffix;
  }

  public static String findGroup(Pattern pattern, String s) {
    return findGroup(pattern, s, 1);
  }

  public static String findGroup(Pattern pattern, String s, int group) {
    if (s == null) {
      return null;
    }
    Matcher matcher = pattern.matcher(s);
    if (matcher.find()) {
      return matcher.group(group);
    }
    return null;
  }

  public static String[] findAllGroup(Pattern pattern, String s) {
    return findAllGroup(pattern, s, 1);
  }

  public static String[] findAllGroup(Pattern pattern, String s, int group) {
    if (s == null) {
      return null;
    }
    Matcher matcher = pattern.matcher(s);
    List result = new ArrayList();
    while (matcher.find()) {
      result.add(matcher.group(group));
    }
    String[] a = new String[result.size()];
    result.toArray(a);
    return a;
  }

  public static String durationToString(long duration) {
    if (duration < 1000L) {
      return duration + "ms";
    }
    if (duration < 10000L) {
      String s = "" + duration / 1000L + '.';
      s = s + (int)(duration % 1000L / 10.0D + 0.5D);
      return s + "s";
    }

    if (duration < 60000L) {
      String s = "" + duration / 1000L + '.';
      s = s + (int)(duration % 1000L / 100.0D + 0.5D);
      return s + "s";
    }

    duration /= 1000L;

    if (duration < 600L) {
      return "" + duration / 60L + 'm' + duration % 60L + 's';
    }

    duration /= 60L;

    if (duration < 60L) {
      return "" + duration + 'm';
    }
    if (duration < 1440L) {
      return "" + duration / 60L + 'h' + duration % 60L + 'm';
    }

    duration /= 60L;

    if (duration < 24L) {
      return "" + duration + 'h';
    }
    if (duration < 168L) {
      return "" + duration / 24L + 'd' + duration % 24L + 'h';
    }

    duration /= 24L;

    return "" + duration + 'd';
  }

  public static String capitalize(String s) {
    if (s == null)
      return null;
    return s.toUpperCase().replace(' ', '_');
  }

  public static boolean startsWith(String haystack, String needle) {
    if (haystack == null)
      return false;
    return haystack.startsWith(needle);
  }

  public static int compareStrings(String s1, String s2, boolean ignoreCase) {
    if ((s1 == null) && (s2 == null))
      return 0;
    if (s1 == null)
      return -1;
    if (s2 == null)
      return 1;
    if (ignoreCase)
      return s1.compareToIgnoreCase(s2);
    return s1.compareTo(s2);
  }
}