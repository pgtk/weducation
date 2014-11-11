package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс учебного заведения.
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "schools")
public class School implements Serializable {

  @Id
  @Column(name = "scl_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "scl_fullname", length = 255, nullable = false)
  private String fullName;

  @Column(name = "scl_shortname", length = 128, nullable = false)
  private String shortName;

  @Column(name = "scl_director", length = 128, nullable = false)
  private String director;

  @Column(name = "scl_current", nullable = false)
  private boolean current;

  public int getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public boolean isCurrent() {
    return current;
  }

  public void setCurrent(boolean current) {
    this.current = current;
  }
}
