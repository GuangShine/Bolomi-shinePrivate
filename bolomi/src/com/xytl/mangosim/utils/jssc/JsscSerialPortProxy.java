package com.xytl.mangosim.utils.jssc;



import java.io.IOException;
import jssc.SerialPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsscSerialPortProxy extends SerialPortProxy
{
  Log LOG = LogFactory.getLog(JsscSerialPortProxy.class);
  private SerialPort port;
  private SerialPortOutputStream os;
  private SerialPortInputStream is;

  public JsscSerialPortProxy(SerialParameters serialParameters)
  {
    super(serialParameters.getCommPortId());
    this.parameters = serialParameters;
  }

  public byte[] readBytes(int i)
    throws SerialPortException
  {
    try
    {
      return this.port.readBytes(i);
    }
    catch (jssc.SerialPortException e) {
      throw new SerialPortException(e);
    }
  }

  public void writeInt(int arg0)
    throws SerialPortException
  {
    try
    {
      this.port.writeInt(arg0);
    }
    catch (jssc.SerialPortException e) {
      throw new SerialPortException(e);
    }
  }

  public void closeImpl()
    throws SerialPortException
  {
    Throwable ex = null;
    try
    {
      this.is.close();
    }
    catch (IOException e) {
      this.LOG.error(e);
      ex = e;
    }
    try {
      this.os.close();
    }
    catch (IOException e) {
      this.LOG.error(e);
      ex = e;
    }
    try {
      this.port.closePort();
    }
    catch (jssc.SerialPortException e) {
      this.LOG.error(e);
      ex = e;
    }

    if (ex != null)
      throw new SerialPortException(ex);
  }

  public void openImpl()
    throws SerialPortException
  {
    try
    {
      if (this.LOG.isDebugEnabled()) {
        this.LOG.debug("Opening Serial Port: " + this.parameters.getCommPortId());
      }
      this.port = new SerialPort(this.parameters.getCommPortId());

      this.port.openPort();
      this.port.setFlowControlMode(this.parameters.getFlowControlIn() | this.parameters.getFlowControlOut());
      this.port.setParams(this.parameters.getBaudRate(), this.parameters.getDataBits(), this.parameters.getStopBits(), this.parameters.getParity());

      this.is = new JsscSerialPortInputStream(this.port, this.listeners);
      this.os = new JsscSerialPortOutputStream(this.port);
    }
    catch (jssc.SerialPortException e)
    {
      throw new SerialPortException(e);
    }
  }

  public SerialPortInputStream getInputStream()
  {
    return this.is;
  }

  public SerialPortOutputStream getOutputStream()
  {
    return this.os;
  }
}
