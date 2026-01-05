package org.example.demo.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommodityRequest {

  @NotBlank
  private String traceCode;

  @NotNull
  private Long productId;

  private Long batchId;

  private String productName;

  private String origin;

  @NotNull
  private Integer quantity;

  private BigDecimal listingPrice;

  private LocalDate listingDate;

  private LocalDate shipmentDate;

  private LocalDate receiptDate;

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

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getListingPrice() {
    return listingPrice;
  }

  public void setListingPrice(BigDecimal listingPrice) {
    this.listingPrice = listingPrice;
  }

  public LocalDate getListingDate() {
    return listingDate;
  }

  public void setListingDate(LocalDate listingDate) {
    this.listingDate = listingDate;
  }

  public LocalDate getShipmentDate() {
    return shipmentDate;
  }

  public void setShipmentDate(LocalDate shipmentDate) {
    this.shipmentDate = shipmentDate;
  }

  public LocalDate getReceiptDate() {
    return receiptDate;
  }

  public void setReceiptDate(LocalDate receiptDate) {
    this.receiptDate = receiptDate;
  }
}

