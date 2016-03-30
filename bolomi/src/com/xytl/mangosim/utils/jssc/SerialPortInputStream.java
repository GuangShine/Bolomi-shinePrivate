package com.xytl.mangosim.utils.jssc;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SerialPortInputStream extends InputStream
{
  private final Log LOG = LogFactory.getLog(SerialPortInputStream.class);
  private final Object closeLock = new Object();
  private volatile boolean closed = false;

  public abstract int read()
    throws IOException;

  public abstract int available()
    throws IOException;

  public void close()
    throws IOException
  {
    if (this.LOG.isDebugEnabled())
      this.LOG.debug("Attempting Close of Serial Port Input Stream.");
    synchronized (this.closeLock) {
      if (this.closed) {
        return;
      }
      closeImpl();
      this.closed = true;
    }
    if (this.LOG.isDebugEnabled())
      this.LOG.debug("Closed Serial Port Input Stream.");
  }

  public abstract void closeImpl()
    throws IOException;

  public abstract int peek();
}