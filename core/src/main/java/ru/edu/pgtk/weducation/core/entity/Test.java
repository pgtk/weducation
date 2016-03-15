package ru.edu.pgtk.weducation.core.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Класс для сущности "Тесты"
 *
 * @author Voronin Leonid
 */
@Entity
@Table(name = "tests")
public class Test implements Serializable {

    @Id
    @Column(name = "tst_pcode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tst_title", nullable = false)
    private String title;

    @Column(name = "tst_author", nullable = false)
    private String author;

    @Column(name = "tst_questions", nullable = false)
    private int questionsForTest;

    @Column(name = "tst_timelimit", nullable = false)
    private int timeForTest;

    @Column(name = "tst_maxtries", nullable = false)
    private int maxTries;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuestionsForTest() {
        return questionsForTest;
    }

    public void setQuestionsForTest(int questionsForTest) {
        this.questionsForTest = questionsForTest;
    }

    public int getTimeForTest() {
        return timeForTest;
    }

    public void setTimeForTest(int timeForTest) {
        this.timeForTest = timeForTest;
    }

    public int getMaxTries() {
        return maxTries;
    }

    public void setMaxTries(int maxTries) {
        this.maxTries = maxTries;
    }
}
