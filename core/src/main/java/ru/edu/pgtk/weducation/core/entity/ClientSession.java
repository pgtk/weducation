package ru.edu.pgtk.weducation.core.entity;

import ru.edu.pgtk.weducation.core.utils.Utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clientsessions")
public class ClientSession implements Serializable {

  @Id
  @Column(name = "cls_pcode")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "cls_ipaddr", nullable = false, length = 128)
  private String hostAddress;

  @Column(name = "cls_created", nullable = false)
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date creationTime;

  @ManyToOne
  @JoinColumn(name = "cls_acocode")
  private Account account;

  public String getCreationString() {
    return Utils.formatDate(creationTime);
  }

  public int getId() {
    return id;
  }

  public String getHostAddress() {
    return hostAddress;
  }

  public void setHostAddress(String hostAddress) {
    this.hostAddress = hostAddress;
  }

  public Date getCraetionTime() {
    return creationTime;
  }

  public void setCraetionTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }
}
