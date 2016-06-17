package ru.edu.pgtk.weducation.core.reports.entity;

import ru.edu.pgtk.weducation.core.entity.Speciality;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс для представления информации о выданном бланке диплома и приложения к нему
 * Created by leonid on 17.06.16.
 */
@Entity
@Table(name = "diplomeblanks")
public class DiplomeBlank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dbk_pcode")
    private int id;

    @Column(name = "dbk_fullname")
    private String fullName;

    @Column(name = "dbk_red")
    private boolean red;

    @Column(name = "dbk_extramural")
    private boolean extramural;

    @Column(name = "dbk_duplicate")
    private boolean duplicate;

    @Column(name = "dbk_diplomenumber")
    private String diplomeNumber;

    @Column(name = "dbk_appendixnumber")
    private String appendixNumber;

    @Column(name = "dbk_regnumber")
    private String registrationNumber;

    @Column(name = "dbk_diplomedate")
    @Temporal(TemporalType.DATE)
    private Date diplomeDate;

    @Column(name = "dbk_edate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "dbk_spccode")
    private Speciality speciality;

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isRed() {
        return red;
    }

    public boolean isExtramural() {
        return extramural;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public String getDiplomeNumber() {
        return diplomeNumber;
    }

    public String getAppendixNumber() {
        return appendixNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Date getDiplomeDate() {
        return diplomeDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Speciality getSpeciality() {
        return speciality;
    }
}
