package ru.edu.pgtk.weducation.core.ejb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.WeekMissing;
import ru.edu.pgtk.weducation.core.helpers.ContainerProvider;

import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class WeekMissingsEJBTest {

  private static ContainerProvider provider;
  private static WeekMissingsDAO ejb;
  private static StudyGroupsDAO groups;

  @BeforeClass
  public static void setUpClass() {
    provider = new ContainerProvider();
    ejb = (WeekMissingsDAO) provider.getBean("WeekMissingsEJB");
    groups = (StudyGroupsDAO) provider.getBean("StudyGroupsEJB");
  }

  @AfterClass
  public static void tearDownClass() {
    try {
      provider.close();
    } catch (Exception e) {
      fail(e.getMessage());
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
      int year = 2011;
      int month = 9;
      int week = 1;
      // Выберем пропуски
      List<WeekMissing> marks = ejb.fetchAll(grp, year, month, week);
      /*
       Пропусков должно быть столько, сколько в группе студентов, 
       то есть по-любому больше нуля.
       */
      assertNotNull(marks);
      assertFalse(marks.isEmpty());
      for (WeekMissing m : marks) {
        // Ставим по 10 часов уважительно и по 8 неуважительно
        m.setLegal(10);
        m.setIllegal(8);
        // Сохраняем
        m = ejb.save(m);
        WeekMissing newWeekMissing = ejb.get(m.getCard(), year, month, week);
        assertNotNull(newWeekMissing);
        assertEquals(m.getLegal(), newWeekMissing.getLegal());
        assertEquals(m.getIllegal(), newWeekMissing.getIllegal());
        // После сравнения удаляем пропуск
        ejb.delete(newWeekMissing);
      }
    } catch (Exception e) {
      fail("Неожиданное исключение класса " + e.getClass().getName() + " с сообщением " + e.getMessage());
    }
  }
}
