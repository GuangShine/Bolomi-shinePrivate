package com.xytl.mangosim.utils.jssc;

import java.io.IOException;
import java.io.OutputStream;

public abstract class SerialPortOutputStream extends OutputStream
{
  public abstract void write(int paramInt)
    throws IOException;

  public abstract void flush();
}