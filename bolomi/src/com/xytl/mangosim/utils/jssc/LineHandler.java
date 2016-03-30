package com.xytl.mangosim.utils.jssc;

public abstract interface LineHandler
{
  public abstract void handleLine(String paramString);

  public abstract void done();
}
