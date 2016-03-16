package ru.edu.pgtk.weducation.reports.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.reports.entity.ReportMissing;
import ru.edu.pgtk.weducation.utils.ContainerProvider;

import static org.junit.Assert.*;

/**
 *
 * @author leonid
 */
public class MissingsDAOTest {

  private static ContainerProvider provider;
  private static MissingsDAO ejb;
  private static StudyCardsEJB cards;
  private static StudyGroupsEJB groups;
  private static GroupSemestersEJB semesters;

  @BeforeClass
  public static void setUp() {
    provider = new ContainerProvider();
    ejb = (MissingsDAO) provider.getBean("MissingsDAO");
    cards = (StudyCardsEJB) provider.getBean("StudyCardsEJB");
    groups = (StudyGroupsEJB) provider.getBean("StudyGroupsEJB");
    semesters = (GroupSemestersEJB) provider.getBean("GroupSemestersEJB");
  }

  @AfterClass
  public static void tearDown() {
    try {
    provider.close();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * Test of getMonthMissings method, of class MissingsDAO.
   */
  @Test
  public void testGetMonthMissings() {
    System.out.println("getMonthMissings");
    int year = 2014;
    int month = 9;
    try {
      StudyGroup grp = groups.get(21);
      int count = 0;
      for (StudyCard sc : cards.findByGroup(grp)) {
        ReportMissing m = ejb.getMonthMissings(sc, year, month);
        assertNotNull(m);
        assertTrue(m.getCardCode() > 0);
        count++;
      }
      assertTrue(count > 0);
    } catch (Exception e) {
      fail("Unexpected exception! " + e.getMessage());
    }
  }

  /**
   * Test of getSemesterMissings method, of class MissingsDAO.
   */
  @Test
  public void testGetSemesterMissings() {
    System.out.println("getSemesterMissings");
    try {
      StudyGroup grp = groups.get(21);
      GroupSemester sem = semesters.get(5);
      int count = 0;
      for (StudyCard sc : cards.findByGroup(grp)) {
        ReportMissing m = ejb.getSemesterMissings(sc, sem);
        assertNotNull(m);
        assertTrue(m.getCardCode() > 0);
        count++;
      }
      assertTrue(count > 0);
    } catch (Exception e) {
      fail("Unexpected exception!");
    }
  }
}
