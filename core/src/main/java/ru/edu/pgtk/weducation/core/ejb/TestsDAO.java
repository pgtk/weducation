package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.Test;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для работы с тестами
 * Created by admin on 17.05.2016.
 */
public interface TestsDAO extends EntityDAO<Test> {

    List<Test> fetchForSpeciality(final Speciality speciality);
}
