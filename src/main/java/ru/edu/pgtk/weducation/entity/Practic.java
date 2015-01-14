package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Класс практики
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "practics")
public class Practic implements Serializable {

  @Id
  @Column(name = "prk_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "prk_name", nullable = false, length = 128)
  private String name;

  @Column(name = "prk_length", nullable = false)
  private float length;

  @Column(name = "prk_course", nullable = false)
  private int course;

  @Column(name = "prk_semester", nullable = false)
  private int semester;

  @ManyToOne
  @JoinColumn(name = "prk_plncode")
  private StudyPlan plan;
  
  @ManyToOne
  @JoinColumn(name = "prk_modcode")
  private StudyModule module;
  
  @Transient
  private int planCode;
  
  @Transient
  private int moduleCode;
  
  @PostLoad
  private void updateCodes() {
    planCode = plan.getId();
    if (null != module) {
      moduleCode = module.getId();
    }
  }
  
  public int getId() {
    return id;
  }

  public StudyPlan getPlan() {
    return plan;
  }

  public void setPlan(StudyPlan plan) {
    this.plan = plan;
  }

  public int getPlanCode() {
    return planCode;
  }

  public void setPlanCode(int planCode) {
    this.planCode = planCode;
  }

  public StudyModule getModule() {
    return module;
  }

  public void setModule(StudyModule module) {
    this.module = module;
  }

  public int getModuleCode() {
    return moduleCode;
  }

  public void setModuleCode(int moduleCode) {
    this.moduleCode = moduleCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getLength() {
    return length;
  }

  public void setLength(float length) {
    this.length = length;
  }

  public int getCourse() {
    return course;
  }

  public void setCourse(int course) {
    this.course = course;
  }

  public int getSemester() {
    return semester;
  }

  public void setSemester(int semester) {
    this.semester = semester;
  }
}
