package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс учебной группы
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "groups")
public class StudyGroup implements Serializable {

  @Id
  @Column(name = "grp_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "grp_name", nullable = false, length = 20)
  private String name;

  @Column(name = "grp_master", length = 128)
  private String master;

  @Column(name = "grp_curse", nullable = false)
  private int course;

  @Column(name = "grp_year", nullable = false)
  private int year;

  @Column(name = "grp_depcode", nullable = false)
  private int departmentCode;

  @Column(name = "grp_spccode", nullable = false)
  private int specialityCode;

  @Column(name = "grp_sfmcode", nullable = false)
  private int studyFormCode;
  
  @Column(name = "grp_commercial", nullable = false)
  private boolean commercial;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMaster() {
    return master;
  }

  public void setMaster(String master) {
    this.master = master;
  }

  public int getCourse() {
    return course;
  }

  public void setCourse(int course) {
    this.course = course;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
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

  public boolean isCommercial() {
    return commercial;
  }

  public void setCommercial(boolean commercial) {
    this.commercial = commercial;
  }
}
