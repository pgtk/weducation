package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.interceptors.Restricted;
import ru.edu.pgtk.weducation.core.interceptors.WithLog;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Корпоративный бин для специальностей
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("specialitiesEJB")
public class SpecialitiesEJB extends AbstractEJB implements SpecialitiesDAO {

    /**
     * Получает один экземпляр специальности по первичному ключу.
     *
     * @param id идентификатор специальности (первичный ключ)
     * @return экземпляр специальности, соответствующий указанному идентификатору
     * @throws EJBException, если записи с данным идентификатором не обнаружено
     */
    @Override
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
    @Override
    public List<Speciality> fetchAll() {
        TypedQuery<Speciality> query = em.createQuery("SELECT s FROM Speciality s ORDER BY s.actual DESC, s.name", Speciality.class);
        return query.getResultList();
    }

    /**
     * Получает список специальностей для указанного отделения.
     *
     * @param department отделение, для которого ищутся специальности.
     * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
     */
    @Override
    public List<Speciality> fetchAll(final Department department) {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.department = :dep) "
                        + "ORDER BY dp.speciality.actual DESC, dp.speciality.name", Speciality.class);
        query.setParameter("dep", department);
        return query.getResultList();
    }

    /**
     * Получает список специальностей по которым проводится обучение.
     *
     * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
     */
    @Override
    public List<Speciality> fetchActual() {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT s FROM Speciality s WHERE (s.actual = true) ORDER BY s.actual DESC, s.name", Speciality.class);
        return query.getResultList();
    }

    /**
     * Получает список специальностей по которым проводится обучение для указанной
     * формы обучения.
     *
     * @param extramural {@code true} для заочной формы обучения, иначе ложь
     * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
     */
    @Override
    public List<Speciality> fetchActual(final boolean extramural) {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT s FROM Speciality s WHERE (s.actual = true) AND "
                        + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :em)) > 0)"
                        + "ORDER BY s.actual DESC, s.name", Speciality.class);
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
    @Override
    public List<Speciality> fetchActual(final Department department) {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.speciality.actual = true) AND (dp.department = :dep) "
                        + "ORDER BY dp.speciality.actual DESC, dp.speciality.name", Speciality.class);
        query.setParameter("dep", department);
        return query.getResultList();
    }

    /**
     * Получает список специальностей по которым проводится набор.
     *
     * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
     */
    @Override
    public List<Speciality> fetchAviable() {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT s FROM Speciality s WHERE (s.actual = true) AND (s.aviable = true) ORDER BY s.actual DESC, s.name", Speciality.class);
        return query.getResultList();
    }

    /**
     * Получает список специальностей по которым проводится набор для указанной
     * формы обучения.
     *
     * @param extramural {@code true} для заочной формы обучения, иначе ложь
     * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
     */
    @Override
    public List<Speciality> fetchAviable(final boolean extramural) {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT s FROM Speciality s WHERE (s.actual = true) AND (s.aviable = true) AND "
                        + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :em)) > 0)"
                        + "ORDER BY s.actual DESC, s.name", Speciality.class);
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
    @Override
    public List<Speciality> fetchAviable(final Department department) {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.speciality.aviable = true)"
                        + " AND (dp.speciality.actual = true) AND (dp.department = :dep) "
                        + "ORDER BY dp.speciality.actual DESC, dp.speciality.name", Speciality.class);
        query.setParameter("dep", department);
        return query.getResultList();
    }

    /**
     * Возвращает список специальностей для подачи заявки поступающему.
     * Возвращаемый список не будет содержать специальности на которые данной
     * персоной уже были поданы заявки.
     *
     * @param person     поступающий
     * @param extramural форма обучение {@code true} если заочная, иначе ложь
     * @param year       год поступления
     * @return {@code List<Speciality>}, содержащий 0 или более специальностей.
     */
    @Override
    public List<Speciality> fetchSuggestions(final Person person, final boolean extramural, final int year) {
        TypedQuery<Speciality> query = em.createQuery(
                "SELECT s FROM Speciality s WHERE (s.actual = true) AND (s.aviable = true) AND "
                        + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :e)) > 0)"
                        + "AND (s.id NOT IN (SELECT r.speciality.id FROM Request r WHERE (r.extramural = :e) AND (r.person = :p) AND (r.year = :y)))"
                        + "AND (s.id NOT IN (SELECT sc.speciality.id FROM StudyCard sc WHERE (sc.person = :p) AND (sc.extramural = :e)))"
                        + "ORDER BY s.name", Speciality.class);
        query.setParameter("e", extramural);
        query.setParameter("p", person);
        query.setParameter("y", year);
        return query.getResultList();
    }

    /**
     * Производит поиск специальности похожей на образец
     *
     * @param sample образец для поиска
     * @return Экземпляр специальности или null, если ничего похожего найти не
     * удалось.
     */
    @Override
    public Speciality findLike(final Speciality sample) {
        try {
            TypedQuery<Speciality> query = em.createQuery(
                    "SELECT s FROM Speciality s WHERE (s.name LIKE :n)", Speciality.class);
            query.setParameter("n", sample.getName());
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Сохраняет специальность в базу данных.
     * Если идентификатор специальности равен нулю, то выполняется вызов
     * {@code em.persist(speciality)}. Если идентификатор не равен нулю, то
     * выполняется вызов {@code em.merge(speciality)}.
     *
     * @param speciality Специальность, которая будет сохранена
     * @return Специальность после сохранения (с измененным идентификатором при
     * добавлении новой).
     */
    @Override
    @WithLog
    @Restricted(allowedRoles = {}) // Разрешено только аминистратору (неявно)
    @Transactional(Transactional.TxType.REQUIRED)
    public Speciality save(Speciality speciality) {
        if (speciality.getId() == 0) {
            em.persist(speciality);
            return speciality;
        } else {
            return em.merge(speciality);
        }
    }

    /**
     * Удаляет специальность из базы данных.
     * При выполнении удаления производится поиск специальности по первичному
     * ключу и если специальность найдена - выполняется её удаление.
     *
     * @param speciality Специальность, которую надо удалить.
     */
    @Override
    @WithLog
    @Restricted(allowedRoles = {}) // Неявно разрешено администратору
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(final Speciality speciality) {
        Speciality item = em.find(Speciality.class, speciality.getId());
        if (null != item) {
            em.remove(item);
        }
    }
}
