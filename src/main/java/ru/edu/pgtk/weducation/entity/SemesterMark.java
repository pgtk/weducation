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

/**
 * Класс оценок за семестр
 */
@Entity
@Table(name = "semestermarks")
public class SemesterMark implements Serializable {

  @Id
  @Column(name = "smk_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "smk_semester", nullable = false)
  private int semester;

  @Column(name = "smk_course", nullable = false)
  private int course;

  @Column(name = "smk_mark", nullable = false)
  private int mark;

  @ManyToOne
  @JoinColumn(name = "smk_psncode", nullable = false)
  private Person person;

  @Transient
  private int personCode;

  @ManyToOne
  @JoinColumn(name = "smk_crdcode", nullable = false)
  private StudyCard card;

  @Transient
  private int cardCode;

  @ManyToOne
  @JoinColumn(name = "smk_subcode", nullable = false)
  private Subject subject;

  @Transient
  private int subjectCode;

  @PostLoad
  private void updateCodes() {
    personCode = person.getId();
    cardCode = card.getId();
    subjectCode = subject.getId();
  }

  public int getId() {
    return id;
  }

  public int getSemester() {
    return semester;
  }

  public void setSemester(int semester) {
    this.semester = semester;
  }

  public int getCourse() {
    return course;
  }

  public void setCourse(int course) {
    this.course = course;
  }

  public int getMark() {
    return mark;
  }

  public void setMark(int mark) {
    this.mark = mark;
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

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public int getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(int subjectCode) {
    this.subjectCode = subjectCode;
  }
}
