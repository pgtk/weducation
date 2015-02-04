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
 * Модуль учебного плана в который входят МДК
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "modules")
public class StudyModule implements Serializable {

  @Id
  @Column(name = "mod_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "mod_name", nullable = false, length = 255)
  private String name;

  @ManyToOne
  @JoinColumn(name = "mod_plncode", nullable = false)
  private StudyPlan plan;

  @Transient
  private int planCode;

  @ManyToOne
  @JoinColumn(name = "mod_exfcode")
  private ExamForm examForm;

  @Transient
  private int examFormCode;

  @PostLoad
  private void updateCodes() {
    planCode = plan.getId();
    if (null != examForm) {
      examFormCode = examForm.getId();
    }
  }

  public int getId() {
    return id;
  }

  public ExamForm getExamForm() {
    return examForm;
  }

  public void setExamForm(ExamForm examForm) {
    this.examForm = examForm;
    if (null != examForm) {
      examFormCode = examForm.getId();
    } else {
      examFormCode = 0;
    }
  }

  public int getExamFormCode() {
    return examFormCode;
  }

  public void setExamFormCode(int examFormCode) {
    this.examFormCode = examFormCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StudyPlan getPlan() {
    return plan;
  }

  public void setPlan(StudyPlan plan) {
    this.plan = plan;
    if (null != plan) {
      planCode = plan.getId();
    } else {
      planCode = 0;
    }
  }

  public int getPlanCode() {
    return planCode;
  }

  public void setPlanCode(int planCode) {
    this.planCode = planCode;
  }
}
