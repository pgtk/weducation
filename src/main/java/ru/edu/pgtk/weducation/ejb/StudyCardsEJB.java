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
    if (item.getInSchoolCode() > 0) {
      School inSchool = em.find(School.class, item.getInSchoolCode());
      if (null != inSchool) {
        item.setInSchool(inSchool);
      }
    }
    if (item.getOutSchoolCode() > 0) {
      School outSchool = em.find(School.class, item.getOutSchoolCode());
      if (null != outSchool) {
        item.setOutSchool(outSchool);
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
    }
    if (item.getSpecialityCode() > 0) {
      Speciality spc = em.find(Speciality.class, item.getSpecialityCode());
      if (null != spc) {
        item.setSpeciality(spc);
      }
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

}
