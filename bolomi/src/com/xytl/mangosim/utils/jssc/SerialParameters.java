package com.xytl.mangosim.utils.jssc;


public class SerialParameters
{
  private String commPortId;
  private String portOwnerName;
  private int baudRate = -1;
  private int flowControlIn = 0;
  private int flowControlOut = 0;
  private int dataBits = 8;
  private int stopBits = 1;
  private int parity = 0;

  private int recieveTimeout = 0;

  public int getBaudRate() {
    return this.baudRate;
  }

  public void setBaudRate(int baudRate) {
    this.baudRate = baudRate;
  }

  public String getCommPortId() {
    return this.commPortId;
  }

  public void setCommPortId(String commPortId) {
    this.commPortId = commPortId;
  }

  public int getDataBits() {
    return this.dataBits;
  }

  public void setDataBits(int dataBits) {
    this.dataBits = dataBits;
  }

  public int getFlowControlIn() {
    return this.flowControlIn;
  }

  public void setFlowControlIn(int flowControlIn) {
    this.flowControlIn = flowControlIn;
  }

  public int getFlowControlOut() {
    return this.flowControlOut;
  }

  public void setFlowControlOut(int flowControlOut) {
    this.flowControlOut = flowControlOut;
  }

  public int getParity() {
    return this.parity;
  }

  public void setParity(int parity) {
    this.parity = parity;
  }

  public int getStopBits() {
    return this.stopBits;
  }

  public void setStopBits(int stopBits) {
    this.stopBits = stopBits;
  }

  public String getPortOwnerName() {
    return this.portOwnerName;
  }

  public void setPortOwnerName(String portOwnerName) {
    this.portOwnerName = portOwnerName;
  }

  public int getRecieveTimeout() {
    return this.recieveTimeout;
  }

  public void setRecieveTimeout(int recieveTimeout) {
    this.recieveTimeout = recieveTimeout;
  }

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + this.baudRate;
    result = 31 * result + (this.commPortId == null ? 0 : this.commPortId.hashCode());
    result = 31 * result + this.dataBits;
    result = 31 * result + this.flowControlIn;
    result = 31 * result + this.flowControlOut;
    result = 31 * result + this.parity;
    result = 31 * result + (this.portOwnerName == null ? 0 : this.portOwnerName.hashCode());
    result = 31 * result + this.recieveTimeout;
    result = 31 * result + this.stopBits;
    return result;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SerialParameters other = (SerialParameters)obj;
    if (this.baudRate != other.baudRate)
      return false;
    if (this.commPortId == null) {
      if (other.commPortId != null)
        return false;
    }
    else if (!this.commPortId.equals(other.commPortId))
      return false;
    if (this.dataBits != other.dataBits)
      return false;
    if (this.flowControlIn != other.flowControlIn)
      return false;
    if (this.flowControlOut != other.flowControlOut)
      return false;
    if (this.parity != other.parity)
      return false;
    if (this.portOwnerName == null) {
      if (other.portOwnerName != null)
        return false;
    }
    else if (!this.portOwnerName.equals(other.portOwnerName))
      return false;
    if (this.recieveTimeout != other.recieveTimeout)
      return false;
    if (this.stopBits != other.stopBits)
      return false;
    return true;
  }
}