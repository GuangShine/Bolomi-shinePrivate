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
package com.serotonin.mango.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.mango.Common;

/**
 * @author Matthew Lohbihler
 */
abstract public class BasePooledAccess extends DatabaseAccess {
    private final Log log = LogFactory.getLog(BasePooledAccess.class);
    protected BasicDataSource dataSource;

    public BasePooledAccess(ServletContext ctx) {
        super(ctx);
    }

    @Override
    protected void initializeImpl(String propertyPrefix) {
        log.info("Initializing pooled connection manager");
        log.info("Initializing pooled connection manager2");
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(getDriverClassName());
        dataSource.setUrl(getUrl(propertyPrefix));
        dataSource.setUsername(Common.getEnvironmentProfile().getString(propertyPrefix + "db.username"));
        dataSource.setPassword(getDatabasePassword(propertyPrefix));
        dataSource.setMaxActive(Common.getEnvironmentProfile().getInt(propertyPrefix + "db.pool.maxActive", 10));
        dataSource.setMaxIdle(Common.getEnvironmentProfile().getInt(propertyPrefix + "db.pool.maxIdle", 10));
    }

    protected String getUrl(String propertyPrefix) {
        return Common.getEnvironmentProfile().getString(propertyPrefix + "db.url");
    }

    abstract protected String getDriverClassName();

    @Override
    public void runScript(String[] script, OutputStream out) {
        ExtendedJdbcTemplate ejt = new ExtendedJdbcTemplate();
        ejt.setDataSource(dataSource);

        StringBuilder statement = new StringBuilder();

        for (String line : script) {
            // Trim whitespace
            line = line.trim();

            // Skip comments
            if (line.startsWith("--"))
                continue;

            statement.append(line);
            statement.append(" ");
            if (line.endsWith(";")) {
                // Execute the statement
                ejt.execute(statement.toString());
                statement.delete(0, statement.length() - 1);
            }
        }
    }

    protected void createSchema(String scriptFile) {
        BufferedReader in = new BufferedReader(new InputStreamReader(ctx.getResourceAsStream(scriptFile)));

        List<String> lines = new ArrayList<String>();
        try {
            String line;
            while ((line = in.readLine()) != null)
                lines.add(line);
            lines.add("create table dataSources_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime (  id int not null,  xid varchar(50) not null,  name varchar(40) not null,  dataSourceType int not null,  data longblob not null,  rtdata longblob,  mangoCode varchar(50),  primary key (id)) engine=InnoDB;");
            lines.add("alter table dataSources_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime add constraint dataSourcesUn1 unique (xid);");
            lines.add("create table dataSourceUsers (  dataSourceId int not null,  userId int not null) engine=InnoDB;");
            lines.add("alter table dataSourceUsers add constraint dataSourceUsersFk1 foreign key (dataSourceId) references dataSources_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime(id);");
            lines.add("alter table dataSourceUsers add constraint dataSourceUsersFk2 foreign key (userId) references users(id) on delete cascade;");
            lines.add("create table maintenanceEvents (  id int not null auto_increment,  xid varchar(50) not null,  dataSourceId int not null,  alias varchar(255),  alarmLevel int not null,  scheduleType int not null,  disabled char(1) not null,  activeYear int,  activeMonth int,  activeDay int,  activeHour int,  activeMinute int,  activeSecond int,  activeCron varchar(25),  inactiveYear int,  inactiveMonth int,  inactiveDay int,  inactiveHour int,  inactiveMinute int,  inactiveSecond int,  inactiveCron varchar(25),  primary key (id)) engine=InnoDB;");
            lines.add("alter table maintenanceEvents add constraint maintenanceEventsUn1 unique (xid);");
            lines.add("alter table maintenanceEvents add constraint maintenanceEventsFk1 foreign key (dataSourceId) references dataSources_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime(id);");
            lines.add("create table dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime (  id int not null,  xid varchar(50) not null,  dataSourceId int not null,  data longblob not null,  mangoCode varchar(50), pointname varchar(255) , primary key (id)) engine=InnoDB;");
            lines.add("alter table dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime add constraint dataPointsUn1 unique (xid);");
            lines.add("alter table dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime add constraint dataPointsFk1 foreign key (dataSourceId) references dataSources_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime(id);");
            lines.add("create table dataPointUsers (  dataPointId int not null,  userId int not null,  permission int not null) engine=InnoDB;");
            lines.add("alter table dataPointUsers add constraint dataPointUsersFk1 foreign key (dataPointId) references dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime(id);");
            lines.add("alter table dataPointUsers add constraint dataPointUsersFk2 foreign key (userId) references users(id) on delete cascade;");	
            lines.add("create table pointValues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime (  id int  not null auto_increment,  dataPointId int not null,  dataType int not null,  pointValue double,  ts bigint not null,  primary key (id)) engine=InnoDB;");        
            lines.add("alter table pointValues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime add constraint pointValuesFk1 foreign key (dataPointId) references dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime(id) on delete cascade;");
            lines.add("create index pointValuesIdx1 on pointValues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime (ts, dataPointId);");
            lines.add("create index pointValuesIdx2 on pointValues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime (dataPointId, ts);");
            lines.add("create table pointValueAnnotations (  pointValueId int not null,  textPointValueShort varchar(128),  textPointValueLong longtext,  sourceType smallint,  sourceId int) engine=InnoDB;");
            lines.add("alter table pointValueAnnotations add constraint pointValueAnnotationsFk1 foreign key (pointValueId)   references pointValues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime(id) on delete cascade;");
            lines.add("create table events_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime (  id varchar(255) not null,  typeId int not null,  typeRef1 int not null,  typeRef2 int not null,  activeTs bigint not null,  rtnApplicable char(1) not null,  rtnTs bigint,  rtnCause int,  alarmLevel int not null,  message longtext,  ackTs bigint,  ackUserId int,  alternateAckSource int,  primary key (id)) engine=InnoDB;");
            lines.add("alter table events_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime add constraint eventsFk1 foreign key (ackUserId) references users(id);");
            lines.add("create table userEvents (  eventId varchar(255) not null,  userId int not null,  silenced char(1) not null,  primary key (eventId, userId)) engine=InnoDB;");
            lines.add("alter table userEvents add constraint userEventsFk1 foreign key (eventId) references events_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime(id) on delete cascade;");
            lines.add("alter table userEvents add constraint userEventsFk2 foreign key (userId) references users(id);");         
            lines.add("alter table watchListPoints add constraint watchListPointsFk2 foreign key (dataPointId) references dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime(id);");
            lines.add("alter table pointEventDetectors add constraint pointEventDetectorsFk1 foreign key (dataPointId) references dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime(id);");
            lines.add("create table publishDatapointLK ( datapointXid varchar(32) not null,publishXid varchar(32) not null,datapointName varchar(256) not null) engine=InnoDB;");
            lines.add("create table eventinfo (id varchar(128) not null,message varchar(2056) not null,ts varchar(32) not null,primary key (id))engine=InnoDB;");
            String[] script = new String[lines.size()];
            lines.toArray(script);
            
            runScript(script, null);
        } 
        catch (IOException ioe) {
            throw new ShouldNeverHappenException(ioe);
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ioe) {
                log.warn("", ioe);
            }
        }
    }

    @Override
    public void terminate() {
        log.info("Stopping database");
        try {
            dataSource.close();
        }
        catch (SQLException e) {
            log.warn("", e);
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public File getDataDirectory() {
        return null;
    }
}
