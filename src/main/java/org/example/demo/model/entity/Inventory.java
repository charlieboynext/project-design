package org.example.demo.model.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventories")
public class Inventory extends BaseEntity {

  @Column(nullable = false, unique = true, length = 120)
  private String traceCode;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "batch_id")
  private Batch batch;

  @Column(nullable = false)
  private Integer shipmentQuantity;

  private LocalDate shipmentDate;

  @Column(length = 160)
  private String origin;

  @Column(length = 200)
  private String productName;

  @Column(length = 500)
  private String deliveryAddress;

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

