package org.example.demo.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.example.demo.model.entity.Batch;
import org.example.demo.model.entity.Product;
import org.example.demo.model.entity.QRCode;
import org.example.demo.model.entity.TraceEventRecord;
import org.example.demo.repository.BatchRepository;
import org.example.demo.repository.QRCodeRepository;
import org.example.demo.repository.TraceEventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/trace")
public class TraceQueryController {

  private final BatchRepository batchRepository;
  private final TraceEventRepository traceEventRepository;
  private final QRCodeRepository qrCodeRepository;

  public TraceQueryController(BatchRepository batchRepository, TraceEventRepository traceEventRepository,
                              QRCodeRepository qrCodeRepository) {
    this.batchRepository = batchRepository;
    this.traceEventRepository = traceEventRepository;
    this.qrCodeRepository = qrCodeRepository;
  }

  @GetMapping("/{batchNo}")
  public TraceResult traceByCode(@PathVariable String batchNo) {
    Batch batch = resolveBatch(batchNo);
    Product product = batch.getProduct();
    List<TraceEventRecord> events = traceEventRepository.findByBatchOrderByEventTimeAsc(batch);
    TraceResult result = new TraceResult();
    result.setProduct(product);
    result.setBatch(batch);
    result.setEvents(events);
    result.setProofs(
        events.stream()
            .map(TraceEventRecord::getTxHash)
            .filter(Objects::nonNull)
            .distinct()
            .map(Proof::new)
            .collect(Collectors.toList()));
    return result;
  }

  private Batch resolveBatch(String code) {
    return qrCodeRepository.findByTraceCode(code)
        .map(QRCode::getBatch)
        .orElseGet(() -> batchRepository.findByBatchNo(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch/trace code not found")));
  }

  public static class TraceResult {
    private Product product;
    private Batch batch;
    private List<TraceEventRecord> events;
    private List<Proof> proofs;

    public Product getProduct() {
      return product;
    }

    public void setProduct(Product product) {
      this.product = product;
    }

    public Batch getBatch() {
      return batch;
    }

    public void setBatch(Batch batch) {
      this.batch = batch;
    }

    public List<TraceEventRecord> getEvents() {
      return events;
    }

    public void setEvents(List<TraceEventRecord> events) {
      this.events = events;
    }

    public List<Proof> getProofs() {
      return proofs;
    }

    public void setProofs(List<Proof> proofs) {
      this.proofs = proofs;
    }
  }

  public static class Proof {
    private String txHash;
    private Long blockNumber;

    public Proof() {
    }

    public Proof(String txHash) {
      this.txHash = txHash;
    }

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
  }
}
