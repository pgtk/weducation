package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.ExamForm;

@Stateless
@Named("examFormsEJB")
public class ExamFormsEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  
  public ExamForm get(final int id) {
    ExamForm result = em.find(ExamForm.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("ExamForm not found with id " + id);
  }
  
  public List<ExamForm> fetchAll() {
    TypedQuery<ExamForm> q = em.createQuery(
            "SELECT ef FROM ExamForm ef ORDER BY ef.name", ExamForm.class);
    return q.getResultList();
  }
  
  public ExamForm save(ExamForm item) {
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final ExamForm item) {
    ExamForm ef = em.find(ExamForm.class, item.getId());
    if (null != ef) {
      em.remove(ef);
    }
  }
}
