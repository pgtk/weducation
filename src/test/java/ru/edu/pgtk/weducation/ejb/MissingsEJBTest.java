package ru.edu.pgtk.weducation.ejb;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Missing;

/**
 *
 * @author user
 */
public class MissingsEJBTest {

  private static EJBContainer container;
  private static MissingsEJB ejb;
  private static StudyGroupsEJB groups;

  @BeforeClass
  public static void setUpClass() {
    try {
      Map<String, Object> properties = new HashMap<>();
      properties.put(EJBContainer.MODULES, new File("target/classes"));
      properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "glassfish");
      properties.put(EJBContainer.APP_NAME, "weducation");
      container = EJBContainer.createEJBContainer(properties);
      ejb = (MissingsEJB) container.getContext().lookup("java:global/weducation/classes/MissingsEJB");
      groups = (StudyGroupsEJB) container.getContext().lookup("java:global/weducation/classes/StudyGroupsEJB");
    } catch (NamingException e) {
      fail("Ошибка при иннициализации сервера " + e.getMessage());
    }
  }

  @AfterClass
  public static void tearDownClass() {
    if (null != container) {
      container.close();
    }
  }

  /**
   * Проверим работоспособность сразу всех операций EJB-компонента.
   */
  @Ignore
  @Test
  public void testOperations() {
    try {
      System.out.println("testOperations()");
      StudyGroup grp = groups.get(23); // Выбираем группу ПКС-11
      int year = 2011;
      int month = 9;
      int week = 1;
      // Выберем пропуски
      List<Missing> marks = ejb.fetchAll(grp, year, month, week);
      /*
      Пропусков должно быть столько, сколько в группе студентов, 
      то есть по-любому больше нуля.
      */
      assertNotNull(marks);
      assertFalse(marks.isEmpty());
      for (Missing m: marks) {
        // Ставим по 10 часов уважительно и по 8 неуважительно
        m.setLegal(10);
        m.setIllegal(8);
        // Сохраняем
        m = ejb.save(m);
        Missing newMissing = ejb.get(m.getCard(), year, month, week);
        assertNotNull(newMissing);
        assertEquals(m.getLegal(), newMissing.getLegal());
        assertEquals(m.getIllegal(), newMissing.getIllegal());
        // После сравнения удаляем пропуск
        ejb.delete(newMissing);
      }      
    } catch (Exception e) {
      fail("Неожиданное исключение класса " + e.getClass().getName() + " с сообщением " + e.getMessage());
    }
  }
}
