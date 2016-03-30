package ru.edu.pgtk.weducation.ejb;

import java.util.List;

/**
 * Шаблонизированный интерфейс для сущности
 * @author Voronin Leonid
 * @since 30.03.2016
 */
public interface EntityDAO<T> extends WeakEntityDAO<T> {

	/**
	 * Получает список всех сущностей из базы данных
	 * @return список экземпляров какого-либо класса
	 */
	List<T> fetchAll();
}
