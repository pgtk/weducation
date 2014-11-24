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

  public int getId() {
    return id;
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
  }
}
