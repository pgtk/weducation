package ru.edu.pgtk.weducation.webui.jsf

import ru.edu.pgtk.weducation.core.ejb.DepartmentsDAO
import ru.edu.pgtk.weducation.core.ejb.SessionDAO
import ru.edu.pgtk.weducation.core.entity.Account
import ru.edu.pgtk.weducation.core.entity.AccountRole
import ru.edu.pgtk.weducation.core.entity.Department
import ru.edu.pgtk.weducation.core.reports.dao.DiplomeBlanksDAO
import spock.lang.Specification

/**
 * Класс для тестирования компонента-подложки для отчета по дипломам
 * Created by leonid on 20.06.16.
 */
class DiplomeBlankReportMBSpec extends Specification {

    // пользователь администратор для тестирования
    def depot = new Account(id: 1, login: "depot", role: AccountRole.DEPOT, fullName: "Учебная часть")
    // пользователь отделения для тестирования
    def department = new Account(id: 2, login: "department", role: AccountRole.DEPARTMENT, code: 1, fullName: "Отделение техникума")
    // пользователь приемной комисии для тестирования
    def reception = new Account(id: 3, login: "reception", role: AccountRole.RECEPTION, fullName: "Приемная комиссия")

    // Отделение для теста
    def testDep = new Department(id: 1, name: "Тестовое отделение")

    // Провайдер отделений
    def departmentsDao = Mock(DepartmentsDAO) {
        _ * get(1) >> testDep
    }

    // Иммитация сессии
    def SessionDao = Mock(SessionDAO) {
    }

    // Иммитация компонента для получения бланков
    def blanksDao = Mock(DiplomeBlanksDAO) {
    }

    // Экземпляр тестируемого класса
    def DiplomeBlankReportMB instance;

    // Создаем новую копию тестируемого класса перед каждым тестом
    def setup() {
        instance = new DiplomeBlankReportMB(sessionDao: SessionDao, departmentsDao: departmentsDao, blanksDao: blanksDao)
    }

    // Учебная часть должна получить полный список бланков
    def "Test that depot user will get full list with no error"() {
        when:
        instance.getList()

        then:
        noExceptionThrown()
        1 * SessionDao.getUser() >> depot
        1 * blanksDao.fetchAll() >> new ArrayList<>()
        !instance.error
    }

    // Учебная часть должна получить список бланков для заочников
    def "Test that depot user will get extramural list with no error"() {
        when:
        instance.setStudyFormCode(2) // Заочное отделение
        instance.getList()

        then:
        noExceptionThrown()
        1 * SessionDao.getUser() >> depot
        0 * departmentsDao.get(_) // Отделение не должно запрашиваться
        1 * blanksDao.fetchAll(true) >> new ArrayList<>()
        !instance.error
    }

    // Учебная часть должна получить список бланков для очников
    def "Test that depot user will get not extramural list with no error"() {
        when:
        instance.setStudyFormCode(1) // Очное отделение
        instance.getList()

        then:
        noExceptionThrown()
        1 * SessionDao.getUser() >> depot
        0 * departmentsDao.get(_) // Отделение не должно запрашиваться
        1 * blanksDao.fetchAll(false) >> new ArrayList<>()
        !instance.error
    }

    // Отделение должно получить свой список бланков
    def "Test that department user will get list with no error"() {
        when:
        instance.getList()

        then:
        noExceptionThrown()
        1 * SessionDao.getUser() >> department
        1 * departmentsDao.get(1) >> testDep
        1 * blanksDao.fetchForDepartment(testDep) >> new ArrayList<>()
        !instance.error
    }

    // Приемная комиссия не должна получить список бланков
    def "Test that reception user will not get list and get error"() {
        when:
        instance.getList()

        then:
        noExceptionThrown()
        1 * SessionDao.getUser() >> reception
        0 * departmentsDao.get(_) // Отделение не должно запрашиваться
        0 * blanksDao.fetchAll()
        instance.error
    }

}
