package ru.edu.pgtk.weducation.ejb;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Абстрактный класс для всех EJB компонентов
 * @author Voronin Leonid
 * @since 30.03.2016
 */
public abstract class AbstractEJB {

	@PersistenceContext
	protected EntityManager em;
}
