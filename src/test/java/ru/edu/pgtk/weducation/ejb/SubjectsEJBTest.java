package ru.edu.pgtk.weducation.ejb;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
import ru.edu.pgtk.weducation.entity.Subject;

public class SubjectsEJBTest {
  private static EJBContainer container;
  private static SubjectsEJB instance;
  
  private final static String EXCEPTION = "Unexpected exception with message ";
  
  @BeforeClass
  public static void setUpClass() throws NamingException {
    Map<String, Object> properties = new HashMap<>();
    properties.put(EJBContainer.MODULES, new File("target/classes"));
    properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "glassfish");
    properties.put(EJBContainer.APP_NAME, "weducation");
    container = EJBContainer.createEJBContainer(properties);
    instance = (SubjectsEJB)container.getContext().lookup("java:global/weducation/classes/SubjectsEJB");    
  }
  
  @AfterClass
  public static void tearDownClass() {
    if (container != null) {
      container.close();
    }
  }
  
  @Test(expected = EJBException.class)
  public void testGetWrong() throws Exception {
    System.out.println("get (wrong id)");
    Subject result = instance.get(0);
    assertNull(result);
    fail("This test should throw an exception!");
  }

  @Test
  public void testFetchAll() throws Exception {
    try {
    System.out.println("fetchAll");
    List<Subject> result = instance.fetchAll();
    assertNotNull(result);
    assertTrue(result.size() > 0);
    assertTrue(result.get(0) instanceof Subject);
    } catch (Exception e) {
      fail(EXCEPTION + e.getMessage());
    }
  }
  
  // . . . Тесты, которые ссылаются на другие классы-сущности

}
