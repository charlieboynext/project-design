package org.example.demo.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "qr_codes")
public class QRCode extends BaseEntity {

  @Column(nullable = false, unique = true, length = 120)
  private String traceCode;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "batch_id")
  private Batch batch;

  @Enumerated(EnumType.STRING)
  private QRCodeStatus status = QRCodeStatus.UNUSED;

  private LocalDateTime bindTime;

  public String getTraceCode() {
    return traceCode;
  }

  public void setTraceCode(String traceCode) {
    this.traceCode = traceCode;
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

  public QRCodeStatus getStatus() {
    return status;
  }

  public void setStatus(QRCodeStatus status) {
    this.status = status;
  }

  public LocalDateTime getBindTime() {
    return bindTime;
  }

  public void setBindTime(LocalDateTime bindTime) {
    this.bindTime = bindTime;
  }
}
