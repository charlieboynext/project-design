package org.example.demo.model.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "batches")
public class Batch extends BaseEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(nullable = false, unique = true, length = 80)
  private String batchNo;

  private LocalDate manufactureDate;

  private LocalDate expireDate;

  private Integer quantity;

  @Column(length = 40)
  private String unit;

  @Column(length = 160)
  private String factory;

  @Enumerated(EnumType.STRING)
  private BatchStatus status = BatchStatus.PRODUCING;

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getBatchNo() {
    return batchNo;
  }

  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

  public LocalDate getManufactureDate() {
    return manufactureDate;
  }

  public void setManufactureDate(LocalDate manufactureDate) {
    this.manufactureDate = manufactureDate;
  }

  public LocalDate getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(LocalDate expireDate) {
    this.expireDate = expireDate;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getFactory() {
    return factory;
  }

  public void setFactory(String factory) {
    this.factory = factory;
  }

  public BatchStatus getStatus() {
    return status;
  }

  public void setStatus(BatchStatus status) {
    this.status = status;
  }
}
