package com.xytl.mangosim.utils.jssc;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerialPortProxyEventTask extends Thread
{
  private final Log LOG = LogFactory.getLog(SerialPortProxyEventTask.class);
  private SerialPortProxyEventListener listener;
  private SerialPortProxyEvent event;
  private long creationTime;
  private SerialPortProxyEventCompleteListener completeListener;

  public SerialPortProxyEventTask(SerialPortProxyEventListener listener, SerialPortProxyEvent event, SerialPortProxyEventCompleteListener completeListener)
  {
    this.creationTime = System.currentTimeMillis();

    this.listener = listener;
    this.event = event;
    this.completeListener = completeListener;
  }

  public void run()
  {
    try
    {
      if (this.LOG.isDebugEnabled()) {
        this.LOG.debug("Running event created at: " + this.event.getCreationTime());
      }
      this.event.setTimeExecuted(System.currentTimeMillis());
      this.listener.serialEvent(this.event);
    } catch (Exception e) {
      this.LOG.error(e);
    }
    finally {
      this.completeListener.eventComplete(System.currentTimeMillis(), this);
    }
  }

  public long getEventCreationTime()
  {
    return this.creationTime;
  }
}