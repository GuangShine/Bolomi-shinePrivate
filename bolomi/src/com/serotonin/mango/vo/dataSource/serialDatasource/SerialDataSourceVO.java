package com.serotonin.mango.vo.dataSource.serialDatasource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.rt.dataSource.DataSourceRT;
import com.serotonin.mango.rt.dataSource.serial.SerialDataSourceRT;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.util.ExportCodes;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.dataSource.PointLocatorVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO.Type;
import com.serotonin.mango.vo.event.EventTypeVO;
import com.serotonin.util.SerializationHelper;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

@JsonRemoteEntity
public  class SerialDataSourceVO   extends DataSourceVO<SerialDataSourceVO>  {
	 public static final Type TYPE = Type.SERIAL;
	  private static final ExportCodes EVENT_CODES = new ExportCodes();
	  static {
	        EVENT_CODES.addElement(SerialDataSourceRT.DATA_SOURCE_EXCEPTION_EVENT, "DATA_SOURCE_EXCEPTION");
	        EVENT_CODES.addElement(SerialDataSourceRT.POINT_READ_EXCEPTION_EVENT, "POINT_READ_EXCEPTION");
	        EVENT_CODES.addElement(SerialDataSourceRT.POINT_WRITE_EXCEPTION_EVENT, "POINT_WRITE_EXCEPTION");
	        EVENT_CODES.addElement(SerialDataSourceRT.POINT_READ_PATTERN_MISMATCH_EVENT, "POINT_READ_PATTERN_MISMATCH_EVENT");
	   }
	@JsonRemoteProperty
	    private String commPortId;
	  @JsonRemoteProperty
	    private int baudRate = 9600;
	  @JsonRemoteProperty
	    private int flowControlIn = 0;
	  @JsonRemoteProperty
	    private int flowControlOut = 0;
	  @JsonRemoteProperty
	    private int dataBits = 8;
	  @JsonRemoteProperty
	    private int stopBits = 1;
	  @JsonRemoteProperty
	    private int parity = 0;
	  @JsonRemoteProperty
	    private int readTimeout = 1000; //Timeout in ms
	  @JsonRemoteProperty
	    private boolean useTerminator = true;
	  @JsonRemoteProperty
	    private String messageTerminator;
	  @JsonRemoteProperty
	    private String messageRegex;
	  @JsonRemoteProperty
	    private int pointIdentifierIndex;
	  @JsonRemoteProperty
	    private boolean hex = false; //Is the setup in Hex Strings?
	  @JsonRemoteProperty
	    private boolean logIO = false;
	  @JsonRemoteProperty
	    private int maxMessageSize = 1024;
	  @JsonRemoteProperty
	    private float ioLogFileSizeMBytes = 1.0f; //1MB
	  @JsonRemoteProperty
	    private int maxHistoricalIOLogs = 1;
	  @Override
	    public LocalizableMessage getConnectionDescription() {
	        return new LocalizableMessage("serial.connection",this.commPortId);
	    }
/*		@Override
		public LocalizableMessage getConnectionDescription() {
			return new TranslatableMessage("serial.connection",this.commPortId);
		}
*/
		@Override
		public PointLocatorVO createPointLocator() {
			return new SerialPointLocatorVO();
		}

		@Override
		public DataSourceRT createDataSourceRT() {
			return new SerialDataSourceRT(this);
		}

		@Override
		public ExportCodes getEventCodes() {
			return EVENT_CODES;
		}

		@Override
		protected void addEventTypes(List<EventTypeVO> eventTypes) {
			eventTypes.add(createEventType(SerialDataSourceRT.DATA_SOURCE_EXCEPTION_EVENT, new LocalizableMessage(
	                "event.ds.dataSource")));
			eventTypes.add(createEventType(SerialDataSourceRT.POINT_READ_EXCEPTION_EVENT, new LocalizableMessage(
	                "event.ds.pointRead")));
			eventTypes.add(createEventType(SerialDataSourceRT.POINT_WRITE_EXCEPTION_EVENT, new LocalizableMessage(
	                "event.ds.pointWrite")));
			eventTypes.add(createEventType(SerialDataSourceRT.POINT_READ_PATTERN_MISMATCH_EVENT, new LocalizableMessage(
	                "event.serial.patternMismatchException")));
			
		}	

		
		public int getFlowControlMode() {
			return (this.getFlowControlIn() | this.getFlowControlOut());
		}

