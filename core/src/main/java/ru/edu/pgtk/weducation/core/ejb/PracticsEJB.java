package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Practic;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("practicsEJB")
public class PracticsEJB extends AbstractEJB implements PracticsDAO {

    @EJB
    private StudyPlansDAO plans;
    @EJB
    private StudyModulesDAO modules;

    @Override
    public Practic get(final int id) {
        Practic result = em.find(Practic.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("Practic not found with id " + id);
    }

    @Override
    public List<Practic> findByPlan(final StudyPlan plan) {
        TypedQuery<Practic> q = em.createQuery(
                "SELECT p FROM Practic p WHERE (p.plan = :pln) ORDER BY p.fullName", Practic.class);
        q.setParameter("pln", plan);
        return q.getResultList();
    }

    @Override
    public List<Practic> fetch(final StudyModule module) {
        TypedQuery<Practic> q = em.createQuery("SELECT p FROM Practic p WHERE (p.module = :mod) ORDER BY p.fullName", Practic.class);
        q.setParameter("mod", module);
        return q.getResultList();
    }

    /**
     * Получает из СУБД список практик для определенной группы за указанный период
     * обучения.
     *
     * @param group    группа
     * @param course   курс
     * @param semester семестр
     * @return Список практик для указанной группы за указанный период обучения
     */
    @Override
    public List<Practic> fetch(final StudyGroup group, final int course, final int semester) {
        TypedQuery<Practic> q = em.createQuery(
                "SELECT p FROM Practic p WHERE (p.plan = :pln) AND (p.course = :c) AND (p.semester = :s)", Practic.class);
        q.setParameter("pln", group.getPlan());
        q.setParameter("c", course);
        q.setParameter("s", semester);
        return q.getResultList();
    }

    @Override
    public Practic save(Practic item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL Practic!");
        }
        item.setPlan(plans.get(item.getPlanCode()));
        if (item.getModuleCode() > 0) {
            item.setModule(modules.get(item.getModuleCode()));
        } else {
            item.setModule(null);
        }
        if (item.getId() == 0) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final Practic item) {
        Practic p = em.find(Practic.class, item.getId());
        if (null != p) {
            em.remove(p);
        }
    }
}
