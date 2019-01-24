package ru.edu.pgtk.weducation.webui.xmlimport;

import ru.edu.pgtk.weducation.core.entity.ExamForm;

/**
 * Класс для реализации семестровой нагрузки по практике.
 *
 * @author Воронин Леонид
 */
public class XMLPracticeLoad {

    private int semester = 0; // Номер семестра от 1 до 8 для техникума
    private int hours = 0;    // Нагрузка в часах
    private int weeks = 0;    // Продолжительность (недель)
    private ExamForm examType = ExamForm.OTHER; // Тип экзамена. пока пусть будет неизвестный, потом разберемся.
    private XMLPractice practice = null; // Практика, для которой задается эта нагрузка.

    /**
     * Конструктор без параметров.
     */
    public XMLPracticeLoad() {
        super();
    }

    /**
     * Конструктор с параметрами.
     *
     * @param practice практика для которой создается нагрузка.
     * @param semester номер семестра (от 1 до 8 для техникума)
     * @param hours    количество часов нагрузки
     * @param weeks    продолжительность практики в неделях
     */
    public XMLPracticeLoad(XMLPractice practice, int semester, int hours, int weeks) {
        super();
        this.practice = practice;
        this.semester = semester;
        this.hours = hours;
        this.weeks = weeks;
    }

    @Override
    public String toString() {
        return "PracticeLoad [semester=" + semester + ", hours=" + hours +
                ", weeks=" + weeks + ", examType=" + examType + "]";
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public ExamForm getExamType() {
        return examType;
    }

    public void setExamType(ExamForm examType) {
        this.examType = examType;
    }

    public XMLPractice getPractice() {
        return practice;
    }

    public void setPractice(XMLPractice practice) {
        this.practice = practice;
    }
}
