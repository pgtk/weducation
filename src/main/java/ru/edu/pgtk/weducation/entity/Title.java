package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс для хранения наименования практики, дисциплины, модуля и т.п.
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "titles")
public class Title implements Serializable {

  @Id
  @Column(name = "tit_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "tit_fullname", nullable = false, length = 255)
  private String fullName;

  @Column(name = "tit_shortname", nullable = false, length = 30)
  private String shortName;

// TODO Возможно, стоит добавить какое-нибудь целочисленное значение, 
// по которому будет понятно, практика это или дисциплина
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
}
