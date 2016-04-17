package ru.edu.pgtk.weducation.reports;

import java.util.Objects;

/**
 * Класс для хранения сведений об аттестации.
 *
 * Планируется использовать этот класс при формировании сводной ведомости.
 * Отличие сводной ведомости от остальных заключается в том, что в сводной
 * ведомости фигурируют оценки по разным типам аттестации: экзамены и зачеты,
 * курсовые проекты, практика.
 *
 * Данный класс позволяет абстрагироваться от деталей аттестации.
 *
 * @author Воронин Леонид
 */
final class ReportExam implements Comparable<ReportExam> {

  private final ReportExamType type; // тип аттестации
  private final String name;         // наименование дисциплины, практики...
  private int id;                    // идентификатор дисциплины, практики...

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.type);
    hash = 17 * hash + Objects.hashCode(this.name);
    hash = 17 * hash + id;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ReportExam other = (ReportExam) obj;
    if (this.type != other.type) {
      return false;
    }
    if (id != other.id) {
      return false;
    }
    return name.contentEquals(other.name);
  }

  @Override
  public int compareTo(ReportExam other) {
    if (type == other.type) {
      return name.compareTo(other.name);
    } else {
      return type.ordinal() - other.type.ordinal();
    }
  }

  public ReportExam(int id, final ReportExamType type, final String name) {
    if (null == name) {
      throw new IllegalArgumentException("Значение name не должно быть NULL!");
    }
    this.id = id;
    this.type = type;
    this.name = name;
  }

  public ReportExamType getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }
}
