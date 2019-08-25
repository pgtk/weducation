package ru.edu.pgtk.weducation.core.entity;

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
import java.io.Serializable;

@Entity
@Table(name = "gosexams")
public class GOSExam implements Serializable {

    @Id
    @Column(name = "gex_pcode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "gex_plncode", nullable = false)
    private StudyPlan plan;

    @ManyToOne
    @JoinColumn(name = "gex_subcode", nullable = false)
    private Subject subject;

    @Transient
    private int subjectCode;

    @PostLoad
    private void updateCodes() {
        if (subject != null) {
            subjectCode = subject.getId();
        }
    }

    public int getId() {
        return id;
    }

    public StudyPlan getPlan() {
        return plan;
    }

    public void setPlan(StudyPlan plan) {
        this.plan = plan;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        updateCodes();
    }

    public int getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(int subjectCode) {
        this.subjectCode = subjectCode;
    }
}
