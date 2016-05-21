package ru.edu.pgtk.weducation.core.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Класс для сущности "Вариант ответа"
 * Created by admin on 17.05.2016.
 */
@Entity
@Table(name = "answers")
public class Answer implements Serializable {

    @Id
    @Column(name = "ans_pcode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ans_text", nullable = false)
    private String text;

    @Column(name = "ans_right", nullable = false)
    private boolean right;

    @ManyToOne
    @JoinColumn(name = "ans_qstcode", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "ans_tstcode", nullable = false)
    private Test test;

    @Transient
    private boolean selected;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
