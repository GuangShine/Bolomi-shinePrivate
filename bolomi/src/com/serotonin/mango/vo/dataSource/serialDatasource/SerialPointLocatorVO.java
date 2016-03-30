package com.serotonin.mango.vo.dataSource.serialDatasource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.rt.dataSource.serial.SerialPointLocatorRT;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.vo.dataSource.AbstractPointLocatorVO;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;
import com.serotonin.util.SerializationHelper;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

public class SerialPointLocatorVO extends AbstractPointLocatorVO implements JsonSerializable{
	
	@Override
	public int getDataTypeId() {
		return dataTypeId;
	}
	 public LocalizableMessage getConfigurationDescription() {
	        return new LocalizableMessage("serial.point.configuration",pointIdentifier);
	    }
	/*@Override
	public TranslatableMessage getConfigurationDescription() {
		//TODO add the properties to this
		return new TranslatableMessage("serial.point.configuration",pointIdentifier);
	}*/

	@Override
	public boolean isSettable() {
		return true;
	}

	@Override
	 public PointLocatorRT createRuntime() {
        return new SerialPointLocatorRT(this);
    }

	@Override
	public void validate(DwrResponseI18n response) {
		if (pointIdentifier == null)
            response.addContextualMessage("pointIdentifier", "validate.invalidValue");	

		if (SerialDataSourceVO.isBlank(valueRegex))
            response.addContextualMessage("valueRegex", "validate.required");	
		try {
			Pattern.compile(valueRegex).matcher("").find(); // Validate the regex
		} catch (PatternSyntaxException e) {
			response.addContextualMessage("valueRegex", "serial.validate.badRegex", e.getMessage());
		}
		
		if(valueIndex < 0)
			response.addContextualMessage("valueIndex","validate.invalidValue");
		
		if (!DataTypes.CODES.isValidId(dataTypeId))
            response.addContextualMessage("dataTypeId", "validate.invalidValue");

	}

	@JsonRemoteProperty
	private String pointIdentifier; //Address or unique ID in message for this point
	@JsonRemoteProperty
	private String valueRegex;
	@JsonRemoteProperty
	private int valueIndex;
	private int dataTypeId = DataTypes.ALPHANUMERIC;
	
	public String getPointIdentifier() {
		return pointIdentifier;
	}

	public void setPointIdentifier(String pointIdentifier) {
		this.pointIdentifier = pointIdentifier;
	}
	
	public String getValueRegex() {
		return valueRegex;
	}

	public void setValueRegex(String valueRegex) {
		this.valueRegex = valueRegex;
	}

	public int getValueIndex() {
		return valueIndex;
	}

	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}

	public void setDataTypeId(int dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	@Override
	public void addProperties(List<LocalizableMessage> list) {
        AuditEventType.addPropertyMessage(list, "dsEdit.serial.pointIdentifier", pointIdentifier);
        AuditEventType.addPropertyMessage(list, "dsEdit.valueRegex", valueRegex);
        AuditEventType.addPropertyMessage(list, "dsEdit.valueIndex", valueIndex);
        AuditEventType.addDataTypeMessage(list, "dsEdit.pointDataType", dataTypeId);

	}

	@Override
	public void addPropertyChanges(List<LocalizableMessage> list, Object o) {
		SerialPointLocatorVO from = (SerialPointLocatorVO)o;
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.pointIdentifier", from.pointIdentifier, pointIdentifier);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.valueRegex", from.valueRegex, valueRegex);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.serial.valueIndex", from.valueIndex, valueIndex);
        AuditEventType.maybeAddDataTypeChangeMessage(list, "dsEdit.pointDataType", from.dataTypeId, dataTypeId);

	}
    //
    //
    // Serialization
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        SerializationHelper.writeSafeUTF(out, pointIdentifier);
        SerializationHelper.writeSafeUTF(out, valueRegex);
        out.writeInt(valueIndex);
        out.writeInt(dataTypeId);

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();
        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
        	pointIdentifier= SerializationHelper.readSafeUTF(in);
        	valueRegex= SerializationHelper.readSafeUTF(in);
        	valueIndex = in.readInt();
        	dataTypeId = in.readInt();
        }
    }

	@Override
	  public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
		Integer value = deserializeDataType(json, DataTypes.IMAGE);
        if (value != null)
            dataTypeId = value;
	}
	@Override
	public void jsonSerialize(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		super.serializeDataType(arg0);
	}

	/*@Override
	public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
		writeDataType(writer);
	}*/

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.dataSource.PointLocatorVO#asModel()
	 */
/*	@Override
	public PointLocatorModel<?> asModel() {
		return new SerialPointLocatorModel(this);
	}*/

	
	
}
