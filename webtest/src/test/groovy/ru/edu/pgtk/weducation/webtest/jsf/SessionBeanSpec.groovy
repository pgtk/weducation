package ru.edu.pgtk.weducation.webtest.jsf

import ru.edu.pgtk.weducation.core.ejb.SpecialitiesDAO
import ru.edu.pgtk.weducation.core.ejb.StudyCardsDAO
import ru.edu.pgtk.weducation.core.ejb.TestsDAO
import ru.edu.pgtk.weducation.core.entity.Speciality
import ru.edu.pgtk.weducation.core.entity.StudyCard
import ru.edu.pgtk.weducation.core.entity.Test
import spock.lang.Specification

import javax.ejb.EJBException
import javax.persistence.NoResultException

/**
 * Тестовый класс для проверки сессионного компонента-подложки для тестов
 * Created by leonid on 21.05.16.
 */
class SessionBeanSpec extends Specification {

    def SessionBean sessionBean;

    // предопределяем несколько специальностей
    def Speciality toa = new Speciality(name: "ТОА", id: 1)
    def Speciality gem = new Speciality(name: "ГЭМ", id: 2)
    def Speciality pks = new Speciality(name: "ПКС", id: 3)
    def Speciality eko = new Speciality(name: "ЭКО", id: 4)

    // Подставной функционал специальностей
    def specialities = Mock(SpecialitiesDAO) {
        _ * fetchActual(false) >> [pks, eko]
        _ * fetchActual(true) >> [toa, gem]
        _ * get(1) >> toa
        _ * get(2) >> gem
        _ * get(3) >> pks
        _ * get(4) >> eko
        _ * get(0) >> { throw new EJBException("Speciality not found with id 0") }
    }

    def tests = Mock(TestsDAO) {
        _ * fetchForSpeciality(pks) >> [new Test(id: 1, title: "Пробный тест", author: "Разработчик")]
    }

    // Предопределенные личные карточки
    def correctCard = new StudyCard(id: 1, speciality: pks, specialityCode: pks.id)

    // Подставной функционал личных карточек
    def cards = Mock(StudyCardsDAO) {
    }

    // Номера студенческих билетов
    def String NUMBER = "123"

    def setup() {
        sessionBean = new SessionBean(specialitiesDao: specialities, cardsDao: cards, testsDAO: tests);
    }

    // Изменение формы обучения должно менять список специальностей
    def "Test that change study form will change speciality list"() {
        setup:
        def firstList = sessionBean.specialitiesList;
        sessionBean.setExtramural(!sessionBean.extramural)
        when:
        def secondList = sessionBean.specialitiesList;
        then:
        assert !isSameList(firstList, secondList);
    }

    // Проверка успешного входа
    def "Test that login with correct parameters will be successfull"() {
        setup:
        sessionBean.setExtramural(false)
        sessionBean.setSpecialityCode(pks.id)
        sessionBean.setBiletNumber(NUMBER)
        when:
        assert !sessionBean.authorized
        sessionBean.login()
        then:
        noExceptionThrown()
        1 * cards.get(pks, false, NUMBER) >> correctCard
        assert sessionBean.authorized;
    }

    // Проверка неудачного входа
    def "Test that login with incorrect parameters will fail"() {
        setup:
        sessionBean.setExtramural(true)
        sessionBean.setSpecialityCode(toa.id)
        sessionBean.setBiletNumber(NUMBER)
        when:
        assert !sessionBean.authorized
        sessionBean.login()
        then:
        noExceptionThrown()
        1 * cards.get(toa, true, NUMBER) >> { throw new NoResultException("No result for souch parameters!") }
        assert !sessionBean.authorized;
    }

    // Неправильный код специальности
    def "Test that login() will not throw exception (incorrect speciality code)"() {
        setup:
        sessionBean.setExtramural(true)
        sessionBean.setSpecialityCode(0)
        sessionBean.setBiletNumber(NUMBER)
        when:
        assert !sessionBean.authorized
        sessionBean.login()
        then:
        noExceptionThrown()
        assert !sessionBean.authorized;
    }

    // Неправильный номер студенческого билета
    def "Test that login() will not throw exception (NULL string)"() {
        setup:
        sessionBean.setExtramural(true)
        sessionBean.setSpecialityCode(toa.id)
        sessionBean.setBiletNumber(null)
        when:
        assert !sessionBean.authorized
        sessionBean.login()
        then:
        noExceptionThrown()
        1 * cards.get(toa, true, null) >> { throw new NullPointerException(null) }
        assert !sessionBean.authorized;
    }

    // Сравнение списков специальностей
    public boolean isSameList(List<Speciality> first, List<Speciality> second) {
        if (first == null && second == null) {
            return true
        }
        if (first == null) {
            return false
        }
        if (second == null) {
            return false
        }
        if (first.size() != second.size()) {
            return false;
        }
        for (int i = 0; i < first.size(); i++) {
            Speciality fspc = first.get(i);
            Speciality sspc = second.get(i);
            if (fspc.id != sspc.id) {
                return false;
            }
        }
        return true;
    }
}
