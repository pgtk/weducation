package ru.edu.pgtk.weducation.core.importcards;

import ru.edu.pgtk.weducation.core.ejb.*;
import ru.edu.pgtk.weducation.core.entity.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;

@Stateless
@Named("importCardEJB")
public class ImportCardEJB implements ImportCardDAO {

    @EJB
    private PlacesDAO places;
    @EJB
    private SchoolsDAO schools;
    @EJB
    private SpecialitiesDAO specialities;
    @EJB
    private PersonsDAO persons;
    @EJB
    private StudyCardsDAO cards;
    @EJB
    private DelegatesDAO delegates;
    @EJB
    private StudyGroupsDAO groups;
    @EJB
    private OldCardsDAO oldCards;



    /**
     * Импортирует все ыозможные данные.
     */
    public void importAll() {
        throw new EJBException("Данная функцияустарела и была отключена!");
    }

    /**
     * Импортируем данные только для одной группы
     * @param grpCode код группы
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public void importGroup(final String grpCode) {
        // Специальность
        Speciality spc = oldCards.getSpeciality(grpCode);
        Speciality exSpeciality = specialities.findLike(spc);
        if (exSpeciality != null) {
            spc = exSpeciality;
        } else {
            specialities.save(spc);
        }
        // Получим данные для группы
        StudyGroup group = oldCards.getGroup(grpCode);
        group.setSpeciality(spc);
        StudyGroup existingGroup = groups.findByName(group.getName());
        if (existingGroup != null) {
            // Пока будем кидать исключение, а там - посмотрим.
            throw new EJBException(String.format("Группа %s уже импортирована. Повторный импорт групп не поддерживается.", group.getName()));
        }

        List<String> studentCodes = oldCards.getStudentCodes(grpCode);
        if (studentCodes.isEmpty()) {
            throw new EJBException(String.format("Не найдено ни одного студента в группе %s.", group.getName()));
        }

        for (String personCode : studentCodes) {
            importPerson(group, personCode);
        }
    }

    /**
     * Импорт одной персоны.
     *
     * Если на момент импорта персона уже присутствует в БД, то данные не будут перезаписаны и такая персона будет пропущена.
     *
     * @param group группа новой БД, в которую будет импортирована персона.
     * @param personCode код персоны из старой БД
     */
    @Transactional(Transactional.TxType.REQUIRED)
    private void importPerson(StudyGroup group, String personCode) {
        // Для начала поищем уже имеющуюся запись персоны.
        Person psn = oldCards.getPerson(personCode);
        Person exPerson = persons.findLike(psn);
        if (exPerson != null) {
            // Персона уже есть. Не будем импортировать заново
            return;
        }
        // Импортируем населенный пункт
        Place place = oldCards.getPlace(personCode);
        if (place != null) {
            Place exPlace = places.findLike(place);
            if (null != exPlace) {
                place = exPlace;
            } else {
                places.save(place);
            }
        }
        // Импортируем учебное заведение
        School scl = oldCards.getSchool(personCode);
        School exSchool = schools.findLike(scl);
        if (null != exSchool) {
            scl = exSchool;
        } else {
            schools.save(scl);
        }
        psn.setPlace(place);
        persons.save(psn);
        // Собираем карточку...
        StudyCard card = oldCards.getCard(personCode);
        card.setSpeciality(group.getSpeciality());
        card.setGroup(group);
        card.setPerson(psn);
        card.setSchool(scl);
        // Сохраняем...
        cards.save(card);
        // Импортируем делегатов
        for (Delegate d : oldCards.getDelegates(personCode)) {
            d.setPerson(psn);
            delegates.save(d);
        }
    }
}
