package org.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import org.example.demo.model.entity.HashRecord;
import org.example.demo.repository.HashRecordRepository;
import org.example.demo.utils.IOUtil;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HashLedgerService {

  private static final Logger log = LoggerFactory.getLogger(HashLedgerService.class);
  private static final String HASH_ABI = IOUtil.readResourceAsString("abi/HashRecorder.abi");

  private final HashRecordRepository hashRecordRepository;
  private final ObjectMapper objectMapper;
  private final AssembleTransactionProcessor txProcessor;
  private final String hashRecorderAddress;

  public HashLedgerService(HashRecordRepository hashRecordRepository,
                           ObjectMapper objectMapper,
                           ObjectProvider<Client> clientProvider,
                           @Value("${system.contract.hashRecorderAddress}") String hashRecorderAddress) throws Exception {
    this.hashRecordRepository = hashRecordRepository;
    this.objectMapper = objectMapper;
    this.hashRecorderAddress = hashRecorderAddress;
    Client client = clientProvider.getIfAvailable();
    if (client != null) {
      this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
          client, client.getCryptoSuite().getCryptoKeyPair());
    } else {
      this.txProcessor = null;
      log.info("BCOS client disabled, HashRecorder will only store hashes in the database.");
    }
  }

  public HashRecord recordPayload(Object payload) {
    try {
      String payloadJson = objectMapper.writeValueAsString(payload);
      String hash = sha256(payloadJson);
      java.util.Optional<HashRecord> existing = hashRecordRepository.findByDataHash(hash);
      if (existing.isPresent()) {
        return existing.get();
      }
      String txHash = null;
      if (isContractConfigured()) {
        TransactionResponse response = txProcessor.sendTransactionAndGetResponse(
            hashRecorderAddress, HASH_ABI, "storeHash", Collections.singletonList(hash));
        if (response.getTransactionReceipt() != null) {
          txHash = response.getTransactionReceipt().getTransactionHash();
        }
      }
      HashRecord record = new HashRecord();
      record.setDataHash(hash);
      record.setPayload(payloadJson);
      record.setTxHash(txHash);
      return hashRecordRepository.save(record);
    } catch (Exception ex) {
      throw new IllegalStateException("Failed to record hash", ex);
    }
  }

  private String sha256(String input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
    StringBuilder sb = new StringBuilder();
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        sb.append('0');
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  private boolean isContractConfigured() {
    if (this.txProcessor == null) {
      return false;
    }
    if (!StringUtils.hasText(hashRecorderAddress)) {
      return false;
    }
    String normalized = hashRecorderAddress.toLowerCase();
    if (normalized.startsWith("0x")) {
      normalized = normalized.substring(2);
    }
    return normalized.replace("0", "").length() > 0;
  }
}
