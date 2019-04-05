package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Named("studyCardsEJB")
public class StudyCardsEJB extends AbstractEJB implements StudyCardsDAO {

    @EJB
    private StudyGroupsDAO groups;
    @EJB
    private SchoolsDAO schools;
    @EJB
    private SpecialitiesDAO specialities;
    @EJB
    private PersonsDAO persons;
    @EJB
    private StudyPlansDAO plans;

    @Override
    public StudyCard get(final int id) {
        StudyCard result = em.find(StudyCard.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("Card not found with id " + id);
    }

    @Override
    public StudyCard get(Speciality speciality, boolean extramural, String biletNumber) {
        TypedQuery<StudyCard> q = em.createQuery("SELECT c FROM StudyCard c WHERE (c.speciality = :s) AND (c.extramural = :e) AND (c.biletNumber = :b)",
                StudyCard.class);
        q.setParameter("s", speciality);
        q.setParameter("e", extramural);
        q.setParameter("b", biletNumber);
        return q.getSingleResult();
    }

    @Override
    public List<StudyCard> fetchAll() {
        TypedQuery<StudyCard> q = em.createQuery("SELECT c FROM StudyCard c", StudyCard.class);
        return q.getResultList();
    }

    @Override
    public List<StudyCard> findByPerson(final Person person) {
        TypedQuery<StudyCard> q = em.createQuery(
                "SELECT c FROM StudyCard c WHERE c.person = :psn", StudyCard.class);
        q.setParameter("psn", person);
        return q.getResultList();
    }

    @Override
    public List<StudyCard> findByGroup(final StudyGroup group) {
        TypedQuery<StudyCard> q = em.createQuery(
                "SELECT c FROM StudyCard c WHERE c.group = :grp ORDER BY c.active DESC, c.person.firstName, c.person.middleName",
                StudyCard.class);
        q.setParameter("grp", group);
        return q.getResultList();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
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

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(final StudyCard item) {
        StudyCard sc = em.find(StudyCard.class, item.getId());
        if (null != sc) {
            em.remove(sc);
        }
    }
}
