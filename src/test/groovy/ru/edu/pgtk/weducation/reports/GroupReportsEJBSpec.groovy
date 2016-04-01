package groovy.ru.edu.pgtk.weducation.reports

import ru.edu.pgtk.weducation.ejb.GroupSemestersDAO
import ru.edu.pgtk.weducation.ejb.StudyGroupsDAO
import ru.edu.pgtk.weducation.ejb.SubjectsDAO
import ru.edu.pgtk.weducation.entity.*
import ru.edu.pgtk.weducation.reports.GroupReportsEJB
import spock.lang.Specification

import javax.ws.rs.NotFoundException

/**
 * Тестовый класс для проверки работоспособости компонента генерации ведомостей для группы.
 * Created by leonid on 28.03.16.
 */
class GroupReportsEJBSpec extends Specification {

    def speciality = new Speciality(id: 1, name: "ТСДМТ", actual: true, aviable: true, description: "Тестовая специальность для модульных тестов")

    def plan = new StudyPlan(id: 1, speciality: speciality, specialityCode: 1, specialityKey: "12.34.56", specialityName: "Тестовая специальность для модульных тестов",
            specialization: "не предусмотрено", kvalification: "техник проверяльщик", beginYear: 2016, extramural: false)

    def testGroup = new StudyGroup(id: 1, name: "ГДТК-01", commercial: false, course: 1, speciality: speciality, specialityCode: 1, planCode: 1,
            plan: plan, master: "Фамилия Имя Отчество", year: 2016)

    // Фейковый класс для иммитации EJB компонента
    def subjects = Mock(SubjectsDAO) {
        _ * get(_) >> new Subject(id: 1, fullName: "Тестовая дисциплина", shortName: "ТД");
        _ * fetch(_, _, _) >> [new Subject(id: 1, fullName: "Дисциплина1", shortName: "Д 1"),
                               new Subject(id: 2, fullName: "Дисциплина2", shortName: "Д 2"),
                               new Subject(id: 3, fullName: "Дисциплина3", shortName: "Д 3"),
                               new Subject(id: 4, fullName: "Дисциплина4", shortName: "Д 4"),
                               new Subject(id: 5, fullName: "Дисциплина5", shortName: "Д 5")]
    }

    def groups = Mock(StudyGroupsDAO) {
        _ * get(_) >> testGroup;
    }

    def semester = Mock(GroupSemestersDAO) {
        _ * get(_, _, _) >> new GroupSemester(id: 1, group: testGroup, semester: 1, course: 1, beginYear: 2016, beginMonth: 9, beginWeek: 1,
                endYear: 2016, endMonth: 12, endWeek: 3)
    }

    def GroupReportsEJB report

    def setup() {
        report = new GroupReportsEJB(subjects: subjects, groups: groups, groupSemesters: semester)
    }

    /**
     * TODO Допилить тест.
     * 1. Сделать иерархию интерфейсов
     * 2. Сделать фейковые EJB, реализующие те же интерфейсы.
     * 3. Подставить в тестовый класс фейковые EJB и написать тесты.
     */
    def "Test that incorrect parameters will produce an error"() {


        when:
        report.examReport(1, 1, 1, 1)

        then:
        thrown(NotFoundException)
    }
}
