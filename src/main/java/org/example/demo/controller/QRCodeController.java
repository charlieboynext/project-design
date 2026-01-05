package org.example.demo.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.example.demo.model.dto.QRCodeGenerateRequest;
import org.example.demo.model.dto.QRCodeResponse;
import org.example.demo.model.dto.QRCodeStatusUpdateRequest;
import org.example.demo.service.QRCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qrcodes")
public class QRCodeController {

  private final QRCodeService qrCodeService;

  public QRCodeController(QRCodeService qrCodeService) {
    this.qrCodeService = qrCodeService;
  }

  @GetMapping
  public List<QRCodeResponse> list(@RequestParam(value = "batchNo", required = false) String batchNo) {
    return qrCodeService.list(batchNo);
  }

  @GetMapping("/export")
  public ResponseEntity<String> export(@RequestParam(value = "batchNo", required = false) String batchNo) {
    List<QRCodeResponse> list = qrCodeService.list(batchNo);
    StringBuilder sb = new StringBuilder();
    sb.append("traceCode,batchNo,productId,status,bindTime\n");
    list.forEach(c -> sb.append(String.join(",",
            safe(c.getTraceCode()),
            safe(c.getBatchNo()),
            safe(c.getProductId()),
            safe(c.getStatus()),
            safe(c.getBindTime()))
        ).append("\n"));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qrcodes.csv")
        .contentType(MediaType.TEXT_PLAIN)
        .body(sb.toString());
  }

  @PostMapping("/generate")
  public List<QRCodeResponse> generate(@Valid @RequestBody QRCodeGenerateRequest request) {
    return qrCodeService.generate(request);
  }

  @PutMapping("/{id}/status")
  public QRCodeResponse updateStatus(@PathVariable Long id, @Valid @RequestBody QRCodeStatusUpdateRequest request) {
    return qrCodeService.updateStatus(id, request.getStatus(), request.getBindTime());
  }

  private String safe(String v) {
    return v == null ? "" : v;
  }
}
