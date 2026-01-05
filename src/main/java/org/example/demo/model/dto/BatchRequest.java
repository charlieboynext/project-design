package org.example.demo.model.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BatchRequest {

  @NotNull
  private Long productId;

  @NotBlank
  private String batchNo;

  @NotNull
  private LocalDate manufactureDate;

  @NotNull
  private LocalDate expireDate;

  private Integer quantity;
  private String unit;
  private String factory;
  private String status;

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
