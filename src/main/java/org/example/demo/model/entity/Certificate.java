package org.example.demo.model.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "certificates")
public class Certificate extends BaseEntity {

  @Column(nullable = false, length = 200)
  private String name;

  @Column(length = 160)
  private String org;

  @Column(length = 160)
  private String hash;

  @Column(length = 32)
  private String anchorStatus = "offchain"; // offchain, pending, onchain, failed

  @Column(length = 128)
  private String anchorTxHash;

  private LocalDate expireDate;

  @Enumerated(EnumType.STRING)
  private CertStatus status = CertStatus.VALID;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrg() {
    return org;
  }

  public void setOrg(String org) {
    this.org = org;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getAnchorStatus() {
    return anchorStatus;
  }

  public void setAnchorStatus(String anchorStatus) {
    this.anchorStatus = anchorStatus;
  }

  public String getAnchorTxHash() {
    return anchorTxHash;
  }

  public void setAnchorTxHash(String anchorTxHash) {
    this.anchorTxHash = anchorTxHash;
  }

  public LocalDate getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(LocalDate expireDate) {
    this.expireDate = expireDate;
  }

  public CertStatus getStatus() {
    return status;
  }

  public void setStatus(CertStatus status) {
    this.status = status;
  }
}
