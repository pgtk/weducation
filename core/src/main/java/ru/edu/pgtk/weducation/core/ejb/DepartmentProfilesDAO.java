package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.DepartmentProfile;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для профилей отделений
 * Created by leonid on 02.06.16.
 */
public interface DepartmentProfilesDAO extends EntityDAO<DepartmentProfile> {

    List<DepartmentProfile> findByDepartment(final Department department);
}
