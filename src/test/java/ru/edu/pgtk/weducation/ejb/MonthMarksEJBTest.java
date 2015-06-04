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
import ru.edu.pgtk.weducation.entity.MonthMark;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Subject;

/**
 * Тестовый класс для проверки EJB компонента для месячных оценок
 */
public class MonthMarksEJBTest {

  private static EJBContainer container;
  private static MonthMarksEJB ejb;
  private static StudyGroupsEJB groups;
  private static SubjectsEJB subjects;

  @BeforeClass
  public static void setUpClass() {
    try {
      Map<String, Object> properties = new HashMap<>();
      properties.put(EJBContainer.MODULES, new File("target/classes"));
      properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "glassfish");
      properties.put(EJBContainer.APP_NAME, "weducation");
      container = EJBContainer.createEJBContainer(properties);
      ejb = (MonthMarksEJB) container.getContext().lookup("java:global/weducation/classes/MonthMarksEJB");
      groups = (StudyGroupsEJB) container.getContext().lookup("java:global/weducation/classes/StudyGroupsEJB");
      subjects = (SubjectsEJB) container.getContext().lookup("java:global/weducation/classes/SubjectsEJB");
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
  @Test
  public void testOperations() {
    try {
      System.out.println("testOperations()");
      StudyGroup grp = groups.get(23); // Выбираем группу ПКС-11
      Subject sub = subjects.get(1122); // Вроде как это информатика
      int year = 2011;
      int month = 9;
      // Выберем оценки
      List<MonthMark> marks = ejb.fetchAll(grp, sub, year, month);
      /*
      Оценок должно быть столько, сколько в группе студентов, 
      то есть по-любому больше нуля.
      */
      assertNotNull(marks);
      assertFalse(marks.isEmpty());
      for (MonthMark m: marks) {
        // Ставим всем тройки
        m.setMark(3);
        // Сохраняем
        m = ejb.save(m);
        MonthMark newMark = ejb.get(m.getCard(), sub, year, month);
        assertNotNull(newMark);
        assertEquals(m.getMark(), newMark.getMark());
        // После сравнения удаляем оценку
        ejb.delete(newMark);
      }      
    } catch (Exception e) {
      fail("Неожиданное исключение класса " + e.getClass().getName() + " с сообщением " + e.getMessage());
    }
  }
}
