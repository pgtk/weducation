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
 * Класс учебной нагрузки по дисциплине.
 *
 */
@Entity
@Table(name = "load")
public class SubjectLoad implements Serializable {

  @Id
  @Column(name = "lod_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "lod_subcode", nullable = false)
  private Subject subject;

  @Transient
  private int subjectCode;

  @Column(name = "lod_course", nullable = false)
  private int course;

  @Column(name = "lod_semester", nullable = false)
  private int semester;

  @Column(name = "lod_auditory", nullable = false)
  private int auditoryLoad;

  @Column(name = "lod_maximum", nullable = false)
  private int maximumLoad;

  @ManyToOne
  @JoinColumn(name = "lod_exfcode")
  private ExamForm examForm;

  @Transient
  private int examFormCode;

  @PostLoad
  private void updateCodes() {
    if (null != examForm) {
      examFormCode = examForm.getId();
    }
    subjectCode = subject.getId();
  }

  public int getId() {
    return id;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
    if (null != subject) {
      subjectCode = subject.getId();
    } else {
      subjectCode = 0;
    }
  }

  public int getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(int subjectCode) {
    this.subjectCode = subjectCode;
  }

  public int getCourse() {
    return course;
  }

  public void setCourse(int course) {
    this.course = course;
  }

  public int getSemester() {
    return semester;
  }

  public void setSemester(int semester) {
    this.semester = semester;
  }

  public int getAuditoryLoad() {
    return auditoryLoad;
  }

  public void setAuditoryLoad(int auditoryLoad) {
    this.auditoryLoad = auditoryLoad;
  }

  public int getMaximumLoad() {
    return maximumLoad;
  }

  public void setMaximumLoad(int maximumLoad) {
    this.maximumLoad = maximumLoad;
  }

  public ExamForm getExamForm() {
    return examForm;
  }

  public void setExamForm(ExamForm examForm) {
    this.examForm = examForm;
    if (null != examForm) {
      examFormCode = examForm.getId();
    } else {
      examFormCode = 0;
    }
  }

  public int getExamFormCode() {
    return examFormCode;
  }

  public void setExamFormCode(int examFormCode) {
    this.examFormCode = examFormCode;
  }
}
