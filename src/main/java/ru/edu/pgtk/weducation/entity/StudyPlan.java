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

@Entity
@Table(name = "plans")
public class StudyPlan implements Serializable {

  @Id
  @Column(name = "pln_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "pln_name", nullable = false, length = 255)
  private String name;

  @Column(name = "pln_description", length = 255)
  private String description;
  
  @Column(name = "pln_extramural", nullable = false)
  private boolean extramural;

  @ManyToOne
  @JoinColumn(name = "pln_spccode", nullable = false)
  private Speciality speciality;
  
  @Transient
  private int specialityCode;

  @PostLoad
  private void updateCodes() {
    specialityCode = speciality.getId();
  }
  
  public int getId() {
    return id;
  }
  
  public String getExtramural() {
    return (extramural)? "заочная форма" : "очная форма";
  }
  
  public String getNameForList() {
    return name + " (" + getExtramural() + ")";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isExtramural() {
    return extramural;
  }

  public void setExtramural(boolean extramural) {
    this.extramural = extramural;
  }

  public Speciality getSpeciality() {
    return speciality;
  }

  public void setSpeciality(Speciality speciality) {
    this.speciality = speciality;
  }

  public int getSpecialityCode() {
    return specialityCode;
  }

  public void setSpecialityCode(int specialityCode) {
    this.specialityCode = specialityCode;
  }
}
