package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.*;

import java.util.List;

/**
 * Интерфейс класса для дисциплин
 * @author Voronin Leonid
 * @since 31.03.2016
 */
public interface SubjectsDAO extends WeakEntityDAO<Subject> {

	int getMaxLoad(final Subject subject);

	int getAudLoad(final Subject subject);

	List<Subject> fetchAll(final StudyPlan plan);

	List<Subject> fetchForCard(final StudyCard card);

	List<Subject> fetchForModule(final StudyModule module);

	List<Subject> fetchNoModules(final StudyPlan plan);

	List<Subject> fetchCourseWorksForCard(final StudyCard card);

	List<Subject> fetchCourseWorks(final StudyGroup group, final int course, final int semester);

	List<Subject> fetchExams(final StudyGroup group, final int course, final int semester);

	List<Subject> fetchZachets(final StudyGroup group, final int course, final int semester);

	List<Subject> fetch(final StudyGroup group, final int course, final int semester);

	void copy(final Subject source, final Subject destination);
}
