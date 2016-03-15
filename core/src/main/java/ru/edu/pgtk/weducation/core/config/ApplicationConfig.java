package ru.edu.pgtk.weducation.core.config;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Класс для конфигурации REST служб.
 * @author Воронин Леонид
 */
@javax.ws.rs.ApplicationPath("reports")
public class ApplicationConfig extends Application {

  @Override
  public final Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new java.util.HashSet<>();
    addRestResourceClasses(resources);
    return resources;
  }

/** Метод для добавления классов ресурсов.
 * @param resources ресурсы.
 */
  private void addRestResourceClasses(Set<Class<?>> resources) {
    resources.add(ru.edu.pgtk.weducation.core.reports.CardReportsEJB.class);
    resources.add(ru.edu.pgtk.weducation.core.reports.GroupReportsEJB.class);
  }
}
