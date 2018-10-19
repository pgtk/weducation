package ru.edu.pgtk.weducation.core.ejb

import ru.edu.pgtk.weducation.core.entity.ForeignLanguage
import ru.edu.pgtk.weducation.core.entity.Person
import spock.lang.Specification

import java.text.SimpleDateFormat

/**
 * Тестовый класс для DataCheckerEJB
 * Created by leonid on 01.06.16.
 */
class DataCheckerEJBSpec extends Specification {


    DataCheckerEJB instance

    def person = new Person(id: 1, firstName: "Иванов", middleName: "Иван", lastName: "Иванович",
            birthDate: parseDate("1996-05-12"), passportDate: parseDate("2010-05-12"), passportSeria: "32 10",
            passportNumber: "123456", passportDept: "Рудничный ОВД", placeCode: 5, averageBall: 3.14, inn: "123456789", foreign: false,
            language: ForeignLanguage.EN, invalid: false, orphan: false, birthPlace: "г. Прокопьевск",
            snils: "123-234-345", phones: "89132176444", address: "ул. Ленина 23, 17")

    def differ1 = new Person(id: 1, firstName: "Иванов", middleName: "Андрей", lastName: "Иванович",
            birthDate: parseDate("1996-05-12"), passportDate: parseDate("2010-05-13"), passportSeria: "32 10",
            passportNumber: "123458", passportDept: "Киселевский ОВД", placeCode: 5, averageBall: 4.4, inn: "123456321", foreign: false,
            language: ForeignLanguage.EN, invalid: true, orphan: false, birthPlace: "г. Киселевск",
            snils: "123-234-789", phones: "89132176432", address: "ул. Ленина 22, 19")

    def match1 = new Person(id: 1, firstName: "Иванов", middleName: "Иван", lastName: "Иванович",
            birthDate: parseDate("1996-05-12"), passportDate: null, passportSeria: null,
            passportNumber: null, passportDept: null, placeCode: 5, averageBall: 3.14, inn: null, foreign: false,
            language: ForeignLanguage.EN, invalid: false, orphan: false, birthPlace: "г. Прокопьевск",
            snils: null, phones: "89132176444", address: "ул. Ленина 23, 17")

    def match2 = new Person(passportNumber: "123456")

    def match3 = new Person(inn: "123456789")

    def match4 = new Person(snils: "123-234-345")

    def setup() {
        instance = new DataCheckerEJB()
    }

    // Данные разные
    def "person vs example1 will return <= 10"() {
        when:
        def level = instance.matchLevel(person, differ1)

        then:
        level <= 10
    }

    // Одинаковые персоны
    def "person vs person will return >= 150"() {
        when:
        def level = instance.matchLevel(person, person)

        then:
        level >= 150
    }

    // Среднестатистические разногласия в данных
    def "person vs example2 will return > 30"() {
        when:
        def level = instance.matchLevel(person, match1)

        then:
        level > 30
    }

    // Совпадает номер паспорта
    def "Test that same passport number will return >= 50"() {
        when:
        def level = instance.matchLevel(person, match2)

        then:
        level >= 50
    }

    // Совпадает ИНН
    def "Test that same inn will return >= 50"() {
        when:
        def level = instance.matchLevel(person, match3)

        then:
        level >= 50
    }

    // Совпадает СНИЛС
    def "Test that same snils will return >= 50"() {
        when:
        def level = instance.matchLevel(person, match4)

        then:
        level >= 50
    }

    def parseDate(String dateVal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
        try {
            return sdf.parse(dateVal)
        } catch (Exception e) {
            return null
        }
    }
}
