package ru.edu.pgtk.weducation.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Класс для сущности "Список тестов по специальности"
 * @author Voronin Leonid
 * @since 17.05.2016
 */
@Entity
@Table(name = "testlists")
public class TestList implements Serializable {

	@Id
	@Column(name = "tsl_pcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "tsl_tstcode", nullable = false)
	private Test test;

	@ManyToOne
	@JoinColumn(name = "tsl_spccode", nullable = false)
	private Speciality speciality;

	public int getId() {
		return id;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}
}
