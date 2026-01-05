package org.example.demo.model.dto;

import javax.validation.constraints.NotBlank;

public class ProductRequest {

  @NotBlank
  private String name;

  private String brand;
  private String category;
  private String spec;
  private String origin;
  private String productCode;
  private String factory;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getSpec() {
    return spec;
  }

  public void setSpec(String spec) {
    this.spec = spec;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getFactory() {
    return factory;
  }

  public void setFactory(String factory) {
    this.factory = factory;
  }
}
