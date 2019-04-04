package ru.edu.pgtk.weducation.core.importcards;

/**
 * Интерфейс для класса импорта карточек студентов
 *
 * @author Voronin Leonid
 * @since 04.04.19
 **/
public interface ImportCardDAO {

    void importGroup(String groupCode);

    @Deprecated
    void importAll();
}
