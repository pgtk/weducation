package ru.edu.pgtk.weducation.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс профессионального модуля, либо блока дисциплин.
 *
 * @author Воронин Леонид
 */
public class XMLModule {

  private String index = "";      // индекс (ЕН, ОП)
  private String name = "";       // Наименование модуля
  private int type = 1;           // Тип модуля (2 для ПМ и 1 для просто группы дисциплин)
  private int kvExams = 0;        // Количество квалификационных экзаменов
  private List<XMLSubject> subjects = new ArrayList<>(); // список дисциплин
  private List<XMLPractice> practices = new ArrayList<>(); // список практик
  // TODO добавить информацию о квалификационных экзаменах

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

  public List<XMLSubject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<XMLSubject> subjects) {
    this.subjects = subjects;
  }

  public List<XMLPractice> getPractices() {
    return practices;
  }

  public void setPractices(List<XMLPractice> practices) {
    this.practices = practices;
  }

  public String getFullName() {
    return index + " " + name;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getKvExams() {
    return kvExams;
  }

  public void setKvExams(int kvExams) {
    this.kvExams = kvExams;
  }
  
  public XMLModule() {
    super();
  }

  public XMLModule(int type, String index, String name) {
    super();
    this.type = type;
    this.index = index;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Module [type=" + type + ", index=" + index + ", name=" + name
            + ", subjects=" + subjects + ", practices=" + practices 
            + ", kvExams=" + kvExams + "]";
  }

  public int getSubjectsCount() {
    return subjects.size();
  }
  
  public int getPracticesCount() {
    return practices.size();
  }

  public void addSubject(XMLSubject subject) {
    subject.setModule(this);
    subjects.add(subject);
  }
  
  public void addPractice(XMLPractice practice) {
    practice.setModule(this);
    practices.add(practice);
  }
}
