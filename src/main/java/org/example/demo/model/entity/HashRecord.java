package org.example.demo.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "hash_records")
public class HashRecord extends BaseEntity {

  @Column(nullable = false, length = 128, unique = true)
  private String dataHash;

  @Lob
  private String payload;

  @Column(length = 128)
  private String txHash;

  public String getDataHash() {
    return dataHash;
  }

  public void setDataHash(String dataHash) {
    this.dataHash = dataHash;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getTxHash() {
    return txHash;
  }

  public void setTxHash(String txHash) {
    this.txHash = txHash;
  }
}
