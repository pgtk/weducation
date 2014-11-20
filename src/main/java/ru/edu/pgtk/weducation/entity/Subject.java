package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Класс дисциплины или междисциплинарного курса
 *
 * @author Воронин Леонид.
 */
@Entity
@Table(name = "subjects")
public class Subject implements Serializable {

  @Id
  @Column(name = "sub_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  // Может сделать ссылку на наименование дисциплины?
  @Column(name = "sub_fullname")
  private String fullName;
  
  @Column(name = "sub_shortname")
  private String shortName;
  
  @ManyToOne
  @JoinColumn(name = "sub_plncode", nullable = false)
  private StudyPlan plan;
}
