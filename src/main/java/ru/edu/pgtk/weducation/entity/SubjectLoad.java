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
 * Класс учебной нагрузки по дисциплине.
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "load")
public class SubjectLoad implements Serializable {

  @Id
  @Column(name = "lod_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @ManyToOne
  @JoinColumn(name = "lod_subcode", nullable = false)
  private Subject subject;
  
  @Column(name = "lod_course", nullable = false)
  private int course;
  
  @Column(name = "lod_semester", nullable = false)
  private int semester;
  
  @Column(name = "lod_auditory", nullable = false)
  private int auditoryLoad;
  
  @Column(name = "lod_maximum", nullable = false)
  private int maximumLoad;
  
    
}
