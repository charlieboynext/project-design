package org.example.demo.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "commodities")
public class Commodity extends BaseEntity {

  @Column(nullable = false, unique = true, length = 120)
  private String traceCode;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "batch_id")
  private Batch batch;

  @Column(length = 200)
  private String productName;

  @Column(length = 160)
  private String origin;

  @Column(nullable = false)
  private Integer quantity;

  @Column(precision = 10, scale = 2)
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

