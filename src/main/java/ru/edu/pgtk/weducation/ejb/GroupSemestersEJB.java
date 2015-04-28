package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.StudyGroup;

@Stateless
@Named("groupSemestersEJB")
public class GroupSemestersEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  
  public GroupSemester get(final int id) {
    GroupSemester result = em.find(GroupSemester.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("GroupSemester not fount with id " + id);
  }
  
  public List<GroupSemester> fetchAll(final StudyGroup group) {
    TypedQuery<GroupSemester> q = em.createQuery(
            "SELECT gs FROM GroupSemester gs WHERE (gs.group = :grp)", GroupSemester.class);
    q.setParameter("grp", group);
    return q.getResultList();
  }
  
  public GroupSemester save(GroupSemester item) {
    // Проверим на корректность данные
    if (item.getEndDate() <= item.getBeginDate()) {
      throw new EJBException("End date must be greater than begin date!");
    }
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final GroupSemester item) {
    GroupSemester gs = em.find(GroupSemester.class, item.getId());
    if (null != gs) {
      em.remove(gs);
    }
  }
}
