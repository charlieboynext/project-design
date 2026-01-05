package org.example.demo.model.bo;

import java.util.ArrayList;
import java.util.List;

public class AssetBalancesInputBO {
  private String arg0;

  public AssetBalancesInputBO() {}

  public AssetBalancesInputBO(String arg0) {
    this.arg0 = arg0;
  }

  public List<Object> toArgs() {
    List<Object> args = new ArrayList<>();
    args.add(arg0);
    return args;
  }

  public String getArg0() { return arg0; }
  public void setArg0(String arg0) { this.arg0 = arg0; }
}
