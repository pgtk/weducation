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
import ru.edu.pgtk.weducation.entity.SemesterMark;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.Subject;

/**
 *
 * @author user
 */
public class SemesterMarksEJBTest {

  private static EJBContainer container;
  private static SemesterMarksEJB ejb;
  private static StudyGroupsEJB groups;
  private static SubjectsEJB subjects;
  private static StudyModulesEJB modules;

  @BeforeClass
  public static void setUpClass() {
    try {
      Map<String, Object> properties = new HashMap<>();
      properties.put(EJBContainer.MODULES, new File("target/classes"));
      properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "glassfish");
      properties.put(EJBContainer.APP_NAME, "weducation");
      container = EJBContainer.createEJBContainer(properties);
      ejb = (SemesterMarksEJB) container.getContext().lookup("java:global/weducation/classes/SemesterMarksEJB");
      groups = (StudyGroupsEJB) container.getContext().lookup("java:global/weducation/classes/StudyGroupsEJB");
      subjects = (SubjectsEJB) container.getContext().lookup("java:global/weducation/classes/SubjectsEJB");
      modules = (StudyModulesEJB) container.getContext().lookup("java:global/weducation/classes/StudyModulesEJB");
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
   * Проверим работоспособность сразу всех операций EJB-компонента для дисциплин.
   */
  @Ignore
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
      for (SemesterMark m: marks) {
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
  @Ignore
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
      for (SemesterMark m: marks) {
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
