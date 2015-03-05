package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@Stateless
@Named("studyCardsEJB")
public class StudyCardsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private StudyGroupsEJB groups;
  @EJB
  private SchoolsEJB schools;
  @EJB
  private SpecialitiesEJB specialities;
  @EJB
  private PersonsEJB persons;
  @EJB
  private StudyPlansEJB plans;

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
    item.setSchool(schools.get(item.getSchoolCode()));
    item.setPerson(persons.get(item.getPersonCode()));
    if (item.getGroupCode() > 0) {
      StudyGroup grp = groups.get(item.getGroupCode());
      item.setGroup(grp);
      if ((null != grp.getPlan()) && (null == item.getPlan()) && (0 == item.getPlanCode())) {
        item.setPlan(grp.getPlan());
      }
    } else {
      // А вдруг группу убрали?
      item.setGroup(null);
    }
    item.setSpeciality(specialities.get(item.getSpecialityCode()));
    if (item.getPlanCode() > 0) {
      StudyPlan pln = em.find(StudyPlan.class, item.getPlanCode());
      if (null != pln) {
        item.setPlan(pln);
      }
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
