package ru.edu.pgtk.weducation.reports;

/**
 * Перечисление для указания типа аттестации в сводной ведомости.
 *
 * Этот класс не планируется для использования в СУБД, поэтому можно добавлять
 * новые значения перечисления в любое удобное для вас место.
 *
 * @author Воронин Леонид
 */
enum ReportExamType {

  ZACHET {
      @Override
      public String getDescription() {
        return "Зачет";
      }

      @Override
      public String getLabel() {
        return "Зч";
      }
    },
  EXAM {
      @Override
      public String getDescription() {
        return "Экзамен";
      }

      @Override
      public String getLabel() {
        return "Эк";
      }
    },
  COURSEWORK {
      @Override
      public String getDescription() {
        return "Курсовая работа (проект)";
      }

      @Override
      public String getLabel() {
        return "КР";
      }
    },
  PRACTIC {
      @Override
      public String getDescription() {
        return "Практика";
      }

      @Override
      public String getLabel() {
        return "Пр";
      }
    };

  /**
   * Возвращает описание данного вида аттестации.
   *
   * @return Короткое описание в виде строки.
   */
  public abstract String getDescription();

  /**
   * Возвращает метку для ведомости.
   *
   * Метка отличается от описания тем, что содержит всего один или два символа.
   * Планируется использовать метки
   *
   * @return Метка в виде строки из одного-двух символов.
   */
  public abstract String getLabel();
}
