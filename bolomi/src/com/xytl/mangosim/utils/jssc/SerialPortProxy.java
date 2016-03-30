package com.xytl.mangosim.utils.jssc;


import java.util.ArrayList;
import java.util.List;



public abstract class SerialPortProxy
{
  public static final int PARITY_NONE = 0;
  public static final int STOPBITS_1 = 1;
  public static final int DATABITS_8 = 8;
  public static final int FLOWCONTROL_NONE = 0;
  protected SerialParameters parameters;
  protected List<SerialPortProxyEventListener> listeners;
  private final String name;
  private final Object closeLock = new Object();
  private volatile boolean closed = true;

  public SerialPortProxy(String name)
  {
    this.name = name;
    this.listeners = new ArrayList();
  }

  public abstract byte[] readBytes(int paramInt)
    throws SerialPortException;

  public abstract void writeInt(int paramInt)
    throws SerialPortException;

  public void close()
    throws SerialPortException
  {
    synchronized (this.closeLock) {
      if (this.closed) {
        return;
      }
      closeImpl();
      this.closed = true;
    }
  }

  public abstract void closeImpl()
    throws SerialPortException;

  public void open()
    throws SerialPortException
  {
    synchronized (this.closeLock) {
      if (!this.closed) {
        throw new SerialPortException("Serial Port: " + this.name + " Already Open!");
      }
      openImpl();
      this.closed = false;
    }
  }

  public abstract void openImpl()
    throws SerialPortException;

  public abstract SerialPortInputStream getInputStream();

  public abstract SerialPortOutputStream getOutputStream();

  public SerialParameters getParameters()
  {
    return this.parameters;
  }

  public void setParameters(SerialParameters parameters) {
    this.parameters = parameters;
  }

  public void addEventListener(SerialPortProxyEventListener listener) {
    this.listeners.add(listener);
  }

  public void removeEventListener(SerialPortProxyEventListener listener) {
    this.listeners.remove(listener);
  }
}