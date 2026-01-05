package org.example.demo.controller;

import org.example.demo.model.dto.PageResponse;
import org.example.demo.model.dto.TraceEventRequest;
import org.example.demo.model.entity.TraceEventRecord;
import org.example.demo.service.TraceEventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class TraceEventController {

  private final TraceEventService traceEventService;

  public TraceEventController(TraceEventService traceEventService) {
    this.traceEventService = traceEventService;
  }

  @GetMapping
  public PageResponse<TraceEventRecord> list(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size,
                                             @RequestParam(required = false) Long batchId) {
    Pageable pageable = PageRequest.of(page, size);
    Page<TraceEventRecord> result = traceEventService.findPaged(batchId, pageable);
    return new PageResponse<>(result.getContent(), result.getTotalElements());
  }

  @PostMapping
  public TraceEventRecord create(@Valid @RequestBody TraceEventRequest request) {
    return traceEventService.create(request);
  }
}
