package ru.edu.pgtk.weducation.webui.xmlimport;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий учебный курс.
 *
 * @author Воронин Леонид
 */
public class XMLCourse {

    private int number = 0;           // номер курсе учебного плана
    private List<XMLSemester> semesters; // семестры курса

    public XMLCourse(int number) {
        super();
        this.number = number;
        semesters = new ArrayList<>();
    }

    public XMLCourse() {
        super();
        semesters = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<XMLSemester> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<XMLSemester> semesters) {
        this.semesters = semesters;
    }

    public void addSemester(XMLSemester semester) {
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