		public String getCommPortId() {
			return commPortId;
		}

		public void setCommPortId(String commPortId) {
			this.commPortId = commPortId;
		}

		public int getBaudRate() {
			return baudRate;
		}

		public void setBaudRate(int baudRate) {
			this.baudRate = baudRate;
		}

		public int getFlowControlIn() {
			return flowControlIn;
		}

		public void setFlowControlIn(int flowControlIn) {
			this.flowControlIn = flowControlIn;
		}

		public int getFlowControlOut() {
			return flowControlOut;
		}

		public void setFlowControlOut(int flowControlOut) {
			this.flowControlOut = flowControlOut;
		}

		public int getDataBits() {
			return dataBits;
		}

		public void setDataBits(int dataBits) {
			this.dataBits = dataBits;
		}

		public int getStopBits() {
			return stopBits;
		}

		public void setStopBits(int stopBits) {
			this.stopBits = stopBits;
		}

		public int getParity() {
			return parity;
		}

		public void setParity(int parity) {
			this.parity = parity;
		}
		
	    public int getReadTimeout() {
			return readTimeout;
		}

		public void setReadTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
		}
		
		public boolean getUseTerminator() {
			return useTerminator;
		}
		
		public void setUseTerminator(boolean useTerminator) {
			this.useTerminator = useTerminator;
		}

		public String getMessageTerminator() {
			return messageTerminator;
		}

		public void setMessageTerminator(String messageTerminator) {
			this.messageTerminator = messageTerminator;
		}

		public String getMessageRegex() {
			return messageRegex;
		}

		public void setMessageRegex(String messageRegex) {
			this.messageRegex = messageRegex;
		}

		public int getPointIdentifierIndex() {
			return pointIdentifierIndex;
		}

		public void setPointIdentifierIndex(int pointIdentifierIndex) {
			this.pointIdentifierIndex = pointIdentifierIndex;
		}
		
		public boolean isHex() {
			return hex;
		}

		public void setHex(boolean hex) {
			this.hex = hex;
		}

		public boolean isLogIO() {
			return logIO;
		}

		public void setLogIO(boolean logIO) {
			this.logIO = logIO;
		}
		
		public int getMaxMessageSize() {
			return maxMessageSize;
		}

		public void setMaxMessageSize(int maxMessageSize) {
			this.maxMessageSize = maxMessageSize;
		}
	    public float getIoLogFileSizeMBytes() {
			return ioLogFileSizeMBytes;
		}

		public void setIoLogFileSizeMBytes(float ioLogFileSizeMBytes) {
			this.ioLogFileSizeMBytes = ioLogFileSizeMBytes;
		}

		public int getMaxHistoricalIOLogs() {
			return maxHistoricalIOLogs;
		}

		public void setMaxHistoricalIOLogs(int maxHistoricalIOLogs) {
			this.maxHistoricalIOLogs = maxHistoricalIOLogs;
		}

		
	  /* public String getIoLogPath() {
	    	return new File(Common.getLogsDir(), SerialDataSourceRT.getIOLogFileName(getId())).getPath();
	    */
		
		
		@Override
	    public void validate(DwrResponseI18n response) {
	        super.validate(response);
	        if (isBlank(commPortId))
	            response.addContextualMessage("commPortId", "validate.required");
	     
	        if (baudRate <= 0)
	            response.addContextualMessage("baudRate", "validate.invalidValue");
	        if (!(flowControlIn == 0 || flowControlIn == 1 || flowControlIn == 4))
	            response.addContextualMessage("flowControlIn", "validate.invalidValue");
	        if (!(flowControlOut == 0 || flowControlOut == 2 || flowControlOut == 8))
	            response.addContextualMessage("flowControlOut", "validate.invalidValue");
	        if (dataBits < 5 || dataBits > 8)
	            response.addContextualMessage("dataBits", "validate.invalidValue");
	        if (stopBits < 1 || stopBits > 3)
	            response.addContextualMessage("stopBits", "validate.invalidValue");
	        if (parity < 0 || parity > 4)
	            response.addContextualMessage("parityBits", "validate.invalidValue");
	        
	        if(useTerminator) {
	        	if(messageTerminator.length() <= 0)
	        		response.addContextualMessage("messageTerminator", "validate.required");
	        	 if (isBlank(messageRegex))
	                 response.addContextualMessage("messageRegex", "validate.required");
	        	 if(pointIdentifierIndex < 0)
	             	response.addContextualMessage("pointIdentifierIndex", "validate.invalidValue");
	        	 
	        	 if(hex){
	        		 if(!messageTerminator.matches("[0-9A-Fa-f]+")){
	        			 response.addContextualMessage("messageTerminator", "serial.validate.notHex");
	        		 }
	        	 }
	        	 
	        }
	        
	        if(readTimeout <= 0)
	        	response.addContextualMessage("readTimeout","validate.greaterThanZero");        
	        
	        if(maxMessageSize <= 0){
	        	response.addContextualMessage("maxMessageSize","validate.greaterThanZero"); 
	        }
	        
	        if (ioLogFileSizeMBytes <= 0)
	            response.addContextualMessage("ioLogFileSizeMBytes", "validate.greaterThanZero");
	        if (maxHistoricalIOLogs <= 0)
	            response.addContextualMessage("maxHistoricalIOLogs", "validate.greaterThanZero");        

	     }

	    @Override
	    protected void addPropertiesImpl(List<LocalizableMessage> list) {
	      
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.port", commPortId);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.baud", baudRate);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.flowControlIn", flowControlIn);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.flowControlOut", flowControlOut);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.dataBits", dataBits);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.stopBits", stopBits);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.parity", parity);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.messageTerminator", messageTerminator);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.readTimeout", readTimeout);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.messageRegex", messageRegex);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.pointIdentifierIndex", pointIdentifierIndex);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.useTerminator", useTerminator);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.hex", hex);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.logIO", logIO);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.maxMessageSize", maxMessageSize);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.logIOFileSize", ioLogFileSizeMBytes);
	        AuditEventType.addPropertyMessage(list, "dsEdit.serial.logIOFiles", maxHistoricalIOLogs);
	        
	    }

	    @Override
	    protected void addPropertyChangesImpl(List<LocalizableMessage> list, SerialDataSourceVO from) {
	    	
	    	AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.port", from.commPortId, commPortId);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.baud", from.baudRate, baudRate);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.flowIn", from.flowControlIn,
	                flowControlIn);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.flowOut", from.flowControlOut,
	                flowControlOut);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.dataBits", from.dataBits, dataBits);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.stopBits", from.stopBits, stopBits);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.parity", from.parity, parity);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.messageTerminator", from.messageTerminator, messageTerminator);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.readTimeout", from.readTimeout, readTimeout);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.messageRegex", from.messageRegex, messageRegex);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.pointIdentifierIndex", from.pointIdentifierIndex, pointIdentifierIndex);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.useTerminator", from.useTerminator, useTerminator);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.hex", from.hex, hex);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.logIO", from.logIO, logIO);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.maxMessageSize", from.maxMessageSize, maxMessageSize);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.logIOFileSize", from.ioLogFileSizeMBytes, ioLogFileSizeMBytes);
	        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.logIOFiles", from.maxHistoricalIOLogs, maxHistoricalIOLogs);

	    }

	    //
	    // /
	    // / Serialization
	    // /
	    //
	    private static final long serialVersionUID = -1;
	    private static final int version = 4;

	    private void writeObject(ObjectOutputStream out) throws IOException {
	        out.writeInt(version);
	        SerializationHelper.writeSafeUTF(out, commPortId);
	        out.writeInt(baudRate);
	        out.writeInt(flowControlIn);
	        out.writeInt(flowControlOut);
	        out.writeInt(dataBits);
	        out.writeInt(stopBits);
	        out.writeInt(parity);
	        SerializationHelper.writeSafeUTF(out, messageTerminator);
	        out.writeInt(readTimeout);
	        SerializationHelper.writeSafeUTF(out, messageRegex);
	        out.writeInt(pointIdentifierIndex);
	        out.writeBoolean(useTerminator);
	        out.writeBoolean(hex);
	        out.writeBoolean(logIO);
	        out.writeInt(maxMessageSize);
	        out.writeFloat(ioLogFileSizeMBytes);
	        out.writeInt(maxHistoricalIOLogs);
	    }

	    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	        int ver = in.readInt();

	        // Switch on the version of the class so that version changes can be elegantly handled.
	        if (ver == 1) {

	            commPortId = SerializationHelper.readSafeUTF(in);
	            baudRate = in.readInt();
	            flowControlIn = in.readInt();
	            flowControlOut = in.readInt();
	            dataBits = in.readInt();
	            stopBits = in.readInt();
	            parity = in.readInt();
	            messageTerminator = StringEscapeUtils.unescapeJava(SerializationHelper.readSafeUTF(in));
	            readTimeout = in.readInt();
	            messageRegex = SerializationHelper.readSafeUTF(in);
	            pointIdentifierIndex = in.readInt();
	            useTerminator = true;
	            hex = false;
	            logIO = false;
	            maxMessageSize = 1024;
	            ioLogFileSizeMBytes = 1;
	            maxHistoricalIOLogs = 1;
	        }
	        if (ver == 2) {

	            commPortId = SerializationHelper.readSafeUTF(in);
	            baudRate = in.readInt();
	            flowControlIn = in.readInt();
	            flowControlOut = in.readInt();
	            dataBits = in.readInt();
	            stopBits = in.readInt();
	            parity = in.readInt();
	            messageTerminator = SerializationHelper.readSafeUTF(in);
	            readTimeout = in.readInt();
	            messageRegex = SerializationHelper.readSafeUTF(in);
	            pointIdentifierIndex = in.readInt();
	            useTerminator = true;
	            hex = false;
	            logIO = false;
	            maxMessageSize = 1024;
	            ioLogFileSizeMBytes = 1;
	            maxHistoricalIOLogs = 1;
	        }
	        if (ver == 3) {

	            commPortId = SerializationHelper.readSafeUTF(in);
	            baudRate = in.readInt();
	            flowControlIn = in.readInt();
	            flowControlOut = in.readInt();
	            dataBits = in.readInt();
	            stopBits = in.readInt();
	            parity = in.readInt();
	            messageTerminator = SerializationHelper.readSafeUTF(in);
	            readTimeout = in.readInt();
	            messageRegex = SerializationHelper.readSafeUTF(in);
	            pointIdentifierIndex = in.readInt();
	            useTerminator = in.readBoolean();
	            hex = false;
	            logIO = false;
	            maxMessageSize = 1024;
	            ioLogFileSizeMBytes = 1;
	            maxHistoricalIOLogs = 1;
	        }
	        if(ver == 4){

	            commPortId = SerializationHelper.readSafeUTF(in);
	            baudRate = in.readInt();
	            flowControlIn = in.readInt();
	            flowControlOut = in.readInt();
	            dataBits = in.readInt();
	            stopBits = in.readInt();
	            parity = in.readInt();
	            messageTerminator = SerializationHelper.readSafeUTF(in);
	            readTimeout = in.readInt();
	            messageRegex = SerializationHelper.readSafeUTF(in);
	            pointIdentifierIndex = in.readInt();
	            useTerminator = in.readBoolean();
	            hex = in.readBoolean();
	            logIO = in.readBoolean();
	            maxMessageSize = in.readInt();
	            ioLogFileSizeMBytes = in.readFloat();
	            maxHistoricalIOLogs = in.readInt();
	        }
	    }

	    /*@Override
	    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
	        super.jsonWrite(writer);
	    }*/

	    @Override
	    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
	       // super.jsonRead(reader, jsonObject);
	        super.jsonDeserialize(reader, json);	     
	    }
	    @Override
	    public void jsonSerialize(Map<String, Object> map) {
	        super.jsonSerialize(map);	     
	    }
		public static boolean isBlank(CharSequence cs) {
			int strLen;
			if ((cs == null) || ((strLen = cs.length()) == 0))
				return true;

			for (int i = 0; i < strLen; ++i) {
				if (!(Character.isWhitespace(cs.charAt(i)))) {
					return false;
				}
			}
			return true;
		}
		@Override
		public com.serotonin.mango.vo.dataSource.DataSourceVO.Type getType() {
			// TODO Auto-generated method stub
			return TYPE;
		}


		/* (non-Javadoc)
		 * @see com.serotonin.m2m2.vo.dataSource.DataSourceVO#asModel()
		 */
	/*	@Override
		public AbstractDataSourceModel<SerialDataSourceVO> asModel() {
			return new SerialDataSourceModel(this);
		}*/
}
