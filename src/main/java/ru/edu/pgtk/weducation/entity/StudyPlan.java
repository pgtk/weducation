package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import ru.edu.pgtk.weducation.utils.Utils;

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
  
  @Column(name = "pln_years", nullable = false)
  private int years;
  
  @Column(name = "pln_months", nullable = false)
  private int months;
  
  @Column(name = "pln_date")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date date;

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
  
  public String getLength() {
    return Utils.getYearString(years) + " " + Utils.getMonthString(months);
  }

  public String getExtramural() {
    return ((extramural) ? "заочная" : "очная") + " форма";
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

  public int getYears() {
    return years;
  }

  public void setYears(int years) {
    this.years = years;
  }

  public int getMonths() {
    return months;
  }

  public void setMonths(int months) {
    this.months = months;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
