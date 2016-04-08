package ru.edu.pgtk.weducation.reports

import ru.edu.pgtk.weducation.ejb.GroupSemestersDAO
import ru.edu.pgtk.weducation.ejb.StudyGroupsDAO
import ru.edu.pgtk.weducation.ejb.StudycardsDAO
import ru.edu.pgtk.weducation.ejb.SubjectsDAO
import ru.edu.pgtk.weducation.entity.*
import spock.lang.Specification

import javax.ejb.EJBException
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Response

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
		_ * get(1) >> new Subject(id: 1, fullName: "Тестовая дисциплина", shortName: "ТД")
		_ * get(_) >> { throw new EJBException("No Subject with souch ID") }
		_ * fetch(testGroup, 1, 1) >> [new Subject(id: 1, fullName: "Дисциплина1", shortName: "Д 1"),
		                               new Subject(id: 2, fullName: "Дисциплина2", shortName: "Д 2"),
		                               new Subject(id: 3, fullName: "Дисциплина3", shortName: "Д 3"),
		                               new Subject(id: 4, fullName: "Дисциплина4", shortName: "Д 4"),
		                               new Subject(id: 5, fullName: "Дисциплина5", shortName: "Д 5")]
		_ * fetch(testGroup, 1, 2) >> new ArrayList<Subject>(); // Допустим, за второй семестр оценок нет
	}

	def groups = Mock(StudyGroupsDAO) {
		_ * get(_) >> testGroup;
	}

	def semester = Mock(GroupSemestersDAO) {
		_ * get(_, _, _) >> new GroupSemester(id: 1, group: testGroup, semester: 1, course: 1, beginYear: 2016, beginMonth: 9, beginWeek: 1,
				endYear: 2016, endMonth: 12, endWeek: 3)
	}

	def cards = Mock(StudycardsDAO) {
		_ * findByGroup(_) >> [
				new StudyCard(id: 1, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Иванов", middleName: "Иван", lastName: "Иванович")),
				new StudyCard(id: 2, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Петров", middleName: "Иван", lastName: "Иванович")),
				new StudyCard(id: 3, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Сидоров", middleName: "Иван", lastName: "Иванович")),
				new StudyCard(id: 4, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Навальный", middleName: "Николай", lastName: "Иванович")),
				new StudyCard(id: 5, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Кекс", middleName: "Антон", lastName: "Иванович")),
				new StudyCard(id: 6, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Иванов", middleName: "Сергей", lastName: "Иванович")),
				new StudyCard(id: 7, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Михайлов", middleName: "Стас", lastName: "Николаевич")),
				new StudyCard(id: 8, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Шаманов", middleName: "Иван", lastName: "Иванович")),
				new StudyCard(id: 9, active: true, remanded: false, extramural: false, person: new Person(id: 1, firstName: "Цой", middleName: "Виктор", lastName: "Робертович"))
		]
	}

	def school = new School(id: 1, fullName: "Тестовое учебное заведение имени разработчиков программного обеспечения",
			shortName: "ТУЗ им. РПО", director: "Фамилия И. О.")

	def GroupReportsEJB report

	def setup() {
		// Создаем класс с подмененными зависимостями
		report = new GroupReportsEJB(subjects: subjects, groups: groups, groupSemesters: semester, school: school, cards: cards)
		// Иннициализируем класс для работы (обычно это выполняется автоматически)
		report.initBean()
	}

/**
 * Проверка с нормальными параметрами
 */
	def "Exam report call with correct parameters will produce correct response"() {
		expect:
		def result = report.examReport(groupCode, courseNum, semesterNum, subjectCode)
		result instanceof Response
		result.status == 200
		result.hasEntity()

		where:
		groupCode | courseNum | semesterNum | subjectCode
		1         | 1         | 1           | 1
		1         | 1         | 2           | 1
		1         | 2         | 3           | 1
		1         | 2         | 4           | 1
		1         | 3         | 5           | 1
		1         | 3         | 6           | 1
		1         | 4         | 7           | 1
		1         | 4         | 8           | 1
	}

	/**
	 * Проверка с некорректными параметрами
	 */
	def "Exam report with incorrect parameters will produce exception"() {
		when:
		def result = report.examReport(groupCode, courseNum, semesterNum, subjectCode)

		then:
		thrown(NotFoundException)

		where:
		groupCode | courseNum | semesterNum | subjectCode
		2         | 3         | 1           | 3
		3         | 3         | 2           | 4
		4         | 4         | 1           | 5
		5         | 4         | 2           | 6
		-1        | 1         | 1           | 6
		1         | 1         | 1           | -1
		0         | 3         | 1           | 3
		3         | 3         | 2           | 0
	}
}
