package ru.edu.pgtk.weducation.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
@Table(name = "cards")
public class StudyCard implements Serializable {

  @Id
  @Column(name = "crd_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "crd_bdate", nullable = false)
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date beginDate;

  @Column(name = "crd_edate")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date endDate;

  @Column(name = "crd_docdate", nullable = false)
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date documentDate;

  @Column(name = "crd_docname", nullable = false)
  private String documentName;

  @Column(name = "crd_docorganization", nullable = false)
  private String documentOrganization;

  @Column(name = "crd_comissiondate")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date comissionDate;

  @Column(name = "crd_comissiondirector")
  private String comissionDirector;

  @Column(name = "crd_diplomelength")
  private float diplomeLength;

  @Column(name = "crd_diplometheme")
  private String diplomeTheme;

  @Column(name = "crd_diplomenumber")
  private String diplomeNumber;

  @Column(name = "crd_appendixnumber")
  private String appendixNumber;

  @Column(name = "crd_regnumber")
  private String registrationNumber;

  @Column(name = "crd_diplomedate")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date diplomeDate;

  @Column(name = "crd_red", nullable = false)
  private boolean red;

  @Column(name = "crd_gosexam", nullable = false)
  private boolean gosExam;

  @Column(name = "crd_remanded", nullable = false)
  private boolean remanded;

  @Column(name = "crd_remandreason")
  private String remandReason;

  @Column(name = "crd_remandcommand")
  private String remandCommand;

  @ManyToOne
  @JoinColumn(name = "crd_sclcode", nullable = false)
  private School school;

  @Transient
  private int schoolCode;

  @ManyToOne
  @JoinColumn(name = "crd_psncode", nullable = false)
  private Person person;

  @Transient
  private int personCode;

  @ManyToOne
  @JoinColumn(name = "crd_spccode", nullable = false)
  private Speciality speciality;

  @Transient
  private int specialityCode;

  @ManyToOne
  @JoinColumn(name = "crd_grpcode")
  private StudyGroup group;

  @Transient
  private int groupCode;

  @Column(name = "crd_active", nullable = false)
  private boolean active;
  
  @Column(name = "crd_extramural", nullable = false)
  private boolean extramural;

  @PostLoad
  private void updateCodes() {
    schoolCode = school.getId();
    personCode = person.getId();
    if (null != group) {
      groupCode = group.getId();
    }
    specialityCode = speciality.getId();
  }

  public int getId() {
    return id;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getDocumentDate() {
    return documentDate;
  }

  public void setDocumentDate(Date documentDate) {
    this.documentDate = documentDate;
  }

  public String getDocumentName() {
    return documentName;
  }

  public void setDocumentName(String documentName) {
    this.documentName = documentName;
  }

  public String getDocumentOrganization() {
    return documentOrganization;
  }

  public void setDocumentOrganization(String documentOrganization) {
    this.documentOrganization = documentOrganization;
  }

  public Date getComissionDate() {
    return comissionDate;
  }

  public void setComissionDate(Date comissionDate) {
    this.comissionDate = comissionDate;
  }

  public String getComissionDirector() {
    return comissionDirector;
  }

  public void setComissionDirector(String comissionDirector) {
    this.comissionDirector = comissionDirector;
  }

  public float getDiplomeLength() {
    return diplomeLength;
  }

  public void setDiplomeLength(float diplomeLength) {
    this.diplomeLength = diplomeLength;
  }

  public String getDiplomeTheme() {
    return diplomeTheme;
  }

  public void setDiplomeTheme(String diplomeTheme) {
    this.diplomeTheme = diplomeTheme;
  }

  public String getDiplomeNumber() {
    return diplomeNumber;
  }

  public void setDiplomeNumber(String diplomeNumber) {
    this.diplomeNumber = diplomeNumber;
  }

  public String getAppendixNumber() {
    return appendixNumber;
  }

  public void setAppendixNumber(String appendixNumber) {
    this.appendixNumber = appendixNumber;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public Date getDiplomeDate() {
    return diplomeDate;
  }

  public void setDiplomeDate(Date diplomeDate) {
    this.diplomeDate = diplomeDate;
  }

  public boolean isRed() {
    return red;
  }

  public void setRed(boolean red) {
    this.red = red;
  }

  public boolean isGosExam() {
    return gosExam;
  }

  public void setGosExam(boolean gosExam) {
    this.gosExam = gosExam;
  }

  public boolean isRemanded() {
    return remanded;
  }

  public void setRemanded(boolean remanded) {
    this.remanded = remanded;
  }

  public String getRemandReason() {
    return remandReason;
  }

  public void setRemandReason(String remandReason) {
    this.remandReason = remandReason;
  }

  public String getRemandCommand() {
    return remandCommand;
  }

  public void setRemandCommand(String remandCommand) {
    this.remandCommand = remandCommand;
  }

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }

  public int getSchoolCode() {
    return schoolCode;
  }

  public void setSchoolCode(int schoolCode) {
    this.schoolCode = schoolCode;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Speciality getSpeciality() {
    return speciality;
  }

  public void setSpeciality(Speciality speciality) {
    this.speciality = speciality;
  }

  public StudyGroup getGroup() {
    return group;
  }

  public void setGroup(StudyGroup group) {
    this.group = group;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public int getPersonCode() {
    return personCode;
  }

  public void setPersonCode(int personCode) {
    this.personCode = personCode;
  }

  public int getSpecialityCode() {
    return specialityCode;
  }

  public void setSpecialityCode(int specialityCode) {
    this.specialityCode = specialityCode;
  }

  public int getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(int groupCode) {
    this.groupCode = groupCode;
  }

  public boolean isExtramural() {
    return extramural;
  }

  public void setExtramural(boolean extramural) {
    this.extramural = extramural;
  }
}
