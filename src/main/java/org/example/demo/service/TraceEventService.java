package org.example.demo.service;

import java.util.List;
import org.example.demo.model.dto.TraceEventRequest;
import org.example.demo.model.entity.Batch;
import org.example.demo.model.entity.HashRecord;
import org.example.demo.model.entity.Product;
import org.example.demo.model.entity.TraceEventRecord;
import org.example.demo.repository.BatchRepository;
import org.example.demo.repository.ProductRepository;
import org.example.demo.repository.TraceEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TraceEventService {

  private final TraceEventRepository traceEventRepository;
  private final ProductRepository productRepository;
  private final BatchRepository batchRepository;
  private final HashLedgerService hashLedgerService;

  public TraceEventService(TraceEventRepository traceEventRepository,
                           ProductRepository productRepository,
                           BatchRepository batchRepository,
                           HashLedgerService hashLedgerService) {
    this.traceEventRepository = traceEventRepository;
    this.productRepository = productRepository;
    this.batchRepository = batchRepository;
    this.hashLedgerService = hashLedgerService;
  }

  public List<TraceEventRecord> findAll() {
    return traceEventRepository.findAll();
  }

  public Page<TraceEventRecord> findPaged(Long batchId, Pageable pageable) {
    if (batchId != null) {
      Batch batch = batchRepository.findById(batchId).orElseThrow(() -> new IllegalArgumentException("Batch not found"));
      return traceEventRepository.findByBatch(batch, pageable);
    }
    return traceEventRepository.findAll(pageable);
  }

  @Transactional
  public TraceEventRecord create(TraceEventRequest request) {
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    Batch batch = batchRepository.findById(request.getBatchId())
        .orElseThrow(() -> new IllegalArgumentException("Batch not found"));
    TraceEventRecord record = new TraceEventRecord();
    record.setType(request.getType());
    record.setProduct(product);
    record.setBatch(batch);
    record.setActorOrg(request.getActorOrg());
    record.setOperatorName(request.getOperatorName());
    record.setLocation(request.getLocation());
    record.setMemo(request.getMemo());
    record.setEventTime(request.getEventTime());

    HashWrapper wrapper = new HashWrapper(request);
    HashRecord hashRecord = hashLedgerService.recordPayload(wrapper);
    record.setDataHash(hashRecord.getDataHash());
    record.setTxHash(hashRecord.getTxHash());
    return traceEventRepository.save(record);
  }

  private static class HashWrapper {
    private final TraceEventRequest request;

    private HashWrapper(TraceEventRequest request) {
      this.request = request;
    }

    public TraceEventRequest getRequest() {
      return request;
    }
  }
}
