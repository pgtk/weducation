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
 * Класс для итоговой практики, которая пойдет в выписку.
 * @author Воронин Леонид
 */
@Entity
@Table(name = "finalpractics")
public class FinalPractic implements Serializable {
  
  @Id
  @Column(name = "fpc_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @Column(name = "fpc_name", nullable = false, length = 255)
  private String name;
  
  @Column(name = "fpc_length", nullable = false)
  private float length;
  
  @Column(name = "fpc_number", nullable = false)
  private int number;
  
  @ManyToOne
  @JoinColumn(name = "fpc_plncode", nullable = false)
  private StudyPlan plan;
  
  @Transient
  private int planCode;
  
  @PostLoad
  private void updateCode() {
    if (null != plan) {
      planCode = plan.getId();
    }
  }

  public int getId() {
    return id;
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

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public StudyPlan getPlan() {
    return plan;
  }

  public void setPlan(StudyPlan plan) {
    this.plan = plan;
    updateCode();
  }

  public int getPlanCode() {
    return planCode;
  }

  public void setPlanCode(int planCode) {
    this.planCode = planCode;
  }
  
  
}
