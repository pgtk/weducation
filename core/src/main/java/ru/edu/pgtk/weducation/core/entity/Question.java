package ru.edu.pgtk.weducation.core.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Класс для хранения сущности "Вопрос"
 * Created by leonid on 17.05.16.
 */
@Entity
@Table(name = "questions")
public class Question implements Serializable {

    @Id
    @Column(name = "qst_pcode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "qst_text", nullable = false, length = 65535)
    private String text;

    @ManyToOne
    @JoinColumn(name = "qst_tstcode", nullable = false)
    private Test test;

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
}
