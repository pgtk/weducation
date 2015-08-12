package ru.edu.pgtk.weducation.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Воронин Леонид
 */
@javax.ws.rs.ApplicationPath("reports")
public class ApplicationConfig extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new java.util.HashSet<>();
    addRestResourceClasses(resources);
    return resources;
  }

  /**
   * Do not modify addRestResourceClasses() method.
   * It is automatically populated with
   * all resources defined in the project.
   * If required, comment out calling this method in getClasses().
   */
  private void addRestResourceClasses(Set<Class<?>> resources) {
    resources.add(ru.edu.pgtk.weducation.reports.CardReportsEJB.class);
    resources.add(ru.edu.pgtk.weducation.reports.GroupReportsEJB.class);
  }
  
}
