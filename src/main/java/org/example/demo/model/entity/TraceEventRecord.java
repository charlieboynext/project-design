package org.example.demo.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "trace_events")
public class TraceEventRecord extends BaseEntity {

  @Column(nullable = false, length = 120)
  private String type;

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(optional = false)
  @JoinColumn(name = "batch_id")
  private Batch batch;

  @Column(length = 160)
  private String actorOrg;

  @Column(length = 80)
  private String operatorName;

  @Column(length = 200)
  private String location;

  private LocalDateTime eventTime;

  @Column(length = 400)
  private String memo;

  @Column(length = 128)
  private String dataHash;

  @Column(length = 128)
  private String txHash;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Batch getBatch() {
    return batch;
  }

  public void setBatch(Batch batch) {
    this.batch = batch;
  }

  public String getActorOrg() {
    return actorOrg;
  }

  public void setActorOrg(String actorOrg) {
    this.actorOrg = actorOrg;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public LocalDateTime getEventTime() {
    return eventTime;
  }

  public void setEventTime(LocalDateTime eventTime) {
    this.eventTime = eventTime;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public String getDataHash() {
    return dataHash;
  }

  public void setDataHash(String dataHash) {
    this.dataHash = dataHash;
  }

  public String getTxHash() {
    return txHash;
  }

  public void setTxHash(String txHash) {
    this.txHash = txHash;
  }
}
