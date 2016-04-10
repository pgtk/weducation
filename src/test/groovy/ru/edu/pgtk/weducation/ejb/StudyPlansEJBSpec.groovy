package ru.edu.pgtk.weducation.ejb

import ru.edu.pgtk.weducation.entity.Speciality
import ru.edu.pgtk.weducation.entity.StudyPlan
import spock.lang.Specification

import javax.ejb.EJBException
import javax.persistence.EntityManager

/**
 * Тестовый класс для корпоративного компонента учебных планов
 * @since 10.04.2016
 */
class StudyPlansEJBSpec extends Specification {

	// Специальность, которая будет использована для тестирования
	def spec = new Speciality(id: 1, name: "ПC", description: "Правильная специальность")

	// Подменяем менеджер постоянства, к которому будет обращаться тестируемый EJB компонент
	def em = Mock(EntityManager) {
		_ * find(StudyPlan.class, 0) >> { throw new EJBException("StudyPlan not found with id 0") }
		_ * find(StudyPlan.class, 1) >> new StudyPlan(id: 1, name: "Test plan", specialityKey: "01.02.03", speciality: spec, specialityCode: 1,
				specialityName: "Тестовая специальность", specialization: "не предусмотрено", kvalification: "test")
	}

	def specDao = Mock(SpecialitiesDAO) {
		_ * get(1) >> spec
		_ * get(0) >> { throw new EJBException("No specialities found with id 0") }
	}

	// Экземпляр тестируемого класса
	def StudyPlansDAO instance = new StudyPlansEJB(em: em, specialities: specDao)

	def "Test that get() method with correct id will return StudyPlan"() {
		when:
		def plan = instance.get(1)

		then:
		noExceptionThrown()
		plan != null
		plan.id == 1;
	}

	def "Test that get() method with incorrect id will throw exception"() {
		when:
		def plan = instance.get(0)

		then:
		thrown(EJBException)
	}

	def "Test that save() correct plan will not throw Exception and will call persist() method"() {
		setup:
		def studyPlan = new StudyPlan(id: 0, specialityCode: 1, name: "Test Plan", kvalification: "kvalification", specialization: "specialization",
				specialityName: "Test", specialityKey: "01.02.03")

		when:
		def result = instance.save(studyPlan)

		then:
		noExceptionThrown()
		1 * em.persist(studyPlan)
		result != null
	}

	def "Test that save() correct plan will not throw Exception and will call merge() method"() {
		setup:
		def studyPlan = new StudyPlan(id: 3, specialityCode: 1, name: "Test Plan", kvalification: "kvalification", specialization: "specialization",
				specialityName: "Test", specialityKey: "01.02.03")

		when:
		def result = instance.save(studyPlan)

		then:
		noExceptionThrown()
		1 * em.merge(studyPlan) >> studyPlan
		result != null
	}

	def "Test that save() incorrect plan will throw Exception"() {
		setup:
		def studyPlan = null

		when:
		def result = instance.save(studyPlan)

		then:
		thrown(IllegalArgumentException)
	}

	def "Test that save() correct plan with incorrect speciality code will throw Exception (persist)"() {
		setup:
		def studyPlan = new StudyPlan(id: 0, specialityCode: 0, name: "Test Plan", kvalification: "kvalification", specialization: "specialization",
				specialityName: "Test", specialityKey: "01.02.03")

		when:
		def result = instance.save(studyPlan)

		then:
		thrown(EJBException)
	}

	def "Test that save() correct plan with incorrect speciality code will throw Exception (merge)"() {
		setup:
		def studyPlan = new StudyPlan(id: 3, specialityCode: 0, name: "Test Plan", kvalification: "kvalification", specialization: "specialization",
				specialityName: "Test", specialityKey: "01.02.03")

		when:
		def result = instance.save(studyPlan)

		then:
		thrown(EJBException)
	}
}
