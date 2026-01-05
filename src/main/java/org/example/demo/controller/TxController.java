package org.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.example.demo.model.CommonResponse;
import org.example.demo.model.entity.HashRecord;
import org.example.demo.repository.HashRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/txs")
public class TxController {

  private final HashRecordRepository hashRecordRepository;

  public TxController(HashRecordRepository hashRecordRepository) {
    this.hashRecordRepository = hashRecordRepository;
  }

  @GetMapping
  public CommonResponse list(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<HashRecord> paged = hashRecordRepository.findAll(pageable);
    List<TxResponse> items = paged.getContent().stream()
        .map(h -> {
          TxResponse r = new TxResponse();
          r.setTxHash(h.getTxHash());
          r.setTimestamp(h.getCreatedAt() != null ? h.getCreatedAt().toString() : null);
          r.setDataHash(h.getDataHash());
          r.setBlockNumber(null);
          r.setType("hash_record");
          r.setProductId(null);
          r.setBatchNo(null);
          return r;
        }).collect(Collectors.toList());
    return CommonResponse.ok(new org.example.demo.model.dto.PageResponse<>(items, paged.getTotalElements()));
  }

  public static class TxResponse {
    private String txHash;
    private Long blockNumber;
    private String type;
    private String productId;
    private String batchNo;
    private String timestamp;
    private String dataHash;

    public String getTxHash() {
      return txHash;
    }

    public void setTxHash(String txHash) {
      this.txHash = txHash;
    }

    public Long getBlockNumber() {
      return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
      this.blockNumber = blockNumber;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
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

    public String getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
    }

    public String getDataHash() {
      return dataHash;
    }

    public void setDataHash(String dataHash) {
      this.dataHash = dataHash;
    }
  }
}
