package ru.edu.pgtk.weducation.utils;

/**
 * Класс для инкапсуляции информации о семестровой нагрузке по дисциплине.
 * 
 * Module.java - информация о модуле или блоке дисциплин.
 * Subject.java - информация о дисциплине (в одном блоке несколько дисциплин).
 * Этот класс будет зранить количество часов аудиторной, максимальной и теоретической нагрузки
 * по дисциплине за какой-либо семестр.
 * 
 * @author Воронин Леонид
 */
public class SubjectLoad {
  
  private int semester = 0; // Номер семестра от 1 до 8 для техникума
  private int maxLoad = 0;  // Максимальная нагрузка по дисциплине
  private int audLoad = 0;  // Аудиторная нагрузка (максимальная - самостоятельная)
  private int thrLoad = 0;  // Теоретическая нагрузка (аудиторная - практика)
  private int cprLoad = 0;  // Курсовое проектирование
  private ExamType examType = ExamType.UNKN; // Тип экзамена. пока пусть будет неизвестный, потом разберемся.
  private Subject subject = null; // Тут будет дисциплина.
  
  /*
   * Конструктор без параметров
   */
  public SubjectLoad() {
    super();
  }
  
  /**
   * Конструктор класса с параметрами
   * @param subj дисциплина, нагрузка которой задается
   * @param num номер семестра (от 1 до 8 для техникума)
   * @param max максимальная нагрузка
   * @param aud аудиторная нагрузка (максимальная минус самостоятельной)
   * @param thr количество часов теории (аудиторная минус практика и курсовые)
   * @param cpr курсовое проектирование
   */
  public SubjectLoad(Subject subj, int num, int max, int aud, int thr, int cpr) {
    super();
    subject = subj;
    semester = num;
    maxLoad = max;
    audLoad = aud;
    thrLoad = thr;
    cprLoad = cpr;    
  }
  
  @Override
  public String toString() {
    return "SubjectLoad [semester=" + semester + ", maxLoad=" + maxLoad +
            ", audLoad=" + audLoad + ", thrLoad=" + thrLoad + ", cprLoad=" + cprLoad + ", examType=" + examType + "]";
  }

  public int getCprLoad() {
    return cprLoad;
  }

  public void setCprLoad(int cprLoad) {
    this.cprLoad = cprLoad;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }
  
  public int getSemester() {
    return semester;
  }

  public void setSemester(int semester) {
    this.semester = semester;
  }

  public int getMaxLoad() {
    return maxLoad;
  }

  public void setMaxLoad(int maxLoad) {
    this.maxLoad = maxLoad;
  }

  public int getAudLoad() {
    return audLoad;
  }

  public void setAudLoad(int audLoad) {
    this.audLoad = audLoad;
  }

  public int getThrLoad() {
    return thrLoad;
  }

  public void setThrLoad(int thrLoad) {
    this.thrLoad = thrLoad;
  }

  public ExamType getExamType() {
    return examType;
  }

  public void setExamType(ExamType examType) {
    this.examType = examType;
  }
}
