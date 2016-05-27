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
        correctString(val, text)

        where:
        val  | text
        0    | "недель"
        0.5  | "недели"
        1    | "неделя"
        1.5  | "недели"
        2    | "недели"
        2.5  | "недели"
        3    | "недели"
        3.5  | "недели"
        5    | "недель"
        6.5  | "недель"
        15   | "недель"
        20.5 | "недель"
        21   | "неделя"
        21.5 | "недели"
        22   | "недели"
        22.5 | "недели"
        23   | "недели"
    }

    def boolean correctString(float value, String suffix) {
        String sampleString = String.format("%2.1f %s", value, suffix);
        return Utils.getLenString(value) == sampleString
    }
}
