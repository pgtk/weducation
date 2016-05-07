package ru.edu.pgtk.weducation.service.ejb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.edu.pgtk.weducation.data.entity.SemesterMark;
import ru.edu.pgtk.weducation.data.entity.StudyGroup;
import ru.edu.pgtk.weducation.data.entity.StudyModule;
import ru.edu.pgtk.weducation.data.entity.Subject;
import ru.edu.pgtk.weducation.service.helpers.ContainerProvider;

import java.util.List;

import static org.junit.Assert.*;

@Ignore
public class SemesterMarksEJBTest {

  private static ContainerProvider provider;
  private static SemesterMarksEJB ejb;
  private static StudyGroupsDAO groups;
  private static SubjectsDAO subjects;
  private static StudyModulesEJB modules;

  @BeforeClass
  public static void setUpClass() {
    provider = new ContainerProvider();
    ejb = (SemesterMarksEJB) provider.getBean("SemesterMarksEJB");
    groups = (StudyGroupsDAO) provider.getBean("StudyGroupsEJB");
    subjects = (SubjectsDAO) provider.getBean("SubjectsEJB");
	  modules = (StudyModulesEJB) provider.getBean("StudyModulesEJB");
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
   * Проверим работоспособность сразу всех операций EJB-компонента для
   * дисциплин.
   */
  @Test
  public void testSubjectSemesterMarks() {
    try {
      System.out.println("testSubjectSemesterMarks()");
      StudyGroup grp = groups.get(23); // Выбираем группу ПКС-11
      Subject sub = subjects.get(1122); // Вроде как это информатика
      int course = 1;
      int semester = 1;
      // Выберем оценки
      List<SemesterMark> marks = ejb.fetchAll(grp, sub, course, semester);
      /*
       Оценок должно быть столько, сколько в группе студентов, 
       то есть по-любому больше нуля.
       */
      assertNotNull(marks);
      assertFalse(marks.isEmpty());
      for (SemesterMark m : marks) {
        // Ставим всем тройки
        m.setMark(3);
        // Сохраняем
        m = ejb.save(m);
        SemesterMark newMark = ejb.get(m.getCard(), sub, course, semester);
        assertNotNull(newMark);
        assertEquals(m.getMark(), newMark.getMark());
        // После сравнения удаляем оценку
        ejb.delete(newMark);
      }
    } catch (Exception e) {
      fail("Неожиданное исключение класса " + e.getClass().getName() + " с сообщением " + e.getMessage());
    }
  }

  /**
   * Проверим работоспособность сразу всех операций EJB-компонента для модулей.
   */
  @Test
  public void testModuleSemesterMarks() {
    try {
      System.out.println("testSubjectSemesterMarks()");
      StudyGroup grp = groups.get(23); // Выбираем группу ПКС-11
      StudyModule mod = modules.get(90); // Администрирование баз данных
      int course = 1;
      int semester = 1;
      // Выберем оценки
      List<SemesterMark> marks = ejb.fetchAll(grp, mod, course, semester);
      /*
       Оценок должно быть столько, сколько в группе студентов, 
       то есть по-любому больше нуля.
       */
      assertNotNull(marks);
      assertFalse(marks.isEmpty());
      for (SemesterMark m : marks) {
        // Ставим всем тройки
        m.setMark(3);
        // Сохраняем
        m = ejb.save(m);
        SemesterMark newMark = ejb.get(m.getCard(), mod, course, semester);
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
