package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента учебных планов
 *
 * @author Voronin Leonid
 * @since 10.04.2016
 */
public interface StudyPlansDAO extends EntityDAO<StudyPlan> {

    List<StudyPlan> findBySpeciality(final Speciality spc);

    List<StudyPlan> findBySpeciality(final Speciality spc, final boolean extramural);

    List<StudyPlan> findByDepartment(final Department dep);

    List<StudyPlan> findLike(final StudyPlan plan);

}
