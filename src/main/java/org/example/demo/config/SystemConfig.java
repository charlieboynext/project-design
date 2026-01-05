package org.example.demo.config;

import java.lang.String;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemConfig {
  private String peers;
  private int groupId = 1;
  private String certPath = "conf";
  private String hexPrivateKey;
  private boolean bcosEnabled = true;
  @NestedConfigurationProperty
  private ContractConfig contract;

  // 显式 getter/setter，避免 Lombok 失效
  public String getPeers() { return peers; }
  public void setPeers(String peers) { this.peers = peers; }

  public int getGroupId() { return groupId; }
  public void setGroupId(int groupId) { this.groupId = groupId; }

  public String getCertPath() { return certPath; }
  public void setCertPath(String certPath) { this.certPath = certPath; }

  public String getHexPrivateKey() { return hexPrivateKey; }
  public void setHexPrivateKey(String hexPrivateKey) { this.hexPrivateKey = hexPrivateKey; }

  public boolean isBcosEnabled() { return bcosEnabled; }
  public void setBcosEnabled(boolean bcosEnabled) { this.bcosEnabled = bcosEnabled; }

  public ContractConfig getContract() { return contract; }
  public void setContract(ContractConfig contract) { this.contract = contract; }
}
