package ru.edu.pgtk.weducation.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

  /**
   * УНифицированный метод для изменения окончания в зависимости от числа
   * @param value число
   * @param prefix1 выражение, используемое при value=1 (день)
   * @param prefix2 выражение, используемое при value=2..4 (дня)
   * @param prefix3 выражение, используемое при остальных значениях value
   * @return строка содержащее value и выражение в нужном склонении. Например, 2 дня
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
				return "null";
			}
			final String[] months = { "января", "февраля", "марта", "апреля",
					"мая", "июня", "июля", "августа", "сентября", "октября",
					"ноября", "декабря" };
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
			return -1;
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
		int ilength;
    if (length < 5) {
      ilength = (int) Math.floor(length);
    } else {
      ilength = (int) Math.ceil(length);
    }
		if (ilength > 20) {
			ilength %= 10;
		}
		String prefix = " недель";
		if (ilength == 1) {
			prefix = " неделя";
		} else if (ilength < 5) {
			prefix = " недели";
		}
		return String.format("%2.1f %s", length, prefix);
	}
}
