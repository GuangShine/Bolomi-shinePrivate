package com.xytl.mangosim.utils.jssc;


import com.serotonin.io.StreamUtils;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsscSerialPortInputStream extends SerialPortInputStream
  implements SerialPortEventListener, SerialPortProxyEventCompleteListener
{
  private final Log LOG = LogFactory.getLog(JsscSerialPortInputStream.class);
  protected LinkedBlockingQueue<Byte> dataStream;
  protected SerialPort port;
  protected List<SerialPortProxyEventListener> listeners;
  private BlockingQueue<SerialPortProxyEventTask> listenerTasks;
  private final int maxPoolSize = 20;

  public JsscSerialPortInputStream(SerialPort serialPort, List<SerialPortProxyEventListener> listeners)
    throws SerialPortException
  {
    this.listeners = listeners;
    this.dataStream = new LinkedBlockingQueue();

    this.port = serialPort;
    this.port.addEventListener(this, 1);

    getClass(); this.listenerTasks = new ArrayBlockingQueue(20);

    if (this.LOG.isDebugEnabled())
      this.LOG.debug("Creating Jssc Serial Port Input Stream for: " + serialPort.getPortName());
  }

  public int read()
    throws IOException
  {
    synchronized (this.dataStream) {
      try {
        if (this.dataStream.size() > 0) {
          return ((Byte)this.dataStream.take()).byteValue() & 0xFF;
        }
        return -1;
      }
      catch (InterruptedException e) {
        throw new IOException(e);
      }
    }
  }

  public int available() throws IOException
  {
    synchronized (this.dataStream) {
      return this.dataStream.size();
    }
  }

  public void closeImpl() throws IOException
  {
    try {
      this.port.removeEventListener();
    }
    catch (SerialPortException e) {
      throw new IOException(e);
    }
  }

  public int peek()
  {
    return ((Byte)this.dataStream.peek()).byteValue();
  }

  public void serialEvent(SerialPortEvent event)
  {
    SerialPortProxyEvent upstreamEvent;
    if (event.isRXCHAR()) {
      if (this.LOG.isDebugEnabled())
        this.LOG.debug("Serial Receive Event fired.");
      try
      {
        synchronized (this.dataStream) {
          byte[] buffer = this.port.readBytes();
          for (int i = 0; i < buffer.length; i++) {
            this.dataStream.put(Byte.valueOf(buffer[i]));
          }
          if (this.LOG.isDebugEnabled()) {
            this.LOG.debug("Recieved: " + StreamUtils.dumpHex(buffer, 0, buffer.length));
          }
        }
      }
      catch (Exception e)
      {
        this.LOG.error(e);
      }

      if (this.listeners.size() > 0)
      {
        upstreamEvent = new SerialPortProxyEvent(System.currentTimeMillis());
        for (SerialPortProxyEventListener listener : this.listeners) {
          SerialPortProxyEventTask task = new SerialPortProxyEventTask(listener, upstreamEvent, this);
          try {
            this.listenerTasks.add(task);
            task.start();
          } catch (IllegalStateException e) {
            this.LOG.warn("Serial Port Problem, Listener task queue full, data will be lost!", e);
          }
        }

      }

    }
    else if (this.LOG.isDebugEnabled()) {
      this.LOG.debug("Non RX Event Type Recieved: " + event.getEventType());
    }
  }

  public void eventComplete(long time, SerialPortProxyEventTask task)
  {
    this.listenerTasks.remove(task);
  }
}