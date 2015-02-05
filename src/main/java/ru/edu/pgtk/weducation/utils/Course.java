package ru.edu.pgtk.weducation.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий учебный курс.
 *
 * @author Воронин Леонид
 */
public class Course {

  private int number = 0;           // номер курсе учебного плана
  private List<Semester> semesters; // семестры курса

  public Course(int number) {
    super();
    this.number = number;
    semesters = new ArrayList<>();
  }
  
  public Course() {
    super();
    semesters = new ArrayList<>();
  }
  
  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public List<Semester> getSemesters() {
    return semesters;
  }

  public void setSemesters(List<Semester> semesters) {
    this.semesters = semesters;
  }
  
  public void addSemester(Semester semester) {
    semesters.add(semester);
  }
  
  public int getSemestersCount() {
    return semesters.size();
  }
  
  @Override
  public String toString() {
    return "Course [number=" + number + ", semesters=" + semesters + "]";
  }
}
