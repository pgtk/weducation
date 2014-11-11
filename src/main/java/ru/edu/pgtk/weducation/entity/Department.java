package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс отделения
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "departments")
public class Department implements Serializable {

  @Id
  @Column(name = "dep_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "dep_name", nullable = false, length = 128)
  private String name;

  @Column(name = "dep_master", nullable = false, length = 128)
  private String master;

  @Column(name = "dep_secretar", nullable = false, length = 128)
  private String secretar;

  // TODO возможно, стоит добавить, является ли отделение заочным?
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

  public String getSecretar() {
    return secretar;
  }

  public void setSecretar(String secretar) {
    this.secretar = secretar;
  }
}
