package ru.edu.pgtk.weducation.core.helpers;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс-обертка для создания и закрытия EJB-контейнера.
 *
 * Поскольку для всех тестов используется один и тот же контейнер, нет смысла
 * повторять один и тот же код в каждом тестируемом классе. Вместо этого будет
 * использоваться данный класс.
 *
 * @author Воронин Леонид
 */
public class ContainerProvider implements AutoCloseable {

  private final EJBContainer container;

  public ContainerProvider() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(EJBContainer.MODULES, new File("target/classes"));
    properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "glassfish");
    properties.put(EJBContainer.APP_NAME, "weducation");
    container = EJBContainer.createEJBContainer(properties);
  }

  public Object getBean(final String beanName) {
    if ((beanName == null) || (beanName.isEmpty())) {
      throw new IllegalArgumentException("Параметр beanName не должен быть null или пустой строкой!");
    }
    try {
      return container.getContext().lookup("java:global/weducation/classes/" + beanName);
    } catch (NamingException e) {
      throw new IllegalArgumentException("NamingException при поиске бина: " + e.getMessage());
    }
  }

  @Override
  public void close() throws Exception {
    if (null != container) {
      container.close();
    }
  }
}
