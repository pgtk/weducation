package ru.edu.pgtk.weducation.reports;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Subject;

/**
 *
 * @author user
 */
public class GroupSheetEJBTest {
  
  private static EJBContainer container;
  private static GroupSheetEJB ejb;
  private static StudyGroupsEJB groups;
  private static SubjectsEJB subjects;
  private final static String EXCEPTION = "Неожиданное исключение";

  @BeforeClass
  public static void setUpClass() {
    try {
      Map<String, Object> properties = new HashMap<>();
      properties.put(EJBContainer.MODULES, new File("target/classes"));
      properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "glassfish");
      properties.put(EJBContainer.APP_NAME, "weducation");
      container = EJBContainer.createEJBContainer(properties);
      ejb = (GroupSheetEJB) container.getContext().lookup("java:global/weducation/classes/GroupSheetEJB");
      subjects = (SubjectsEJB) container.getContext().lookup("java:global/weducation/classes/SubjectsEJB");
      groups = (StudyGroupsEJB) container.getContext().lookup("java:global/weducation/classes/StudyGroupsEJB");
    } catch (NamingException e) {
      fail(EXCEPTION + e.getMessage());
    }
  }
  
  @AfterClass
  public static void tearDownClass() {
    if (null != container) {
      container.close();
    }
  }
  
  @Test
  public void testGetExamSheet() {
    try {
    System.out.println("getExamSheet");
    StudyGroup group = groups.get(21);
    Subject subject = subjects.get(1248);
    int course = 2;
    int semester = 3;
    byte[] result = ejb.getExamSheet(group, subject, course, semester);
    assertNotNull(result);
    assertTrue(result.length > 0);
    } catch (Exception e) {
      fail(EXCEPTION + e.getMessage());
    }
  }

  /**
   * Test of getCourseWorkSheet method, of class GroupSheetEJB.
   */
  @Test
  public void testGetCourseWorkSheet() {
    try {
    System.out.println("getCourseWorkSheet");
    StudyGroup group = groups.get(21);
    Subject subject = subjects.get(1248);
    int course = 2;
    int semester = 3;
    byte[] result = ejb.getExamSheet(group, subject, course, semester);
    assertNotNull(result);
    assertTrue(result.length > 0);
    } catch (Exception e) {
      fail(EXCEPTION + e.getMessage());
    }
  }

  /**
   * Test of getMonthMarksSheet method, of class GroupSheetEJB.
   */
  @Test
  public void testGetMonthMarksSheet() {
    try {
    System.out.println("testGetMonthMarkSheet");
    StudyGroup group = groups.get(21);
    int year = 2014;
    int month = 9;
    byte[] result = ejb.getMonthMarksSheet(group, year, month, true);
    assertNotNull(result);
    assertTrue(result.length > 0);
    } catch (Exception e) {
      fail(EXCEPTION + e.getMessage());
    }
  }
}
