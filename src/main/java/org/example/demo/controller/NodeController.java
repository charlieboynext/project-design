package org.example.demo.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

  private final ObjectProvider<Client> clientProvider;
  @Value("${system.bcos-enabled:false}")
  private boolean bcosEnabled;
  @Value("${system.peers:}")
  private String peers;

  public NodeController(ObjectProvider<Client> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @GetMapping
  public List<NodeStatus> list() {
    Client client = clientProvider.getIfAvailable();
    if (!bcosEnabled || client == null) {
      NodeStatus status = new NodeStatus();
      status.setName("bcos");
      status.setBlockHeight(0L);
      status.setLatency(null);
      status.setStatus("offline");
      status.setMessage("BCOS disabled or unavailable; returning placeholder data");
      return Collections.singletonList(status);
    }
    Instant start = Instant.now();
    long height = client.getBlockNumber().getBlockNumber().longValue();
    long latency = Duration.between(start, Instant.now()).toMillis();
    List<NodeStatus> list = new ArrayList<>();
    List<String> peerList = (peers == null || peers.isEmpty())
        ? Collections.singletonList("bcos")
        : Arrays.asList(peers.split(","));
    for (String peer : peerList) {
      NodeStatus status = new NodeStatus();
      status.setName(peer.trim());
      status.setBlockHeight(height);
      status.setLatency(latency);
      status.setStatus("normal");
      status.setMessage("Realtime data from BCOS");
      list.add(status);
    }
    return list;
  }

  public static class NodeStatus {
    private String name;
    private Long blockHeight;
    private Long latency;
    private String status;
    private String message;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Long getBlockHeight() {
      return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
      this.blockHeight = blockHeight;
    }

    public Long getLatency() {
      return latency;
    }

    public void setLatency(Long latency) {
      this.latency = latency;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
