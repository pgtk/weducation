package ru.edu.pgtk.weducation.webui.jsf

import ru.edu.pgtk.weducation.core.ejb.AccountsDAO
import ru.edu.pgtk.weducation.core.ejb.ClientSessionsEJB
import ru.edu.pgtk.weducation.core.ejb.SessionEJB
import ru.edu.pgtk.weducation.core.entity.Account
import ru.edu.pgtk.weducation.core.entity.AccountRole
import ru.edu.pgtk.weducation.core.entity.ClientSession
import spock.lang.Specification

/**
 * Класс для тестирования сессионного компонента-подложки.
 * Created by Voronin leonid on 31.05.16.
 */
class SessionMBSpec extends Specification {

    def SessionMB sessionBean

    // Фейковый пользователь-отделение
    def department = new Account(id: 1, login: "department", fullName: "Отделение", role: AccountRole.DEPARTMENT, code: 1)
    // Фейковый пользователь-администратор
    def admin = new Account(id: 1, login: "admin", fullName: "Администратор", role: AccountRole.ADMIN, code: 0)
    // Фейковый пользователь-учебная часть
    def depot = new Account(id: 1, login: "depot", fullName: "Учебная часть", role: AccountRole.DEPOT, code: 0)
    // Фейковый пользователь-Приемная комиссия
    def reception = new Account(id: 1, login: "reception", fullName: "Приемная комиссия", role: AccountRole.RECEPTION, code: 0)

    // Подставной сервис, который будет возвращать нам пользователей
    def accountsDao = Mock(AccountsDAO) {
        _ * get("department", "secret") >> department
        _ * get("admin", "admin") >> admin
        _ * get("depot", "test") >> depot
        _ * get("reception", "recep") >> reception
        _ * get(_, _) >> null
    }

    // Подставной класс для иммитации компонента для клиентских сессий
    def clientSessionsDao = Mock(ClientSessionsEJB) {

    }

    def setup() {
        sessionBean = new SessionMB(usersEJB: accountsDao, session: new ClientSession(),
                ejbSession: new SessionEJB(), sessionsDao: clientSessionsDao)
    }

    // Тест попытки входа пользователя с правильными логином и паролем
    def "Test that login with correct username and password will correct"() {
        when:
        sessionBean.setLogin("department")
        sessionBean.setPassword("secret")
        sessionBean.doLogin()

        then:
        noExceptionThrown()
        1 * clientSessionsDao.save(_)
        sessionBean.logged
        !sessionBean.admin
    }

    // Тест попытки входа пользователя с неправильным паролем
    def "Test that login with incorrect password will fail"() {
        when:
        sessionBean.setLogin("department")
        sessionBean.setPassword("wrongpassword")
        sessionBean.doLogin()

        then:
        noExceptionThrown()
        0 * clientSessionsDao.save(_)
        !sessionBean.logged
        !sessionBean.admin
    }

    // Тест попытки входа пользователя с неправильным логином
    def "Test that login with incorrect login will fail"() {
        when:
        sessionBean.setLogin("unknown")
        sessionBean.setPassword("secret")
        sessionBean.doLogin()

        then:
        noExceptionThrown()
        0 * clientSessionsDao.save(_)
        !sessionBean.logged
        !sessionBean.admin
    }

    // Тест попытки входа администратора с правильными логином и паролем
    def "Test that admin login will set isAdmin flag on "() {
        when:
        sessionBean.setLogin("admin")
        sessionBean.setPassword("admin")
        sessionBean.doLogin()

        then:
        noExceptionThrown()
        1 * clientSessionsDao.save(_)
        sessionBean.logged
        sessionBean.admin
    }

    // Тест выхода из учетной записи администратора
    def "Test that admin logout will reset isAdmin and isLogged flags"() {
        setup:
        sessionBean.setLogin("admin")
        sessionBean.setPassword("admin")
        sessionBean.doLogin() // Входим

        when:
        sessionBean.doLogout() // выходим

        then:
        noExceptionThrown()
        1 * clientSessionsDao.save(_)
        !sessionBean.logged
        sessionBean.login != old(sessionBean.login)
        !sessionBean.admin
        sessionBean.admin != old(sessionBean.admin)
    }
}
