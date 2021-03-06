package ru.edu.pgtk.weducation.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс для хранения сущности "Сеанс тестирования"
 * Created by admin on 17.05.2016.
 */
@Entity
@Table(name = "testsessions")
public class TestSession implements Serializable {

    @Id
    @Column(name = "tss_pcode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tss_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "tss_mark", nullable = false)
    private int mark;

    @Column(name = "tss_questions", nullable = false)
    private int questions;

    @Column(name = "tss_right", nullable = false)
    private int rightAnswers;

    @ManyToOne
    @JoinColumn(name = "tss_psncode", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "tss_tstcode", nullable = false)
    private Test test;

    private String stringify(int value) {
        return value >= 0 ? String.valueOf(value) : "неизвестно";
    }

    public String getDateOfTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return timestamp == null ? "Неизвестно" : sdf.format(timestamp);
    }

    public String getRightAnswered() {
        return stringify(rightAnswers);
    }

    public String getWrongAnswered() {
        return stringify(questions - rightAnswers);
    }

    public String getTotalAnswered() {
        return stringify(questions);
    }

    public String getMarkForTest() {
        switch (mark) {
            case 0:
            case 1:
            case 2:
                return "Неудовлетворительно";
            case 3:
                return "Удовлетворительно";
            case 4:
                return "Хорошо";
            case 5:
                return "Отлично";
            default:
                return "Неизвестно";
        }
    }

    public int getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(int rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
