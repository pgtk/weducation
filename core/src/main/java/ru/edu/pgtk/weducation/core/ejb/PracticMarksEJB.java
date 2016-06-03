package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Practic;
import ru.edu.pgtk.weducation.core.entity.PracticMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

/**
 * Корпоративный компонент для реализации CRUD оценок по практике.
 * Данный класс работает с практиками, которые привязаны к модулям. Существуют
 * также итоговые практики, которые идет в выписку.
 *
 * @author Воронин Леонид.
 */
@Stateless
@Named("practicMarksEJB")
public class PracticMarksEJB extends AbstractEJB implements PracticMarksDAO {

    @EJB
    StudyCardsDAO cards;

    @Override
    public PracticMark get(int id) {
        PracticMark result = em.find(PracticMark.class, id);
        if (result != null) {
            return result;
        }
        throw new EJBException("PracticMark not found with id " + id);
    }

    /**
     * Получает из базы данных информацию об оценке за практику.
     * <p>
     * Выбирается одна оценка для конкретной личной карточки по конкретной
     * практике.</p>
     * <p>
     * Если оценка в базе данных отсутствует, создается новый экземпляр и
     * возвращается так, ка буд-то он был выбран из базы.</p>
     *
     * @param card    личная карточка персоны
     * @param practic практика
     * @return Оценка за практику для конкретной личной карточке.
     */
    @Override
    public PracticMark get(final StudyCard card, final Practic practic) {
        try {
            TypedQuery<PracticMark> q = em.createQuery(
                    "SELECT pm FROM PracticMark pm WHERE (pm.practic = :p) AND (pm.card = :c)", PracticMark.class);
            q.setParameter("p", practic);
            q.setParameter("c", card);
            return q.getSingleResult();
        } catch (NoResultException e) {
      /* Отсутствие результата - вполне возможный вариант, если мы
       запрашиваем оценки впервые при выставлении их из клиента. */
            PracticMark result = new PracticMark();
            result.setCard(card);
            result.setPractic(practic);
            return result;
        }
    }

    /**
     * Получает список оценок по практике для группы.
     * Для указанной группы выбирается список личных карточек, после чего для
     * каждой личной карточки вызывается метод
     * {@code get(StudyCard card, Practic practic)}.
     *
     * @param group   группа
     * @param practic практика, для которой выбираются оценки
     * @return Список оценок, содержащий ровно столько оценок, сколько личных
     * карточек зарегистрировано для группы.
     */
    @Override
    public List<PracticMark> fetchAll(final StudyGroup group, final Practic practic) {
        List<PracticMark> result = new LinkedList<>();
        for (StudyCard sc : cards.findByGroup(group)) {
            result.add(get(sc, practic));
        }
        return result;
    }

    /**
     * Сохраняет оценку в базу данных.
     *
     * @param item оценка
     * @return Оценка с изменениями послед созранения в базу данных.
     */
    @Override
    public PracticMark save(PracticMark item) {
        if (item.getId() == 0) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    /**
     * Удаляет оценку из базы данных.
     *
     * @param item оценка
     */
    @Override
    public void delete(PracticMark item) {
        PracticMark mark = em.find(PracticMark.class, item.getId());
        if (null != mark) {
            em.remove(item);
        }
    }
}
