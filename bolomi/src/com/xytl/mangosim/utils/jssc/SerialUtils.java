package com.xytl.mangosim.utils.jssc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import jssc.SerialNativeInterface;
import jssc.SerialPortList;

public class SerialUtils
{
  private static List<String> ownedPortIdentifiers = Collections.synchronizedList(new ArrayList());

  public static List<CommPortProxy> getCommPorts()
    throws CommPortConfigException
  {
    try
    {
      List ports = new LinkedList();
      String[] portNames;
      switch (SerialNativeInterface.getOsType()) {
      case 0:
        portNames = SerialPortList.getPortNames(Pattern.compile("(cu|ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm|ttyO)[0-9]{1,3}"));
        break;
      case 3:
        portNames = SerialPortList.getPortNames(Pattern.compile("(cu|tty)..*"));
        break;
      default:
        portNames = SerialPortList.getPortNames();
      }

      for (String portName : portNames) {
        CommPortIdentifier id = new CommPortIdentifier(portName, false);
        ports.add(new CommPortProxy(id));
      }

      return ports;
    }
    catch (UnsatisfiedLinkError e) {
      throw new CommPortConfigException(e.getMessage());
    } catch (NoClassDefFoundError e) {
    }
    throw new CommPortConfigException("Comm configuration error. Check that rxtx DLL or libraries have been correctly installed.");
  }

  public static SerialPortProxy openSerialPort(SerialParameters serialParameters)
    throws SerialPortException
  {
    SerialPortProxy serialPort = null;
    try
    {
      if (portOwned(serialParameters.getCommPortId())) {
        throw new SerialPortException("Port In Use: " + serialParameters.getCommPortId());
      }

      serialPort = new JsscSerialPortProxy(serialParameters);
      serialPort.open();

      synchronized (ownedPortIdentifiers) {
        ownedPortIdentifiers.add(serialPort.getParameters().getCommPortId());
      }
    }
    catch (Exception e)
    {
      if ((e instanceof SerialPortException))
        throw ((SerialPortException)e);
      throw new SerialPortException(e);
    }

    return serialPort;
  }

  public static boolean portOwned(String commPortId)
  {
    synchronized (ownedPortIdentifiers) {
      for (String identifier : ownedPortIdentifiers) {
        if (identifier.equals(commPortId))
          return true;
      }
      return false;
    }
  }

  public static void close(SerialPortProxy serialPort)
    throws SerialPortException
  {
    if (serialPort != null) {
      serialPort.close();

      synchronized (ownedPortIdentifiers) {
        ownedPortIdentifiers.remove(serialPort.getParameters().getCommPortId());
      }
    }
  }
}