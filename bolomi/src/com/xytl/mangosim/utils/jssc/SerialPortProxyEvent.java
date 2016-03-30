package com.xytl.mangosim.utils.jssc;

public class SerialPortProxyEvent
{
  private long creationTime;
  private long timeExecuted;

  public SerialPortProxyEvent(long time)
  {
    this.creationTime = time;
  }

  public long getCreationTime() {
    return this.creationTime;
  }

  public void setTimeExecuted(long time) {
    this.timeExecuted = time;
  }
  public long getTimeExecuted() {
    return this.timeExecuted;
  }
}