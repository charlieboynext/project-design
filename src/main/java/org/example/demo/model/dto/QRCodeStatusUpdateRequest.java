package org.example.demo.model.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

public class QRCodeStatusUpdateRequest {

  @NotBlank
  private String status;

  private LocalDateTime bindTime;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getBindTime() {
    return bindTime;
  }

  public void setBindTime(LocalDateTime bindTime) {
    this.bindTime = bindTime;
  }
}
