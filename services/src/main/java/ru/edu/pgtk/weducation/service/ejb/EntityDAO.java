package ru.edu.pgtk.weducation.service.ejb;

import java.util.List;

/**
 * Шаблонизированный интерфейс для сущности
 * @author Voronin Leonid
 * @since 30.03.2016
 */
interface EntityDAO<T> extends WeakEntityDAO<T> {

	/**
	 * Получает список всех сущностей из базы данных
	 * @return список экземпляров какого-либо класса
	 */
	List<T> fetchAll();
}
