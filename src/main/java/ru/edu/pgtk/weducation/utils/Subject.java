package ru.edu.pgtk.weducation.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации дисциплин, либо МДК.
 *
 * @author Воронин Леонид
 */
public class Subject {

  private String index = "";    // индекс (ЕН, ОП)
  private String name = "";     // Наименование предмета
  private Module module = null; // родительский элемент
  private List<SubjectLoad> load = new ArrayList<>(); // список нагрузки по семестрам

  public Subject() {
    super();
  }

  @Override
  public String toString() {
    return "Subject [index=" + index + ", name=" + name + ", load=" + load + "]";
  }

  public Subject(Module module, String index, String name) {
    this.index = index;
    this.name = name;
    this.module = module;
  }

  public void addLoad(SubjectLoad subjLoad) {
    load.add(subjLoad);
  }

  public List<SubjectLoad> getLoad() {
    return load;
  }

  public String getFullName() {
    return index + " " + name;
  }

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

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }
}
