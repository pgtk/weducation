package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Класс для хранения информации о персоне.
 *
 * @author Воронин Леонид
 */
@Entity
@Table(name = "persons")
public class Person implements Serializable {

  @Id
  @Column(name = "psn_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @Column(name = "psn_firstname", nullable = false, length = 50)
  private String firstName;
  
  @Column(name = "psn_middlename", nullable = false, length = 50)
  private String middleName;
  
  @Column(name = "psn_lastname", nullable = false, length = 50)
  private String lastName;
  
  @Column(name = "psn_male", nullable = false)
  private boolean male;
  
  @Column(name = "psn_foreign", nullable = false)
  private boolean foreign;
  
  @Column(name = "psn_birthdate", nullable = false)
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date birthDate;
  
  @Column(name = "psn_birthplace", nullable = false, length = 255)
  private String birthPlace;
  
  @OneToMany(mappedBy = "person")
  private List<Delegate> delegates;

  public int getId() {
    return id;
  }
  
  public String getFullName() {
    return firstName + " " + middleName + " " + lastName;
  }
  
  public String getBirthDateString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(birthDate);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isMale() {
    return male;
  }

  public void setMale(boolean male) {
    this.male = male;
  }

  public boolean isForeign() {
    return foreign;
  }

  public void setForeign(boolean foreign) {
    this.foreign = foreign;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getBirthPlace() {
    return birthPlace;
  }

  public void setBirthPlace(String birthPlace) {
    this.birthPlace = birthPlace;
  }

  public List<Delegate> getDelegates() {
    return delegates;
  }

  public void setDelegates(List<Delegate> delegates) {
    this.delegates = delegates;
  }
}
