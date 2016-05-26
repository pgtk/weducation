package ru.edu.pgtk.weducation.core.utils

import spock.lang.Specification

/**
 * Тестовый класс для методов  утилит
 *
 * Created by leonid on 26.05.16.
 */
class UtilsSpec extends Specification {


    def "Test than prefix for float week number will be correct"() {
        expect:
        Utils.getLenString(val) == text

        where:
        val  | text
        0    | "0,0 недель"
        0.5  | "0,5 недели"
        1    | "1,0 неделя"
        1.5  | "1,5 недели"
        2    | "2,0 недели"
        2.5  | "2,5 недели"
        3    | "3,0 недели"
        3.5  | "3,5 недели"
        5    | "5,0 недель"
        6.5  | "6,5 недель"
        15   | "15,0 недель"
        20.5 | "20,5 недель"
        21   | "21,0 неделя"
        21.5 | "21,5 недели"
        22   | "22,0 недели"
        22.5 | "22,5 недели"
        23   | "23,0 недели"
    }
}
