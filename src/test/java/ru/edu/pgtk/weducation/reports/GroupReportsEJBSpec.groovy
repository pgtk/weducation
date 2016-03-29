package ru.edu.pgtk.weducation.reports

import spock.lang.Specification

/**
 * Тестовый класс для проверки работоспособости компонента генерации ведомостей для группы.
 * Created by leonid on 28.03.16.
 */
class GroupReportsEJBSpec extends Specification {

    def GroupReportsEJB report;

    /**
     * TODO Допилить тест.
     * 1. Сделать иерархию интерфейсов
     * 2. Сделать фейковые EJB, реализующие те же интерфейсы.
     * 3. Подставить в тестовый класс фейковые EJB и написать тесты.
     */
    def "Test that incorrect parameters will produce an error" () {
        when:
        report.examReport(1, 2, 4, 5)

        then:
        thrown(NullPointerException)
    }
}
