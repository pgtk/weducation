package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.interceptors.Restricted;
import ru.edu.pgtk.weducation.interceptors.WithLog;

/**
 * Корпоративный бин для специальностей
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("specialitiesEJB")
public class SpecialitiesEJB {

  @PersistenceContext(unitName = "weducationPU")
  EntityManager em;

  /**
   * Получает один экземпляр специальности по первичному ключу.
   *
   * @param id идентификатор специальности (первичный ключ)
   * @return экземпляр специальности, соответствующий указанному идентификатору
   * @throws EJBException, если записи с данным идентификатором не обнаружено
   */
  public Speciality get(final int id) {
    Speciality item = em.find(Speciality.class, id);
    if (null != item) {
      return item;
    }
    throw new EJBException("Speciality not found with id " + id);
  }

  /**
   * Получает полный список специальностей.
   *
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchAll() {
    TypedQuery<Speciality> query = em.createQuery("SELECT s FROM Speciality s ORDER BY s.key, s.fullName", Speciality.class);
    return query.getResultList();
  }

  /**
   * Получает список специальностей для указанного отделения.
   *
   * @param department отделение, для которого ищутся специальности.
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchAll(final Department department) {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.department = :dep) "
        + "ORDER BY dp.speciality.key, dp.speciality.fullName", Speciality.class);
    query.setParameter("dep", department);
    return query.getResultList();
  }

  /**
   * Получает список специальностей по которым проводится обучение.
   *
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchActual() {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT s FROM Speciality s WHERE (s.actual = true) ORDER BY s.key, s.fullName", Speciality.class);
    return query.getResultList();
  }

  /**
   * Получает список специальностей по которым проводится обучение для указанной
   * формы обучения.
   *
   * @param extramural {@code true} для заочной формы обучения, иначе ложь
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchActual(final boolean extramural) {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT s FROM Speciality s WHERE (s.actual = true) AND "
        + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :em)) > 0)"
        + "ORDER BY s.key, s.fullName", Speciality.class);
    query.setParameter("em", extramural);
    return query.getResultList();
  }

  /**
   * Получает список специальностей по которым проводится обучение на указанном
   * отделении.
   *
   * @param department отделение, для которого ищутся специальности.
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchActual(final Department department) {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.speciality.actual = true) AND (dp.department = :dep) "
        + "ORDER BY dp.speciality.key, dp.speciality.fullName", Speciality.class);
    query.setParameter("dep", department);
    return query.getResultList();
  }

  /**
   * Получает список специальностей по которым проводится набор.
   *
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchAviable() {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT s FROM Speciality s WHERE (s.actual = true) AND (s.aviable = true) ORDER BY s.key, s.fullName", Speciality.class);
    return query.getResultList();
  }

  /**
   * Получает список специальностей по которым проводится набор для указанной
   * формы обучения.
   *
   * @param extramural {@code true} для заочной формы обучения, иначе ложь
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchAviable(final boolean extramural) {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT s FROM Speciality s WHERE (s.actual = true) AND (s.aviable = true) AND "
        + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :em)) > 0)"
        + "ORDER BY s.key, s.fullName", Speciality.class);
    query.setParameter("em", extramural);
    return query.getResultList();
  }

  /**
   * Получает список специальностей по которым проводится набор на указанном
   * отделении.
   *
   * @param department отделение, для которого ищутся специальности.
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchAviable(final Department department) {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.speciality.aviable = true)"
        + " AND (dp.speciality.actual = true) AND (dp.department = :dep) "
        + "ORDER BY dp.speciality.key, dp.speciality.fullName", Speciality.class);
    query.setParameter("dep", department);
    return query.getResultList();
  }

  /**
   * Возвращает список специальностей для подачи заявки поступающему.
   *
   * Возвращаемый список не будет содержать специальности на которые данной
   * персоной уже были поданы заявки.
   *
   * @param person поступающий
   * @param extramural форма обучение {@code true} если заочная, иначе ложь
   * @param year год поступления
   * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
   */
  public List<Speciality> fetchSuggestions(final Person person, final boolean extramural, final int year) {
    TypedQuery<Speciality> query = em.createQuery(
        "SELECT s FROM Speciality s WHERE (s.actual = true) AND (s.aviable = true) AND "
        + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :e1)) > 0)"
        + "AND (s.id NOT IN (SELECT r.speciality.id FROM Request r WHERE (r.extramural = :e2) AND (r.person = :p1) AND (r.year = :y)))"
        + "AND (s.id NOT IN (SELECT sc.speciality.id FROM StudyCard sc WHERE (sc.person = :p2) AND (sc.extramural = :e3)))"
        + "ORDER BY s.fullName", Speciality.class);
    query.setParameter("e1", extramural);
    query.setParameter("e2", extramural);
    query.setParameter("e3", extramural);
    query.setParameter("p1", person);
    query.setParameter("p2", person);
    query.setParameter("y", year);
    return query.getResultList();
  }

  /**
   * Возвращает одну специальность по шифру.
   *
   * Не рекомендуется использовать данный метод, поскольку ключи могут
   * дублироваться
   *
   * @param key шифр специальности (например, 230115)
   * @return один экземпляр специальности, либо null, если ни одной
   * специальности с таким шифром найти не удалось.
   */
  public Speciality findByKey(final String key) {
    try {
      TypedQuery<Speciality> query = em.createQuery(
          "SELECT s FROM Speciality s WHERE (s.key = :k) ", Speciality.class);
      query.setParameter("k", key);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Производит поиск специальности похожей на образец
   *
   * @param sample образец для поиска
   * @return Экземпляр специальности или null, если ничего похожего найти не
   * удалось.
   */
  public Speciality findLike(final Speciality sample) {
    try {
      TypedQuery<Speciality> query = em.createQuery(
          "SELECT s FROM Speciality s WHERE (s.key LIKE :k) AND (s.fullName LIKE :fn)", Speciality.class);
      query.setParameter("k", sample.getKey());
      query.setParameter("fn", sample.getFullName());
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Сохраняет специальность в базу данных.
   *
   * Если идентификатор специальности равен нулю, то выполняется вызов
   * {@code em.persist(speciality)}. Если идентификатор не равен нулю, то
   * выполняется вызов {@code em.merge(speciality)}.
   *
   * @param speciality Специальность, которая будет сохранена
   * @return Специальность после сохранения (с измененным идентификатором при
   * добавлении новой).
   */
  @WithLog
  @Restricted(allowedRoles = {}) // Разрешено только аминистратору (неявно)
  public Speciality save(Speciality speciality) {
    try {
      if (speciality.getId() == 0) {
        em.persist(speciality);
        return speciality;
      } else {
        return em.merge(speciality);
      }
    } catch (Exception e) {
      throw new EJBException(e.getClass().getName() + " : " + e.getMessage());
    }
  }

  /**
   * Удаляет специальность из базы данных.
   *
   * При выполнении удаления производится поиск специальности по первичному
   * ключу и если специальность найдена - выполняется её удаление.
   *
   * @param speciality Специальность, которую надо удалить.
   */
  @WithLog
  @Restricted(allowedRoles = {}) // Неявно разрешено администратору
  public void delete(final Speciality speciality) {
    try {
      Speciality item = em.find(Speciality.class, speciality.getId());
      if (null != item) {
        em.remove(item);
      }
    } catch (Exception e) {
      throw new EJBException(e.getClass().getName() + " : " + e.getMessage());
    }
  }
}
