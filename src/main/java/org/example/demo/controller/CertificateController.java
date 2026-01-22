package org.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.example.demo.model.entity.CertStatus;
import org.example.demo.model.entity.Certificate;
import org.example.demo.repository.CertificateRepository;
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
@RequestMapping("/api/certs")
public class CertificateController {

  private final CertificateRepository certificateRepository;

  public CertificateController(CertificateRepository certificateRepository) {
    this.certificateRepository = certificateRepository;
  }

  @GetMapping
  public List<CertificateResponse> list() {
    return certificateRepository.findAll().stream().map(cert -> {
      // 到期自动标记
      if (cert.getExpireDate() != null && cert.getExpireDate().isBefore(java.time.LocalDate.now())) {
        cert.setStatus(CertStatus.EXPIRED);
        certificateRepository.save(cert);
      }
      return CertificateResponse.from(cert);
    }).collect(Collectors.toList());
  }

  @PostMapping
  public CertificateResponse create(@Valid @RequestBody CertificateRequest request) {
    Certificate cert = new Certificate();
    cert.setName(request.getName());
    cert.setOrg(request.getOrg());
    cert.setHash(request.getHash());
    cert.setAnchorStatus(request.getAnchorStatus() != null ? request.getAnchorStatus() : "offchain");
    cert.setAnchorTxHash(request.getAnchorTxHash());
    cert.setExpireDate(request.getExpireDate());
    cert.setStatus(request.getStatus() != null ? request.getStatus() : CertStatus.VALID);
    return CertificateResponse.from(certificateRepository.save(cert));
  }

  @PutMapping("/{id}")
  public CertificateResponse update(@PathVariable Long id, @Valid @RequestBody CertificateRequest request) {
    Certificate cert = certificateRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cert not found"));
    cert.setName(request.getName());
    cert.setOrg(request.getOrg());
    cert.setHash(request.getHash());
    if (request.getAnchorStatus() != null) {
      cert.setAnchorStatus(request.getAnchorStatus());
    }
    cert.setAnchorTxHash(request.getAnchorTxHash());
    cert.setExpireDate(request.getExpireDate());
    cert.setStatus(request.getStatus() != null ? request.getStatus() : CertStatus.VALID);
    return CertificateResponse.from(certificateRepository.save(cert));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (certificateRepository.existsById(id)) {
      certificateRepository.deleteById(id);
    }
    return ResponseEntity.noContent().build();
  }

  public static class CertificateRequest {
    private String name;
    private String org;
    private String hash;
    private String anchorStatus;
    private String anchorTxHash;
    private java.time.LocalDate expireDate;
    private CertStatus status;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getOrg() {
      return org;
    }

    public void setOrg(String org) {
      this.org = org;
    }

    public String getHash() {
      return hash;
    }

    public void setHash(String hash) {
      this.hash = hash;
    }

    public String getAnchorStatus() {
      return anchorStatus;
    }

    public void setAnchorStatus(String anchorStatus) {
      this.anchorStatus = anchorStatus;
    }

    public String getAnchorTxHash() {
      return anchorTxHash;
    }

    public void setAnchorTxHash(String anchorTxHash) {
      this.anchorTxHash = anchorTxHash;
    }

    public java.time.LocalDate getExpireDate() {
      return expireDate;
    }

    public void setExpireDate(java.time.LocalDate expireDate) {
      this.expireDate = expireDate;
    }

    public CertStatus getStatus() {
      return status;
    }

    public void setStatus(CertStatus status) {
      this.status = status;
    }
  }

  public static class CertificateResponse {
    private Long id;
    private String name;
    private String org;
    private String hash;
    private String expireDate;
    private String status;
    private String anchorStatus;
    private String anchorTxHash;

    public static CertificateResponse from(Certificate cert) {
      CertificateResponse resp = new CertificateResponse();
      resp.setId(cert.getId());
      resp.setName(cert.getName());
      resp.setOrg(cert.getOrg());
      resp.setHash(cert.getHash());
      resp.setExpireDate(cert.getExpireDate() != null ? cert.getExpireDate().toString() : null);
      resp.setStatus(cert.getStatus() != null ? cert.getStatus().name().toLowerCase() : null);
      resp.setAnchorStatus(cert.getAnchorStatus());
      resp.setAnchorTxHash(cert.getAnchorTxHash());
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

    public String getOrg() {
      return org;
    }

    public void setOrg(String org) {
      this.org = org;
    }

    public String getHash() {
      return hash;
    }

    public void setHash(String hash) {
      this.hash = hash;
    }

    public String getExpireDate() {
      return expireDate;
    }

    public void setExpireDate(String expireDate) {
      this.expireDate = expireDate;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getAnchorStatus() {
      return anchorStatus;
    }

    public void setAnchorStatus(String anchorStatus) {
      this.anchorStatus = anchorStatus;
    }

    public String getAnchorTxHash() {
      return anchorTxHash;
    }

    public void setAnchorTxHash(String anchorTxHash) {
      this.anchorTxHash = anchorTxHash;
    }
  }
}
