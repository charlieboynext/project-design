package org.example.demo.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class QRCodeGenerateRequest {

  @NotBlank
  private String batchNo;

  @Min(1)
  private int count = 10;

  public String getBatchNo() {
    return batchNo;
  }

  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
