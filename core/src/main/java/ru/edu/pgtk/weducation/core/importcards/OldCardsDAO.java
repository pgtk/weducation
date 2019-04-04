package ru.edu.pgtk.weducation.core.importcards;

import ru.edu.pgtk.weducation.core.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс для доступа к данным в старой БД
 *
 * @author Voronin Leonid
 * @since 04.04.19
 **/
public interface OldCardsDAO {

    Map<String, String> getGroups();

    Speciality getSpeciality(String grpCode);

    Place getPlace(String personCode);

    School getSchool(String personCode);

    Person getPerson(String personCode);

    StudyCard getCard(String personCode);

    StudyGroup getGroup(String groupCode);

    List<Delegate> getDelegates(String personCode);

    List<String> getStudentCodes(String grpCode);
}
