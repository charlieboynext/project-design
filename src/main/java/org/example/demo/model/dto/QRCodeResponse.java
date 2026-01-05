package org.example.demo.model.dto;

public class QRCodeResponse {
  private Long id;
  private String traceCode;
  private String productId;
  private String batchNo;
  private String status;
  private String bindTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTraceCode() {
    return traceCode;
  }

  public void setTraceCode(String traceCode) {
    this.traceCode = traceCode;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getBatchNo() {
    return batchNo;
  }

  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getBindTime() {
    return bindTime;
  }

  public void setBindTime(String bindTime) {
    this.bindTime = bindTime;
  }
}
