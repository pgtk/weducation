package ru.edu.pgtk.weducation.core.reports.dao;

import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.reports.entity.DiplomeBlank;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Класс для получения списков бланков дипломов
 * Created by leonid on 17.06.16.
 */
public class DiplomeBlanksDAO {

    @PersistenceContext(unitName = "weducationPU")
    private EntityManager em;

    public List<DiplomeBlank> fetchAll() {
        // TODO выбрать всё по аналогии с другими методами этого класса и добавить сортировку
        return null;
    }

    /**
     * Получает список дипломов по конкретному отделению.
     * Поиск бланков производится по специальности на основании информации из профилей отделений.
     *
     * @param department Отделение, для которого нужен список
     * @return List<DiplomeBlank>
     */
    public List<DiplomeBlank> fetchForDepartment(final Department department) {
        if (department == null) {
            throw new IllegalArgumentException("You can't fetch data for NULL Department!");
        }
        TypedQuery<DiplomeBlank> q = em.createQuery("SELECT db FROM DiplomeBlank db WHERE (db.speciality.id IN " +
                "(SELECT dp.speciality.id FROM DepartmentProfile dp WHERE (dp.department = :dep))) ORDER BY db.registrationNumber", DiplomeBlank.class);
        q.setParameter("dep", department);
        return q.getResultList();
    }

    /**
     * Получает список бланков дипломов в зависимости от формы обучения. Сортируется по регистрационному номеру
     *
     * @param extramural true, если нужен список для заочной формы обучения
     * @return List<DiplomeBlank>
     */
    public List<DiplomeBlank> fetchAll(boolean extramural) {
        TypedQuery<DiplomeBlank> q = em.createQuery(
                "SELECT db FROM DiplomeBlank db WHERE (db.extramural = :em) ORDER BY db.registrationNumber", DiplomeBlank.class);
        q.setParameter("em", extramural);
        return q.getResultList();
    }
}
