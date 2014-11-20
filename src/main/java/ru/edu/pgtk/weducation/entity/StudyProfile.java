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
 * Класс для хранения профилей обучения.
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "studyprofiles")
public class StudyProfile implements Serializable {

  @Id
  @Column(name = "stp_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "stp_depcode", nullable = false)
  private Department department;

  @ManyToOne
  @JoinColumn(name = "stp_spccode", nullable = false)
  private Speciality speciality;

  @ManyToOne
  @JoinColumn(name = "stp_sfmcode", nullable = false)
  private StudyForm studyForm;

  public int getId() {
    return id;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public Speciality getSpeciality() {
    return speciality;
  }

  public void setSpeciality(Speciality speciality) {
    this.speciality = speciality;
  }

  public StudyForm getStudyForm() {
    return studyForm;
  }

  public void setStudyForm(StudyForm studyForm) {
    this.studyForm = studyForm;
  }
}
