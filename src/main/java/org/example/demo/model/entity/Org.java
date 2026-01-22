package org.example.demo.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 组织/节点信息，支持前端组织管理。
 */
@Entity
@Table(name = "orgs")
public class Org extends BaseEntity {

  @Column(nullable = false, length = 160)
  private String name;

  @Column(length = 80)
  private String role;

  @Column(length = 120)
  private String nodeId;

  @Column(length = 120)
  private String contact;

  @Column(nullable = false)
  private boolean active = true;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
