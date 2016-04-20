package ru.edu.pgtk.weducation.ejb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.edu.pgtk.weducation.data.entity.MonthMark;
import ru.edu.pgtk.weducation.data.entity.StudyGroup;
import ru.edu.pgtk.weducation.data.entity.Subject;
import ru.edu.pgtk.weducation.helpers.ContainerProvider;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Тестовый класс для проверки EJB компонента для месячных оценок
 */
@Ignore
public class MonthMarksEJBTest {

  private static ContainerProvider provider;
  private static MonthMarksEJB ejb;
  private static StudyGroupsDAO groups;
  private static SubjectsDAO subjects;

  @BeforeClass
  public static void setUpClass() {
    provider = new ContainerProvider();
    ejb = (MonthMarksEJB) provider.getBean("MonthMarksEJB");
    groups = (StudyGroupsDAO) provider.getBean("StudyGroupsEJB");
    subjects = (SubjectsDAO) provider.getBean("SubjectsEJB");
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
      for (MonthMark m : marks) {
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
