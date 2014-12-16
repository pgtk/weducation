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
import javax.persistence.Transient;

/**
 * Класс учебной группы
 *
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

  @Column(name = "grp_commercial", nullable = false)
  private boolean commercial;
  
  @Column(name = "grp_extramural", nullable = false)
  private boolean extramural;

  @ManyToOne
  @JoinColumn(name = "grp_depcode", nullable = false)
  private Department department;
  
  @Transient
  private int departmentCode;

  @ManyToOne
  @JoinColumn(name = "grp_spccode", nullable = false)
  private Speciality speciality;
  
  @Transient
  private int specialityCode;

  @ManyToOne
  @JoinColumn(name = "grp_plncode", nullable = false)
  private StudyPlan plan;

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

  public boolean isCommercial() {
    return commercial;
  }

  public void setCommercial(boolean commercial) {
    this.commercial = commercial;
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

  public StudyPlan getPlan() {
    return plan;
  }

  public void setPlan(StudyPlan plan) {
    this.plan = plan;
  }

  public boolean isExtramural() {
    return extramural;
  }

  public void setExtramural(boolean extramural) {
    this.extramural = extramural;
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
}
