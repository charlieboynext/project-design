package org.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.example.demo.model.entity.Org;
import org.example.demo.repository.OrgRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orgs")
public class OrgController {

  private final OrgRepository orgRepository;

  public OrgController(OrgRepository orgRepository) {
    this.orgRepository = orgRepository;
  }

  @GetMapping
  public List<OrgResponse> list() {
    return orgRepository.findAll().stream().map(OrgResponse::from).collect(Collectors.toList());
  }

  @PostMapping
  public OrgResponse create(@Valid @RequestBody OrgRequest request) {
    Org org = new Org();
    org.setName(request.getName());
    org.setRole(request.getRole());
    org.setNodeId(request.getNodeId());
    org.setContact(request.getContact());
    org.setActive(request.isActive() != null ? request.isActive() : true);
    return OrgResponse.from(orgRepository.save(org));
  }

  @PutMapping("/{id}")
  public OrgResponse update(@PathVariable Long id, @Valid @RequestBody OrgRequest request) {
    Org org = orgRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Org not found"));
    org.setName(request.getName());
    org.setRole(request.getRole());
    org.setNodeId(request.getNodeId());
    org.setContact(request.getContact());
    if (request.isActive() != null) {
      org.setActive(request.isActive());
    }
    return OrgResponse.from(orgRepository.save(org));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (orgRepository.existsById(id)) {
      orgRepository.deleteById(id);
    }
    return ResponseEntity.noContent().build();
  }

  public static class OrgRequest {
    @NotBlank
    private String name;
    private String role;
    private String nodeId;
    private String contact;
    private Boolean active;

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

    public Boolean isActive() {
      return active;
    }

    public void setActive(Boolean active) {
      this.active = active;
    }
  }

  public static class OrgResponse {
    private Long id;
    private String name;
    private String role;
    private String nodeId;
    private String contact;
    private Boolean active;

    public static OrgResponse from(Org org) {
      OrgResponse resp = new OrgResponse();
      resp.setId(org.getId());
      resp.setName(org.getName());
      resp.setRole(org.getRole());
      resp.setNodeId(org.getNodeId());
      resp.setContact(org.getContact());
      resp.setActive(org.isActive());
      return resp;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

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

    public Boolean getActive() {
      return active;
    }

    public void setActive(Boolean active) {
      this.active = active;
    }
  }
}
