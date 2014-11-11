package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс для формы обучения (очная, заочная и т.п.)
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "studyforms")
public class StudyForm implements Serializable {

  @Id
  @Column(name = "sfm_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "sfm_name")
  private String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
