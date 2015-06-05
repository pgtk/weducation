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
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;

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
      "SELECT c FROM StudyCard c WHERE c.group = :grp ORDER BY c.active DESC, c.person.firstName, c.person.middleName",
      StudyCard.class);
    q.setParameter("grp", group);
    return q.getResultList();
  }

  public StudyCard save(StudyCard item) {
    if (item.getSchoolCode() > 0) {
      item.setSchool(schools.get(item.getSchoolCode()));
    }
    if (item.getPersonCode() > 0) {
      item.setPerson(persons.get(item.getPersonCode()));
    }
    // Если у нас есть группа, то часть полей возьмем оттуда
    if (item.getGroupCode() > 0) {
      StudyGroup grp = groups.get(item.getGroupCode());
      // Устанавливаем группу
      item.setGroup(grp);
      // Устанавливаем учебный план
      item.setPlan(grp.getPlan());
      item.setSpeciality(grp.getSpeciality());
      // Заочник или нет - возьмем тоже из группы
      item.setExtramural(grp.isExtramural());
    } else {
      // А вдруг группу убрали?
      item.setGroup(null);
      // Явно устанавливаем специальность и план
      if (item.getSpecialityCode() > 0) {
        item.setSpeciality(specialities.get(item.getSpecialityCode()));
      }
      if (item.getPlanCode() > 0) {
        item.setPlan(plans.get(item.getPlanCode()));
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
