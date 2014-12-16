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
@Table(name = "weekmissings")
public class WeekMissing implements Serializable {

  @Id
  @Column(name = "wms_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "wms_psncode", nullable = false)
  private Person person;

  @Transient
  private int personCode;

  @Column(name = "wms_month", nullable = false)
  private int month;

  @Column(name = "wms_year", nullable = false)
  private int year;

  @Column(name = "wms_week", nullable = false)
  private int week;

  @Column(name = "wms_legal", nullable = false)
  private int legal;

  @Column(name = "wms_illegal", nullable = false)
  private int illegal;

  @ManyToOne
  @JoinColumn(name = "wms_crdcode", nullable = false)
  private StudyCard card;

  @Transient
  private int cardCode;

  @PostLoad
  private void updateCodes() {
    personCode = person.getId();
    cardCode = card.getId();
  }

  public int getId() {
    return id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public int getPersonCode() {
    return personCode;
  }

  public void setPersonCode(int personCode) {
    this.personCode = personCode;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getWeek() {
    return week;
  }

  public void setWeek(int week) {
    this.week = week;
  }

  public int getLegal() {
    return legal;
  }

  public void setLegal(int legal) {
    this.legal = legal;
  }

  public int getIllegal() {
    return illegal;
  }

  public void setIllegal(int illegal) {
    this.illegal = illegal;
  }

  public StudyCard getCard() {
    return card;
  }

  public void setCard(StudyCard card) {
    this.card = card;
  }

  public int getCardCode() {
    return cardCode;
  }

  public void setCardCode(int cardCode) {
    this.cardCode = cardCode;
  }
}
