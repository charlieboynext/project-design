package org.example.demo.model.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TraceEventRequest {

  @NotBlank
  private String type;

  @NotNull
  private Long productId;

  @NotNull
  private Long batchId;

  private String actorOrg;
  private String operatorName;
  private String location;
  private String memo;

  @NotNull
  private LocalDateTime eventTime;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public String getActorOrg() {
    return actorOrg;
  }

  public void setActorOrg(String actorOrg) {
    this.actorOrg = actorOrg;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public LocalDateTime getEventTime() {
    return eventTime;
  }

  public void setEventTime(LocalDateTime eventTime) {
    this.eventTime = eventTime;
  }
}
