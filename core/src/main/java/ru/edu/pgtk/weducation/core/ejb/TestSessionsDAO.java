package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Test;
import ru.edu.pgtk.weducation.core.entity.TestSession;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для управления сеансами тестирования
 * Created by admin on 18.05.2016.
 */
public interface TestSessionsDAO extends EntityDAO<TestSession> {

    List<TestSession> fetch(final Test test, final Person person);

    int getSessionsCount(final Test test, final Person person);
}
