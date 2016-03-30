package ru.edu.pgtk.weducation.ejb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.edu.pgtk.weducation.entity.Missing;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.utils.ContainerProvider;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@Ignore
public class MissingsEJBTest {

  private static ContainerProvider provider;
  private static MissingsEJB ejb;
  private static StudyGroupsEJB groups;

  @BeforeClass
  public static void setUpClass() {
    provider = new ContainerProvider();
    ejb = (MissingsEJB) provider.getBean("MissingsEJB");
    groups = (StudyGroupsEJB) provider.getBean("StudyGroupsEJB");
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
      List<Missing> marks = ejb.fetchAll(grp, year, month, week);
      /*
       Пропусков должно быть столько, сколько в группе студентов, 
       то есть по-любому больше нуля.
       */
      assertNotNull(marks);
      assertFalse(marks.isEmpty());
      for (Missing m : marks) {
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
