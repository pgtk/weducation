package ru.edu.pgtk.weducation.ejb;

import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.StudyGroup;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("groupSemestersEJB")
public class GroupSemestersEJB extends AbstractEJB implements GroupSemestersDAO {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  @Override
  public GroupSemester get(final int id) {
    GroupSemester result = em.find(GroupSemester.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("GroupSemester not fount with id " + id);
  }

  @Override
  public GroupSemester get(final StudyGroup group, final int course, final int semester) {
    try {
      TypedQuery<GroupSemester> q = em.createQuery(
        "SELECT gs FROM GroupSemester gs WHERE (gs.group = :g) AND (gs.course = :c) AND (gs.semester = :s)", GroupSemester.class);
      q.setParameter("g", group);
      q.setParameter("c", course);
      q.setParameter("s", semester);
      return q.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public GroupSemester getByMonth(final StudyGroup group, final int year, final int month) {
    try {
      TypedQuery<GroupSemester> q = em.createQuery(
        "SELECT gs FROM GroupSemester gs WHERE (gs.group = :g) AND "
          + "((gs.beginYear * 1000 + gs.beginMonth * 10 + gs.beginWeek) <= :bd) AND "
          + "((gs.endYear * 1000 + gs.endMonth * 10 + gs.endWeek) >= :ed)", GroupSemester.class);
      q.setParameter("g", group);
      q.setParameter("bd", year * 1000 + month * 10 + 4);
      q.setParameter("ed", year * 1000 + month * 10 + 1);
      return q.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<GroupSemester> fetchAll(final StudyGroup group) {
    TypedQuery<GroupSemester> q = em.createQuery(
      "SELECT gs FROM GroupSemester gs WHERE (gs.group = :grp)", GroupSemester.class);
    q.setParameter("grp", group);
    return q.getResultList();
  }

  @Override
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

  @Override
  public void delete(final GroupSemester item) {
    GroupSemester gs = em.find(GroupSemester.class, item.getId());
    if (null != gs) {
      em.remove(gs);
    }
  }
}
