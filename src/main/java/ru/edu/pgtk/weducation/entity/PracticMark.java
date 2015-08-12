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
 * Класс оценки за практику по модулям.
 * 
 * @author Воронин Леонид
 */
@Entity
@Table(name = "pmarks")
public class PracticMark implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pmk_pcode")
  private int id;
  
  @Column(name = "pmk_mark", nullable = false)
  private int mark;
  
  @Column(name = "pmk_length", nullable = false)
  private double length;
  
  @ManyToOne
  @JoinColumn(name = "pmk_prccode", nullable = false)
  private Practic practic;
  
  @ManyToOne
  @JoinColumn(name = "pmk_crdcode", nullable = false)
  private StudyCard card;

  public int getId() {
    return id;
  }

  public int getMark() {
    return mark;
  }

  public void setMark(int mark) {
    this.mark = mark;
  }

  public double getLength() {
    return length;
  }

  public void setLength(double length) {
    this.length = length;
  }

  public Practic getPractic() {
    return practic;
  }

  public void setPractic(Practic practic) {
    this.practic = practic;
  }

  public StudyCard getCard() {
    return card;
  }

  public void setCard(StudyCard card) {
    this.card = card;
  }
}
