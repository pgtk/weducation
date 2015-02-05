package ru.edu.pgtk.weducation.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс практики. Помимо дисциплин, в модуль может также входить и практика.
 *
 * @author Воронин Леонид
 */
public class Practice {

  private String index = "";             // индекс (ЕН, ОП)
  private String name = "";              // Наименование практики
  private Module module = null;          // родительский элемент (модуль, которому принадлежит практика)
  private List<PracticeLoad> load = new ArrayList<>(); // список нагрузки по семестрам

  /**
   * Конструктор без параметров.
   */
  public Practice() {
    super();
  }

  /**
   * Конструктор с параметрами.
   *
   * @param module Модуль для которого создается практика
   * @param index индекс практики (Всякие там ПР.02 и т.п.)
   * @param name наименование практики
   */
  public Practice(Module module, String index, String name) {
    super();
    this.module = module;
    this.index = index;
    this.name = name;
  }
  
  /**
   * Возвращает полное наименование практики.
   * @return полное наименование в виде строки.
   */
  public String getFullName() {
    return index + " " + name;
  }
  
  @Override
  public String toString() {
    return "Practice [index=" + index + ", name=" + name + ", load=" + load +"]";
  }

  public void addLoad(PracticeLoad item) {
    load.add(item);
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

  public List<PracticeLoad> getLoad() {
    return load;
  }

  public void setLoad(List<PracticeLoad> load) {
    this.load = load;
  }
}
