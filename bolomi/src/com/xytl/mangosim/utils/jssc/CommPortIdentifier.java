package com.xytl.mangosim.utils.jssc;

public class CommPortIdentifier
{
  public static final int PORT_SERIAL = 0;
  public static final int PORT_PARALLEL = 1;
  private String name;
  private int portType;
  private boolean currentlyOwned = false;
  private String currentOwner = "";

  public CommPortIdentifier(String name, boolean isComm)
  {
    this.name = name;
    if (isComm)
      this.portType = 0;
    else
      this.portType = 1;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPortType() {
    return this.portType;
  }

  public void setPortType(int portType) {
    this.portType = portType;
  }

  public boolean isCurrentlyOwned() {
    return this.currentlyOwned;
  }

  public void setCurrentlyOwned(boolean currentlyOwned) {
    this.currentlyOwned = currentlyOwned;
  }

  public String getCurrentOwner() {
    return this.currentOwner;
  }

  public void setCurrentOwner(String currentOwner) {
    this.currentOwner = currentOwner;
  }
}