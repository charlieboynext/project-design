package org.example.demo.model.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class InventoryRequest {

  @NotBlank
  private String traceCode;

  @NotNull
  private Long productId;

  private Long batchId;

  @NotNull
  private Integer shipmentQuantity;

  private LocalDate shipmentDate;

  private String origin;

  private String productName;

  private String deliveryAddress;

  public String getTraceCode() {
    return traceCode;
  }

  public void setTraceCode(String traceCode) {
    this.traceCode = traceCode;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Long getBatchId() {
    return batchId;
  }

  public void setBatchId(Long batchId) {
    this.batchId = batchId;
  }

  public Integer getShipmentQuantity() {
    return shipmentQuantity;
  }

  public void setShipmentQuantity(Integer shipmentQuantity) {
    this.shipmentQuantity = shipmentQuantity;
  }

  public LocalDate getShipmentDate() {
    return shipmentDate;
  }

  public void setShipmentDate(LocalDate shipmentDate) {
    this.shipmentDate = shipmentDate;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }
}

