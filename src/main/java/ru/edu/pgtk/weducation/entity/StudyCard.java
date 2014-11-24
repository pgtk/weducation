package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Класс учебной карточки.
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "cards")
public class StudyCard implements Serializable {

  @Id
  @Column(name = "crd_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "crd_bdate", nullable = false)
  private Date beginDate;

  @Column(name = "crd_edate")
  private Date endDate;
  
  @Column(name = "crd_docdate", nullable = false)
  private Date documentDate;
  
  @Column(name = "crd_comissiondate")
  private Date comissionDate;
  
  @Column(name = "crd_diplomedate")
  private Date diplomeDate;

  @ManyToOne
  @JoinColumn(name = "crd_psncode")
  private Person person;

  @ManyToOne
  @JoinColumn(name = "crd_spccode")
  private Speciality speciality;

  @ManyToOne
  @JoinColumn(name = "crd_grpcode")
  private StudyGroup group;
}
