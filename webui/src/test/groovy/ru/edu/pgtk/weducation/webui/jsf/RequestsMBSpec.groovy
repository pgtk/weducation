package ru.edu.pgtk.weducation.webui.jsf

import spock.lang.Specification

/**
 * Тестовый класс для проверки компонента-подложки подачи заявок.
 *
 * Created by Voronin Leonid on 28.05.16.
 */
class RequestsMBSpec extends Specification {


    def RequestsMB request // Экземпляр класса для тестирования

    def setup() {
        request = new RequestsMB() // Для каждого теста создаем новый
    }

    // Проверим, что при выполнении метода addRequest класс меняет состояние
    def "Test that addRequest() will change class state"() {
        when:
        request.addRequests();

        then:
        noExceptionThrown()
        request.edit != old(request.edit)
    }

    // Проверим, что при выполнении метода cancel класс меняет состояние
    // если находился в режиме редактирования
    def "Test that cancel() will change state from edit=true to edit=false"() {
        when:
        request.addRequests(); // now edit should be true
        request.cancel();

        then:
        noExceptionThrown()
        !request.edit // edit should be false
    }

    // Проверим, что при выполнении метода cancel класс не меняет состояние
    // если находился в режиме просмотра
    def "Test that cancel() will do nothing if not in edit state"() {
        when:
        request.cancel();

        then:
        noExceptionThrown()
        request.edit == old(request.edit)
    }

    // Проверим, что метод getYears вернет не пустую мапу с годами
    def "Test that getYears returns non empty map"() {
        when:
        def years = request.years;

        then:
        noExceptionThrown()
        years != null
        !years.isEmpty()
    }

    // Проверим, что метод getYears вернет мапу, которая будет содержать текущий год
    def "Test that getYears contains current year"() {
        setup:
        def year = getCurrentYear()

        when:
        def years = request.years

        then:
        noExceptionThrown()
        years.containsValue(year)
    }

    // Служебный метод для вычисления текущего года.
    def getCurrentYear() {
        Calendar c = new GregorianCalendar();
        return c.get(Calendar.YEAR);
    }
}
