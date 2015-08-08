package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс специальности
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "specialities")
public class Speciality implements Serializable {

  @Id
  @Column(name = "spc_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "spc_shortname", nullable = false, length = 10)
  private String shortName;

  @Column(name = "spc_fullname", nullable = false, length = 255)
  private String fullName;

  @Column(name = "spc_key", nullable = false, length = 20)
  private String key;

  @Column(name = "spc_kvalification", nullable = false, length = 128)
  private String kvalification;

  @Column(name = "spc_specialization", nullable = false, length = 128)
  private String specialization;

  @Column(name = "spc_actual", nullable = false)
  private boolean actual;

  @Column(name = "spc_aviable", nullable = false)
  private boolean aviable;

  public int getId() {
    return id;
  }

  public String getNameForList() {
    return key + " (" + shortName + ")";
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKvalification() {
    return kvalification;
  }

  public void setKvalification(String kvalification) {
    this.kvalification = kvalification;
  }

  public String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }

  public boolean isActual() {
    return actual;
  }

  public void setActual(boolean actual) {
    this.actual = actual;
  }

  public boolean isAviable() {
    return aviable;
  }

  public void setAviable(boolean aviable) {
    this.aviable = aviable;
  }
}
