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
import static ru.edu.pgtk.weducation.utils.Utils.getStringForMonth;

/**
 * Класс для хранения информации о семестре какой-либо группы.
 * @author Воронин Леонид
 */
@Entity
@Table(name = "groupsemesters")
public class GroupSemester implements Serializable {
  
  @Id
  @Column(name = "grp_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @Column(name = "grs_course")
  private int course;
  
  @Column(name = "grs_semester")
  private int semester;
  
  @Column(name = "grs_beginweek")
  private int beginWeek;
  
  @Column(name = "grs_beginmonth")
  private int beginMonth;
  
  @Column(name = "grs_beginyear")
  private int beginYear;
  
  @Column(name = "grs_endweek")
  private int endWeek;
  
  @Column(name = "grs_endmonth")
  private int endMonth;
  
  @Column(name = "grs_endyear")
  private int endYear;
  
  @ManyToOne
  @JoinColumn(name = "grs_grpcode")
  private StudyGroup group;
  
  @Transient
  private int beginDate;
  
  @Transient
  private int endDate;
  
  @PostLoad
  private void updateDates() {
    beginDate = beginYear * 1000 + beginMonth * 10 + beginWeek;
    endDate = endYear * 1000 + endMonth * 10 + endWeek;
  }
  
  public String getBeginDateString() {
    return beginYear + "-й год, " + getStringForMonth(beginMonth-1) + ", " + beginWeek + "-я неделя.";
  }
  
  public String getEndDateString() {
    return endYear + "-й год, " + getStringForMonth(endMonth-1) + ", " + endWeek + "-я неделя.";
  }

  public int getId() {
    return id;
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

  public int getBeginWeek() {
    return beginWeek;
  }

  public void setBeginWeek(int beginWeek) {
    this.beginWeek = beginWeek;
    updateDates();
  }

  public int getBeginMonth() {
    return beginMonth;
  }

  public void setBeginMonth(int beginMonth) {
    this.beginMonth = beginMonth;
    updateDates();
  }

  public int getBeginYear() {
    return beginYear;
  }

  public void setBeginYear(int beginYear) {
    this.beginYear = beginYear;
    updateDates();
  }

  public int getEndWeek() {
    return endWeek;
  }

  public void setEndWeek(int endWeek) {
    this.endWeek = endWeek;
    updateDates();
  }

  public int getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(int endMonth) {
    this.endMonth = endMonth;
    updateDates();
  }

  public int getEndYear() {
    return endYear;
  }

  public void setEndYear(int endYear) {
    this.endYear = endYear;
    updateDates();
  }

  public int getBeginDate() {
    return beginDate;
  }

  public int getEndDate() {
    return endDate;
  }

  public StudyGroup getGroup() {
    return group;
  }

  public void setGroup(StudyGroup group) {
    this.group = group;
  }
}
