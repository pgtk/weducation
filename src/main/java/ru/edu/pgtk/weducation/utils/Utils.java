package ru.edu.pgtk.weducation.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Различные утилиты, вынесенные в отдельный класс
 *
 * @author Воронин Леонид
 */
public class Utils {

  private Utils() {
    throw new IllegalStateException("This constructor should not be called!");
  }

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

  /**
   * УНифицированный метод для изменения окончания в зависимости от числа
   *
   * @param value число
   * @param prefix1 выражение, используемое при value=1 (день)
   * @param prefix2 выражение, используемое при value=2..4 (дня)
   * @param prefix3 выражение, используемое при остальных значениях value
   * @return строка содержащее value и выражение в нужном склонении. Например, 2
   * дня
   */
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

  /**
   * Получает сокращенное наименование дисциплины из полного.
   *
   * @param name полное наименование дисциплины
   * @return Аббревиатура в виде строки
   */
  public static String getShortName(final String name) {
    if ((null == name) || name.isEmpty()) {
      throw new IllegalArgumentException("Empty strings and null is not allowed!");
    }
    // Копируем строку во временную переменную? заменяя дефис на пробел
    String text = name.replace('-', ' ');
    StringBuilder result = new StringBuilder();
    // разбиваем строку на фрагменты и обрабатываем их
    for (String piece : text.split(" ")) {
      if (!piece.isEmpty()) {
        // Если фрагмент - один символ, то копируем его в вывод. Иначе - берем заглавный первый символ
        result.append((piece.length() > 1) ? piece.substring(0, 1).toUpperCase() : piece);
      }
    }
    return result.toString();
  }

  public static String getMonthString(final int month) {
    return getStringPrefix(month, " месяц", " месяца", " месяцев");
  }

  public static String getYearString(final int year) {
    return getStringPrefix(year, " год", " года", " лет");
  }

  public static String getWeekString(final int week) {
    return getStringPrefix(week, "неделя", "недели", "недель");
  }

  public static String getDateString(Date date) {
    try {
      if (date == null) {
        throw new IllegalArgumentException("Null value is not allowed!");
      }
      final String[] months = {"января", "февраля", "марта", "апреля",
        "мая", "июня", "июля", "августа", "сентября", "октября",
        "ноября", "декабря"};
      Calendar c = new GregorianCalendar();
      c.setTime(date);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);
      int year = c.get(Calendar.YEAR);
      return day + " " + months[month] + " " + year;
    } catch (Exception e) {
      return "exception";
    }
  }

  public static int getYear(Date date) {
    if (date == null) {
      throw new IllegalArgumentException("Null value is not allowed!");
    }
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    return c.get(Calendar.YEAR);
  }

  public static String getMarkString(int mark) {
    String result;
    switch (mark) {
      case 0:
        result = "x";
        break;
      case 1:
        result = "неудовлетворительно";
        break;
      case 2:
        result = "неудовлетворительно";
        break;
      case 3:
        result = "удовлетворительно";
        break;
      case 4:
        result = "хорошо";
        break;
      case 5:
        result = "отлично";
        break;
      case 13:
        result = "зачтено";
        break;
      default:
        result = " ";
    }
    return result;
  }

  public static String getLenString(final float length) {
    if (length < 0) {
      throw new IllegalArgumentException("Negative values is not allowed!");
    }
    if (length > 1000) {
      throw new IllegalArgumentException("Values more than 1000 is not allowed!");
    }
    if (length == 1) {
      return String.format("%2.1f %s", length, "неделя");
    }
    String prefix = "недель";
    if (length > 1) {
      long ilength = (long) Math.ceil(length);
      if (ilength > 20) {
        ilength %= 10;
      }
      if (ilength < 5) {
        prefix = " недели";
      }
    }
    return String.format("%2.1f %s", length, prefix);
  }

  public static void addMessage(final Exception e) {
    FacesContext context = FacesContext.getCurrentInstance();
    String message = "Exception class " + e.getClass().getName() + " with message " + e.getMessage();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  public static void addMessage(final String message) {
    FacesContext context = FacesContext.getCurrentInstance();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

}
