package com.xytl.mangosim.utils.jssc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.lang.StringUtils;




public class CommPortProxy
{
  private final Log LOG = LogFactory.getLog(CommPortProxy.class);
  private final String name;
  private String portType;
  private final boolean currentlyOwned;
  private final String currentOwner;
  private String hardwareId;
  private String product;

  public CommPortProxy(CommPortIdentifier cpid)
  {
    this.name = cpid.getName();
    switch (cpid.getPortType()) {
    case 0:
      this.portType = "Serial";
      break;
    case 1:
      this.portType = "Parallel";
      break;
    default:
      this.portType = ("Unknown (" + cpid.getPortType() + ")");
    }
    this.currentlyOwned = cpid.isCurrentlyOwned();
    this.currentOwner = cpid.getCurrentOwner();

    if (this.LOG.isDebugEnabled()) {
      String output = "Creating comm port with id: " + cpid.getName();
      if (this.currentlyOwned)
        output = output + " Owned by " + cpid.getCurrentOwner();
      this.LOG.debug(output);
    }
  }

  public CommPortProxy(String name, boolean serial)
  {
    this.name = name;
    this.portType = (serial ? "Serial" : "Parallel");
    this.currentlyOwned = false;
    this.currentOwner = null;

    if (this.LOG.isDebugEnabled())
      this.LOG.debug("Creating comm port with id: " + name);
  }

  public boolean isCurrentlyOwned() {
    return this.currentlyOwned;
  }

  public String getCurrentOwner() {
    return this.currentOwner;
  }

  public String getName() {
    return this.name;
  }

  public String getPortType() {
    return this.portType;
  }

  public String getHardwareId() {
    return this.hardwareId;
  }

  public void setHardwareId(String hardwareId) {
    this.hardwareId = hardwareId;
  }

  public String getProduct() {
    return this.product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getId() {
    if (StringUtils.isEmpty(this.hardwareId))
      return this.name;
    return this.hardwareId;
  }

  public String getDescription() {
    if ((!StringUtils.isEmpty(this.hardwareId)) && (!StringUtils.isEmpty(this.product)))
      return this.hardwareId + " (" + this.product.trim() + ")";
    if (!StringUtils.isEmpty(this.hardwareId))
      return this.hardwareId + " (" + this.name + ")";
    return this.name;
  }
}