package ru.edu.pgtk.weducation.reports.dao;

import ru.edu.pgtk.weducation.data.entity.GroupSemester;
import ru.edu.pgtk.weducation.data.entity.StudyCard;
import ru.edu.pgtk.weducation.reports.entity.ReportMissing;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MissingsDAO {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public ReportMissing getMonthMissings(final StudyCard card, int year, int month) {
    try {
      Query query = em.createNativeQuery(
        "SELECT wmv_crdcode AS mis_crdcode, SUM(wmv_legal) AS mis_legal, SUM(wmv_illegal) AS mis_illegal "
        + "FROM missingsView WHERE (wmv_crdcode = ?1) AND (wmv_year = ?2) AND (wmv_month = ?3) GROUP BY wmv_crdcode;", "missingSummary");
      query.setParameter(1, card.getId());
      query.setParameter(2, year);
      query.setParameter(3, month);
      return (ReportMissing) query.getSingleResult();
    } catch (NoResultException e) {
      // Если пропусков нет, то создадим новый объект с нулевыми значениями и вернем его
      return new ReportMissing(card.getId(), 0, 0);
    }
  }

  public ReportMissing getSemesterMissings(final StudyCard card, GroupSemester semester) {
    try {
      Query query = em.createNativeQuery(
        "SELECT wmv_crdcode AS mis_crdcode, SUM(wmv_legal) AS mis_legal, SUM(wmv_illegal) AS mis_illegal "
        + "FROM missingsView WHERE (wmv_crdcode = ?1) AND (wmv_date >= ?2) AND (wmv_date <= ?3) GROUP BY wmv_crdcode;", "missingSummary");
      query.setParameter(1, card.getId());
      query.setParameter(2, semester.getBeginDate());
      query.setParameter(3, semester.getEndDate());
      return (ReportMissing) query.getSingleResult();
    } catch (NoResultException e) {
      // Если пропусков нет, то создадим новый объект с нулевыми значениями и вернем его
      return new ReportMissing(card.getId(), 0, 0);
    }
  }
}
