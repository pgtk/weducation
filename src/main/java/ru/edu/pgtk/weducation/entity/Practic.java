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
  @JoinColumn(name = "prk_modcode")
  private StudyModule module;

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

  public StudyModule getModule() {
    return module;
  }

  public void setModule(StudyModule module) {
    this.module = module;
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
