package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
  
  @Column(name = "psn_fname", nullable = false, length = 50)
  private String firstName;
  
  @Column(name = "psn_mname", nullable = false, length = 50)
  private String middleName;
  
  @Column(name = "psn_lname", nullable = false, length = 50)
  private String lastName;
  
  @Column(name = "psn_male", nullable = false)
  private boolean male;
  
  @OneToMany(mappedBy = "person")
  private List<Delegate> delegates;

  public int getId() {
    return id;
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

  public List<Delegate> getDelegates() {
    return delegates;
  }

  public void setDelegates(List<Delegate> delegates) {
    this.delegates = delegates;
  }
}
