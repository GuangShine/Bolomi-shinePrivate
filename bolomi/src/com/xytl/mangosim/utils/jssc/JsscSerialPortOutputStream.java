package com.xytl.mangosim.utils.jssc;



import java.io.IOException;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsscSerialPortOutputStream extends SerialPortOutputStream
{
  private final Log LOG = LogFactory.getLog(JsscSerialPortOutputStream.class);
  private SerialPort port;

  public JsscSerialPortOutputStream(SerialPort port)
  {
    this.port = port;
  }

  public void write(int arg0)
    throws IOException
  {
    try
    {
      byte b = (byte)arg0;
      if (this.LOG.isDebugEnabled())
        this.LOG.debug("Writing byte: " + String.format("%02x", new Object[] { Byte.valueOf(b) }));
      if ((this.port != null) && (this.port.isOpened()))
        this.port.writeByte(b);
    }
    catch (SerialPortException e)
    {
      throw new IOException(e);
    }
  }

  public void flush()
  {
    if (this.LOG.isDebugEnabled())
      this.LOG.debug("Called no-op flush...");
  }
}