package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.ClientSession;

/**
 * Интерфейс корпоративного компонента для клиентских сессий
 * Created by admin on 01.06.2016.
 */
public interface ClientSessionsDAO extends EntityDAO<ClientSession> {

    ClientSession add(ClientSession item);

    ClientSession update(ClientSession item);
}
