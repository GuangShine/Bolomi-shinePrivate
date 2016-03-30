/*
    Mango - Open Source M2M - http://mango.serotoninsoftware.com
    Copyright (C) 2006-2011 Serotonin Software Technologies Inc.
    @author Matthew Lohbihler
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.mango.web.dwr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.IntValuePair;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonValue;
import com.serotonin.json.JsonWriter;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.web.dwr.beans.ImportTask;
import com.serotonin.web.dwr.DwrResponseI18n;

/**
 * @author Matthew Lohbihler
 */
public class DataSourceListDwr extends BaseDwr {
    public static final String DATA_SOURCES = "dataSources";
    public static final String DATA_POINTS = "dataPoints";
    public DwrResponseI18n init() {
        DwrResponseI18n response = new DwrResponseI18n();

        if (Common.getUser().isAdmin()) {
            List<IntValuePair> translatedTypes = new ArrayList<IntValuePair>();
            for (DataSourceVO.Type type : DataSourceVO.Type.values()) {
                // Allow customization settings to overwrite the default display value.
                boolean display = SystemSettingsDao.getBooleanValue(type.name()
                        + SystemSettingsDao.DATASOURCE_DISPLAY_SUFFIX, type.isDisplay());
                if (display)
                    translatedTypes.add(new IntValuePair(type.getId(), getMessage(type.getKey())));
            }
            response.addData("types", translatedTypes);
        }

        return response;
    }

    public Map<String, Object> toggleDataSource(int dataSourceId) {
        Permissions.ensureDataSourcePermission(Common.getUser(), dataSourceId);

        RuntimeManager runtimeManager = Common.ctx.getRuntimeManager();
        DataSourceVO<?> dataSource = runtimeManager.getDataSource(dataSourceId);
        Map<String, Object> result = new HashMap<String, Object>();

        dataSource.setEnabled(!dataSource.isEnabled());
        runtimeManager.saveDataSource(dataSource);

        result.put("enabled", dataSource.isEnabled());
        result.put("id", dataSourceId);
        return result;
    }

    public int deleteDataSource(int dataSourceId) {
        Permissions.ensureDataSourcePermission(Common.getUser(), dataSourceId);
        Common.ctx.getRuntimeManager().deleteDataSource(dataSourceId);
        return dataSourceId;
    }

    public DwrResponseI18n toggleDataPoint(int dataPointId) {
        DataPointVO dataPoint = new DataPointDao().getDataPoint(dataPointId);
        Permissions.ensureDataSourcePermission(Common.getUser(), dataPoint.getDataSourceId());

        RuntimeManager runtimeManager = Common.ctx.getRuntimeManager();
        dataPoint.setEnabled(!dataPoint.isEnabled());
        runtimeManager.saveDataPoint(dataPoint);

        DwrResponseI18n response = new DwrResponseI18n();
        response.addData("id", dataPointId);
        response.addData("enabled", dataPoint.isEnabled());
        return response;
    }

    public int copyDataSource(int dataSourceId) {
        Permissions.ensureDataSourcePermission(Common.getUser(), dataSourceId);
        int dsId = new DataSourceDao().copyDataSource(dataSourceId, getResourceBundle());
        new UserDao().populateUserPermissions(Common.getUser());
        return dsId;
    }
    public int copyDataPoint(int pointId,int datasourceId)
    {
    	DataPointDao  dataPointDao=new DataPointDao();
    	DataPointVO dataPoint=dataPointDao.getDataPoint(pointId);
    	DataPointVO dataPointCopy = dataPoint.copy();
        dataPointCopy.setId(Common.NEW_ID);
        dataPointCopy.setXid(dataPointDao.generateUniqueXid());
        dataPointCopy.setName(dataPoint.getName());
        dataPointCopy.setDataSourceId(datasourceId);
        dataPointCopy.setEnabled(dataPoint.isEnabled());
        dataPointCopy.getComments().clear();
        for (PointEventDetectorVO ped : dataPointCopy.getEventDetectors()) {
            ped.setId(Common.NEW_ID);
            ped.njbSetDataPoint(dataPointCopy);
        }
        dataPointDao.saveDataPoint(dataPointCopy);

        // Copy permissions
        dataPointDao.copyPermissions(dataPoint.getId(), dataPointCopy.getId());
        return dataPointCopy.getId();
    	
    }
    //new code
    public String createExportData(int dataSourceId)
    {
    	  Map<String, Object> data = new LinkedHashMap<String, Object>();
    	 if(dataSourceId!=-1){
		        data.put(DATA_SOURCES, new DataSourceDao().getDataSourcesById(dataSourceId));		        
		        data.put(DATA_POINTS, new DataPointDao().ExprotData(dataSourceId));
    	 }
    	 int prettyIndent=3;
    	 JsonWriter writer = new JsonWriter();
         writer.setPrettyIndent(prettyIndent);
         writer.setPrettyOutput(true);

         try {
             return writer.write(data);
         }
         catch (JsonException e) {
             throw new ShouldNeverHappenException(e);
         }
         catch (IOException e) {
             throw new ShouldNeverHappenException(e);
         }
    }
    
    public DwrResponseI18n importData(String data) {
        DwrResponseI18n response = new DwrResponseI18n();
        ResourceBundle bundle = getResourceBundle();

        User user = Common.getUser();
        Permissions.ensureAdmin(user);

        JsonReader reader = new JsonReader(data);
        try {
            JsonValue value = reader.inflate();
            if (value instanceof JsonObject) {
                JsonObject root = value.toJsonObject();
                ImportTask importTask = new ImportTask(reader, root, bundle, user);
                user.setImportTask(importTask);
                response.addData("importStarted", true);
            }
            else {
                response.addGenericMessage("emport.invalidImportData");
            }
        }
        catch (ClassCastException e) {
            response.addGenericMessage("emport.parseError", e.getMessage());
        }
        catch (LocalizableJsonException e) {
            response.addMessage(e.getMsg());
        }
        catch (JsonException e) {
            response.addGenericMessage("emport.parseError", e.getMessage());
        }

        return response;
    }

    public DwrResponseI18n importUpdate() {
        DwrResponseI18n response;
        User user = Common.getUser();
        ImportTask importTask = user.getImportTask();
        if (importTask != null) {
            response = importTask.getResponse();

            if (importTask.isCancelled()) {
                response.addData("cancelled", true);
                user.setImportTask(null);
            }
            else if (importTask.isCompleted()) {
                response.addData("complete", true);
                user.setImportTask(null);
            }
        }
        else {
            response = new DwrResponseI18n();
            response.addData("noImport", true);
        }
        return response;
    }

    
}

