package ru.edu.pgtk.weducation.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * Класс для хранения сущности "Вопрос"
 * Created by leonid on 17.05.16.
 */
@Entity
@Table(name = "questions")
public class Question implements Serializable, Comparable<Question> {

    @Id
    @Column(name = "qst_pcode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "qst_text", nullable = false, length = 65535)
    private String text;

    @ManyToOne
    @JoinColumn(name = "qst_tstcode", nullable = false)
    private Test test;

    @Transient
    private int number;

    @PostLoad
    private void countNumber() {
        Random rnd = new Random((new Date()).getTime());
        number = rnd.nextInt();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(Question o) {
        if (o == null) {
            return 1;
        }
        return number - o.getNumber();
    }
}
