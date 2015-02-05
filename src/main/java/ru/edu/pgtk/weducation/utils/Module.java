package ru.edu.pgtk.weducation.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс профессионального модуля, либо блока дисциплин.
 *
 * @author Воронин Леонид
 */
public class Module {

  private String index = "";      // индекс (ЕН, ОП)
  private String name = "";       // Наименование модуля
  private List<Subject> subjects = new ArrayList<>(); // список дисциплин
  private List<Practice> practices = new ArrayList<>(); // список практик

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<Subject> subjects) {
    this.subjects = subjects;
  }

  public List<Practice> getPractices() {
    return practices;
  }

  public void setPractices(List<Practice> practices) {
    this.practices = practices;
  }

  public String getFullName() {
    return index + " " + name;
  }
  
  public Module() {
    super();
  }

  public Module(String index, String name) {
    super();
    this.index = index;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Module [index=" + index + ", name=" + name
            + ", subjects=" + subjects + ", practices=" + practices + "]";
  }

  public int getSubjectsCount() {
    return subjects.size();
  }
  
  public int getPracticesCount() {
    return practices.size();
  }

  public void addSubject(Subject subject) {
    subject.setModule(this);
    subjects.add(subject);
  }
  
  public void addPractice(Practice practice) {
    practice.setModule(this);
    practices.add(practice);
  }
}
