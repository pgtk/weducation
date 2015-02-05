package ru.edu.pgtk.weducation.utils;

/**
 * Различные утилиты, вынесенные в отдельный класс
 *
 * @author Воронин Леонид
 */
public class Utils {

  /**
   * Преобразует значение из строкового в целое. Если преобразование невозможно,
   * то присваивается значение по умолчанию.
   *
   * @param stringValue строка, которую надо преобразовать в число
   * @param defaultValue значение по умолчанию
   * @return целое число
   */
  public static int toInt(final String stringValue, final int defaultValue) {
    int result;
    try {
      if (stringValue == null) {
        return defaultValue;
      }
      result = Integer.parseInt(stringValue);
    } catch (NumberFormatException e) {
      result = defaultValue;
    }
    return result;
  }

  private static String getStringPrefix(final int value, final String prefix1, 
          final String prefix2, final String prefix3) {
    int val = value;
    if (val > 20) {
      val /= 10;
    }
    if (val == 1) {
      return val + prefix1;
    }
    if ((val > 1) && (val < 5)) {
      return val + prefix2;
    }
    return val + prefix3;
  }

  public static String getMonthString(final int month) {
    return getStringPrefix(month, " месяц", " месяца", " месяцев");
  }
  
  public static String getYearString(final int year) {
    return getStringPrefix(year, " год", " года", " лет");
  }
}
