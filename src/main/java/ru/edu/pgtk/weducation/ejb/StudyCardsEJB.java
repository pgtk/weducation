package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.School;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@Stateless
@Named("studyCardsEJB")
public class StudyCardsEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  
  public StudyCard get(final int id) {
    StudyCard result = em.find(StudyCard.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Card not found with id " + id);
  }
  
  public List<StudyCard> fetchAll() {
    TypedQuery<StudyCard> q = em.createQuery("SELECT c FROM StudyCard c", StudyCard.class);
    return q.getResultList();
  }
  
  public List<StudyCard> findByPerson(final Person person) {
    TypedQuery<StudyCard> q = em.createQuery(
            "SELECT c FROM StudyCard c WHERE c.person = :psn", StudyCard.class);
    q.setParameter("psn", person);
    return q.getResultList();
  }
  
  public List<StudyCard> findByGroup(final StudyGroup group) {
    TypedQuery<StudyCard> q = em.createQuery(
            "SELECT c FROM StudyCard c WHERE c.group = :grp", StudyCard.class);
    q.setParameter("grp", group);
    return q.getResultList();
  }
  
  public StudyCard save(StudyCard item) {
    if (item.getSchoolCode() > 0) {
      School school = em.find(School.class, item.getSchoolCode());
      if (null != school) {
        item.setSchool(school);
      }
    }
    if (item.getPersonCode() > 0) {
      Person person = em.find(Person.class, item.getPersonCode());
      if (null != person) {
        item.setPerson(person);
      }
    }
    if (item.getGroupCode() > 0) {
      StudyGroup grp = em.find(StudyGroup.class, item.getGroupCode());
      if (null != grp) {
        item.setGroup(grp);
      }
    } else {
      // А вдруг группу убрали?
      item.setGroup(null);
    }
    if (item.getSpecialityCode() > 0) {
      Speciality spc = em.find(Speciality.class, item.getSpecialityCode());
      if (null != spc) {
        item.setSpeciality(spc);
      }
    }
    if (item.getPlanCode() > 0) {
      StudyPlan pln = em.find(StudyPlan.class, item.getPlanCode());
      if (null != pln) {
        item.setPlan(pln);
      }
    }
    if((null != item.getGroup()) && (item.getPlan().getId() != item.getGroup().getPlanCode())) {
      throw new EJBException("Указанный явно учебный план отличается от учебного плана группы!");
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final StudyCard item) {
    StudyCard sc = em.find(StudyCard.class, item.getId());
    if (null != sc) {
      em.remove(sc);
    }
  }
}
