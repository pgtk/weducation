package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

  @Column(name = "stp_depcode")
  private int departmentCode;

  @Column(name = "stp_spccode")
  private int specialityCode;

  @Column(name = "stp_sfmcode")
  private int studyFormCode;

  public int getId() {
    return id;
  }

  public int getDepartmentCode() {
    return departmentCode;
  }

  public void setDepartmentCode(int departmentCode) {
    this.departmentCode = departmentCode;
  }

  public int getSpecialityCode() {
    return specialityCode;
  }

  public void setSpecialityCode(int specialityCode) {
    this.specialityCode = specialityCode;
  }

  public int getStudyFormCode() {
    return studyFormCode;
  }

  public void setStudyFormCode(int studyFormCode) {
    this.studyFormCode = studyFormCode;
  }
}
