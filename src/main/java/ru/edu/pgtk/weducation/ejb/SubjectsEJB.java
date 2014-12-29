package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;

@Stateless
@Named("subjectsEJB")
public class SubjectsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public Subject get(final int id) {
    Subject result = em.find(Subject.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Subject not found with id " + id);
  }

  public List<Subject> fetchAll() {
    TypedQuery<Subject> q = em.createQuery("SELECT s FROM Subject s ORDER BY s.fullName", Subject.class);
    return q.getResultList();
  }

  public List<Subject> findByName(final String name) {
    TypedQuery<Subject> q = em.createQuery(
            "SELECT s FROM Subject s WHERE s.fullName LIKE :name ORDER BY s.fullName", Subject.class);
    q.setParameter("name", name);
    return q.getResultList();
  }

  public Subject save(Subject item) {
    if (item.getModuleCode() > 0) {
      StudyModule m = em.find(StudyModule.class, item.getModuleCode());
      if (null != m) {
        item.setModule(m);
      }
    }
    if (item.getPlanCode() > 0) {
      StudyPlan p = em.find(StudyPlan.class, item.getPlanCode());
      if (null != p) {
        item.setPlan(p);
      }
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(Subject item) {
    Subject s = em.find(Subject.class, item.getId());
    if (null != s) {
      em.remove(s);
    }
  }
}
