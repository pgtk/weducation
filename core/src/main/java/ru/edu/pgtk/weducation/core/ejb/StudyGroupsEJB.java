package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Named("studyGroupsEJB")
public class StudyGroupsEJB extends AbstractEJB implements StudyGroupsDAO {

    @EJB
    private SpecialitiesDAO specialities;
    @EJB
    private StudyPlansDAO plans;

    @Override
    public StudyGroup get(final int id) {
        StudyGroup result = em.find(StudyGroup.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("StudyGroup not found with id " + id);
    }

    @Override
    public List<StudyGroup> fetchAll() {
        TypedQuery<StudyGroup> q = em.createQuery(
                "SELECT sg FROM StudyGroup sg ORDER BY sg.course, sg.name", StudyGroup.class);
        return q.getResultList();
    }

    @Override
    public List<StudyGroup> fetchActual() {
        TypedQuery<StudyGroup> q = em.createQuery(
                "SELECT sg FROM StudyGroup sg WHERE (sg.active = true) ORDER BY sg.course, sg.name", StudyGroup.class);
        return q.getResultList();
    }

    @Override
    public List<StudyGroup> findByDepartment(final Department department) {
        TypedQuery<StudyGroup> q = em.createQuery(
                "SELECT sg FROM StudyGroup sg, DepartmentProfile dp "
                        + "WHERE (sg.speciality = dp.speciality) AND "
                        + "(sg.extramural = dp.extramural) AND (dp.department = :dep) "
                        + "ORDER BY sg.course, sg.name", StudyGroup.class);
        q.setParameter("dep", department);
        return q.getResultList();
    }

    public List<StudyGroup> findBySpeciality(final Speciality speciality) {
        TypedQuery<StudyGroup> q = em.createQuery(
                "SELECT sg FROM StudyGroup sg WHERE (sg.speciality = :spc) "
                        + "ORDER BY sg.course, sg.name", StudyGroup.class);
        q.setParameter("spc", speciality);
        return q.getResultList();
    }

    public List<StudyGroup> findBySpeciality(final Speciality speciality, final boolean extramural) {
        TypedQuery<StudyGroup> q = em.createQuery(
                "SELECT sg FROM StudyGroup sg WHERE (sg.speciality = :spc) AND (sg.extramural = :em)"
                        + "ORDER BY sg.course, sg.name", StudyGroup.class);
        q.setParameter("spc", speciality);
        q.setParameter("em", extramural);
        return q.getResultList();
    }

    @Override
    public StudyGroup findByName(final String name) {
        try {
            TypedQuery<StudyGroup> q = em.createQuery(
                    "SELECT sg FROM StudyGroup sg WHERE (sg.name = :gn)", StudyGroup.class);
            q.setParameter("gn", name);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public StudyGroup save(StudyGroup item) {
        try {
            // Получим специальность и учебный план
            if (item.getSpecialityCode() > 0) {
                item.setSpeciality(specialities.get(item.getSpecialityCode()));
            }
            if (item.getPlanCode() > 0) {
                item.setPlan(plans.get(item.getPlanCode()));
            }
            // Сохраним изменения
            if (item.getId() == 0) {
                em.persist(item);
            } else {
                item = em.merge(item);
            }
            // Обновим специальность, план и форму обучения у всех студентов группы
            Query q = em.createQuery("UPDATE StudyCard sc SET sc.speciality = sc.group.speciality, "
                    + "sc.plan = sc.group.plan, sc.extramural = sc.group.extramural WHERE (sc.group = :grp)");
            q.setParameter("grp", item);
            q.executeUpdate();
            // Вернем обновленную группу
            return item;
        } catch (Exception e) {
            throw new EJBException("Exception while group update! Class " + e.getClass().getName()
                    + " with message " + e.getMessage());
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(final StudyGroup item) {
        StudyGroup sg = em.find(StudyGroup.class, item.getId());
        if (null != sg) {
            em.remove(sg);
        }
    }
}
