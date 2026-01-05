package org.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.example.demo.model.dto.QRCodeGenerateRequest;
import org.example.demo.model.dto.QRCodeResponse;
import org.example.demo.model.entity.Batch;
import org.example.demo.model.entity.QRCode;
import org.example.demo.model.entity.QRCodeStatus;
import org.example.demo.repository.BatchRepository;
import org.example.demo.repository.QRCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class QRCodeService {

  private final QRCodeRepository qrCodeRepository;
  private final BatchRepository batchRepository;

  public QRCodeService(QRCodeRepository qrCodeRepository, BatchRepository batchRepository) {
    this.qrCodeRepository = qrCodeRepository;
    this.batchRepository = batchRepository;
  }

  @Transactional(readOnly = true)
  public List<QRCodeResponse> list(String batchNo) {
    List<QRCode> codes = (!StringUtils.hasText(batchNo))
        ? qrCodeRepository.findAllByOrderByIdDesc()
        : qrCodeRepository.findByBatch_BatchNoContainingIgnoreCaseOrderByIdDesc(batchNo);
    return codes.stream().map(this::toDto).collect(Collectors.toList());
  }

  @Transactional
  public List<QRCodeResponse> generate(QRCodeGenerateRequest request) {
    Batch batch = batchRepository.findByBatchNo(request.getBatchNo())
        .orElseThrow(() -> new IllegalArgumentException("Batch not found"));
    List<QRCode> created = new ArrayList<>(request.getCount());
    for (int i = 0; i < request.getCount(); i++) {
      QRCode code = new QRCode();
      code.setBatch(batch);
      code.setProduct(batch.getProduct());
      code.setStatus(QRCodeStatus.UNUSED);
      code.setTraceCode(buildCode(batch.getBatchNo(), i));
      created.add(code);
    }
    return qrCodeRepository.saveAll(created).stream().map(this::toDto).collect(Collectors.toList());
  }

  private String buildCode(String batchNo, int index) {
    String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    return batchNo + "-" + suffix + "-" + index;
  }

  private QRCodeResponse toDto(QRCode code) {
    QRCodeResponse dto = new QRCodeResponse();
    dto.setId(code.getId());
    dto.setTraceCode(code.getTraceCode());
    if (code.getProduct() != null && code.getProduct().getId() != null) {
      dto.setProductId(String.valueOf(code.getProduct().getId()));
    }
    if (code.getBatch() != null) {
      dto.setBatchNo(code.getBatch().getBatchNo());
    }
    dto.setStatus(code.getStatus() != null ? code.getStatus().name().toLowerCase() : null);
    LocalDateTime bindTime = code.getBindTime();
    dto.setBindTime(bindTime != null ? bindTime.toString() : null);
    return dto;
  }

  @Transactional
  public QRCodeResponse updateStatus(Long id, String status, LocalDateTime bindTime) {
    QRCode code = qrCodeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("QRCode not found"));
    QRCodeStatus newStatus = QRCodeStatus.valueOf(status.toUpperCase());
    validateTransition(code.getStatus(), newStatus);
    code.setStatus(newStatus);
    if (newStatus == QRCodeStatus.BOUND) {
      code.setBindTime(bindTime != null ? bindTime : LocalDateTime.now());
    } else if (newStatus == QRCodeStatus.UNUSED) {
      code.setBindTime(null);
    }
    return toDto(qrCodeRepository.save(code));
  }

  private void validateTransition(QRCodeStatus current, QRCodeStatus target) {
    if (current == null) return;
    switch (target) {
      case BOUND:
        if (current != QRCodeStatus.UNUSED) {
          throw new IllegalStateException("仅未使用的码可以绑定");
        }
        break;
      case VOID:
        if (current == QRCodeStatus.VOID) {
          throw new IllegalStateException("已作废的码不可重复作废");
        }
        break;
      case RECALL:
        if (current != QRCodeStatus.BOUND) {
          throw new IllegalStateException("仅已绑定的码可以召回");
        }
        break;
      case UNUSED:
        // 允许重置为未使用（仅内部操作），不额外限制
        break;
      default:
        throw new IllegalArgumentException("不支持的状态");
    }
  }
}
