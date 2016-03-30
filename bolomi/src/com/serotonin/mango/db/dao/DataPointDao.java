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
package com.serotonin.mango.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.IntValuePair;
import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.util.PointCode;
import com.serotonin.mango.vo.DataPointExtendedNameComparator;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.PageVO;
import com.serotonin.mango.vo.UserComment;
import com.serotonin.mango.vo.bean.PointHistoryCount;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.mango.vo.hierarchy.PointFolder;
import com.serotonin.mango.vo.hierarchy.PointHierarchy;
import com.serotonin.mango.vo.hierarchy.PointHierarchyEventDispatcher;
import com.serotonin.mango.vo.link.PointLinkVO;
import com.serotonin.util.SerializationHelper;
import com.serotonin.util.Tuple;

public class DataPointDao extends BaseDao {
	PageVO page=new PageVO();
    public DataPointDao() {
        super();
    }

    public DataPointDao(DataSource dataSource) {
        super(dataSource);
    }

    //
    //
    // Data Points
    //
    public String generateUniqueXid() {
        return generateUniqueXid(DataPointVO.XID_PREFIX, "dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime");
    }

    public boolean isXidUnique(String xid, int excludeId) {
        return isXidUnique(xid, excludeId, "dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime");
    }

    public String getExtendedPointName(int dataPointId) {
        DataPointVO vo = getDataPoint(dataPointId);
        if (vo == null)
            return "?";
        return vo.getExtendedName();        
    }

    private static final String DATA_POINT_SELECT = "select dp.id, dp.xid, dp.dataSourceId, dp.data, ds.name, " //
            + "ds.xid, ds.dataSourceType " //
            + "from dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime dp join dataSources_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime ds on ds.id = dp.dataSourceId where dp.mangocode='"+Common.getEnvironmentProfile().getString("db.mangocode")+"'";
    //new code
    public int getDataPointsCount(int dataSourceId)
    {
    	   return ejt.queryForInt("select count(*) from dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime where datasourceid=? and mangocode=?",
                   new Object[] { dataSourceId,Common.getEnvironmentProfile().getString("db.mangocode") }); 

    }
    public List<DataPointVO> getDataPoints(Comparator<DataPointVO> comparator, boolean includeRelationalData) {
        List<DataPointVO> dps = query(DATA_POINT_SELECT, new DataPointRowMapper());
        if (includeRelationalData)
            setRelationalData(dps);
        if (comparator != null)
            Collections.sort(dps, comparator);
        return dps;
    }
    //new code
   /* public List PageUp(int dataSourceId,Comparator<DataPointVO> comparator,int nowpager,int pageContent,int pageSize)
    {PageVO page=new PageVO();
    	this.defaultInfo(dataSourceId,comparator);  
        page.setNowPage(nowpager);  
        page.setPageSize(pageSize);  
        page.setPageCount(pageContent);
        if(nowpager<=0)  
        {  
            page.setNowPage(1);  
        }  
        int startLine = (page.getNowPage()*page.getPageSize())-page.getPageSize(); //璁剧疆璧峰琛�
    	List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=? limit "+startLine+","+page.getPageSize(), new Object[] { dataSourceId },
                new DataPointRowMapper());
    	List pointAndPage = new ArrayList();
    	pointAndPage.add(0, dps);
    	pointAndPage.add(1,page);
    	return pointAndPage;
    }*/
    public int getInfoCount(int dataSourceId)  
    {  
    	int infoCount=0;
    	List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=?", new Object[] { dataSourceId },
                new DataPointRowMapper());
    	infoCount=dps.size();
    	return infoCount;
    }
    public List selectInfo(int dataSourceId, Comparator<DataPointVO> comparator,int pagePageSize)
    {
    	this.defaultInfo(dataSourceId, comparator);
		page.setPageSize(pagePageSize);
		if(pagePageSize>=page.getInfoCount())
		{
			page.setPageCount(page.getInfoCount()/page.getPageSize());
		}else
		{
			page.setPageCount(page.getInfoCount()/page.getPageSize()+1);
		}
		List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=? limit 0,"+page.getPageSize(), new Object[] { dataSourceId },
                new DataPointRowMapper());
    	List pointAndPage = new ArrayList();
    	List<PageVO> pages=new ArrayList<PageVO>();
    	pages.add(page);
    	//pages.add(new PageVO(page.getInfoCount(),page.getNowPage(),page.getPageSize(),page.getPageCount()));
    	pointAndPage.add(0, dps);
    	pointAndPage.add(1,pages);
    	return pointAndPage;
    }
    public List goInfo(int dataSourceId, Comparator<DataPointVO> comparator,int nowpager,int pageContent,int pageSizeSize)
   	{
    	this.defaultInfo(dataSourceId,comparator);
		page.setNowPage(nowpager);
		page.setPageSize(pageSizeSize);
		page.setPageCount(pageContent);
		if(nowpager>page.getPageCount())
		{
			page.setNowPage(page.getPageCount());  //濡傛灉璇锋眰鐨勯〉闈㈠ぇ浜庢�椤垫暟
		}
		int startLine = page.getNowPage()*page.getPageSize()-page.getPageSize(); //璁剧疆璧峰琛�
		List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=? limit "+startLine+","+page.getPageSize(), new Object[] { dataSourceId },
                new DataPointRowMapper());
    	List pointAndPage = new ArrayList();
    	List<PageVO> pages=new ArrayList<PageVO>();
    	pages.add(page);
    	//pages.add(new PageVO(page.getInfoCount(),page.getNowPage(),page.getPageSize(),page.getPageCount()));
    	pointAndPage.add(0, dps);
    	pointAndPage.add(1,pages);
    	return pointAndPage;
   	}
    
    public List upInfo(int dataSourceId, Comparator<DataPointVO> comparator,int nowpager,int pageContent,int pageSizeSize)
	{

    	this.defaultInfo(dataSourceId,comparator);
		page.setNowPage(nowpager);
		page.setPageSize(pageSizeSize);
		page.setPageCount(pageContent);
		if(nowpager<=0)
		{
			page.setNowPage(1);  //濡傛灉璇锋眰鐨勯〉闈㈠ぇ浜庢�椤垫暟
		}
		int startLine = (page.getNowPage()*page.getPageSize())-page.getPageSize(); //璁剧疆璧峰琛�
		List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=? limit "+startLine+","+page.getPageSize(), new Object[] { dataSourceId },
                new DataPointRowMapper());
    	List pointAndPage = new ArrayList();
    	List<PageVO> pages=new ArrayList<PageVO>();
    	pages.add(page);
    	//pages.add(new PageVO(page.getInfoCount(),page.getNowPage(),page.getPageSize(),page.getPageCount()));
    	pointAndPage.add(0, dps);
    	pointAndPage.add(1,pages);
    	return pointAndPage;
	}
    
    public List nextInfo(int dataSourceId, Comparator<DataPointVO> comparator,int nowpager,int pageContent,int pageSizeSize)
	{
    	this.defaultInfo(dataSourceId,comparator);
		page.setNowPage(nowpager);
		page.setPageSize(pageSizeSize);
		page.setPageCount(pageContent);
		if(nowpager>page.getPageCount())
		{
			page.setNowPage(page.getPageCount());  //濡傛灉璇锋眰鐨勯〉闈㈠ぇ浜庢�椤垫暟
		}
		int startLine = (page.getNowPage()-1)*page.getPageSize(); //璁剧疆璧峰琛�
		List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=? limit "+startLine+","+page.getPageSize(), new Object[] { dataSourceId },
                new DataPointRowMapper());
    	List pointAndPage = new ArrayList();
    	List<PageVO> pages=new ArrayList<PageVO>();
    	pages.add(page);
    	//pages.add(new PageVO(page.getInfoCount(),page.getNowPage(),page.getPageSize(),page.getPageCount()));
    	pointAndPage.add(0, dps);
    	pointAndPage.add(1,pages);
    	return pointAndPage;
	}
    public List defaultInfo(int dataSourceId, Comparator<DataPointVO> comparator)
    {	int nowPage = 1;  
	    if (nowPage < 1)  
	    {  
	        nowPage =1;   
	    }
    	/*PageVO page = new PageVO(this.getInfoCount(dataSourceId),nowPage,2,this.getInfoCount(dataSourceId)/2+1);    	
    	*/
        page.setInfoCount(this.getInfoCount(dataSourceId));
        page.setNowPage(nowPage);
        page.setPageSize(5);
        page.setPageCount((this.getInfoCount(dataSourceId)+page.getPageSize()-1)/page.getPageSize());
        
    	List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=? limit 0,"+page.getPageSize(), new Object[] { dataSourceId },
                new DataPointRowMapper());
    	 setRelationalData(dps);
         if (comparator != null)
             Collections.sort(dps, comparator);
    	List pointAndPage = new ArrayList();
    	List<PageVO> pages=new ArrayList<PageVO>();
    	pages.add(page);
    	//pages.add(new PageVO(page.getInfoCount(),page.getNowPage(),page.getPageSize(),page.getPageCount()));
    	pointAndPage.add(0, dps);
    	pointAndPage.add(1,pages);
    	return pointAndPage;
    }   
 

    public List<DataPointVO> getDataPoints(int dataSourceId, Comparator<DataPointVO> comparator) {
        List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=?", new Object[] { dataSourceId },
                new DataPointRowMapper());
        setRelationalData(dps);
        if (comparator != null)
            Collections.sort(dps, comparator);
        return dps;
    }
    public List<DataPointVO> ExprotData(int dataSourceId)
    {
    	 List<DataPointVO> dps = query(DATA_POINT_SELECT + " and dp.dataSourceId=?", new Object[] { dataSourceId },
                 new DataPointRowMapper());
         setRelationalData(dps);
         return dps;
    }

    public DataPointVO getDataPoint(int id) {
        DataPointVO dp = queryForObject(DATA_POINT_SELECT + " and dp.id=?", new Object[] { id },
                new DataPointRowMapper(), null);
        setRelationalData(dp);
        return dp;
    }

    public DataPointVO getDataPoint(String xid) {
        DataPointVO dp = queryForObject(DATA_POINT_SELECT + " and dp.xid=?", new Object[] { xid },
                new DataPointRowMapper(), null);
        setRelationalData(dp);
        return dp;
    }

    class DataPointRowMapper implements GenericRowMapper<DataPointVO> {
        public DataPointVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataPointVO dp;
            try {
                dp = (DataPointVO) SerializationHelper.readObject(rs.getBlob(4).getBinaryStream());
            }
            catch (ShouldNeverHappenException e) {
                dp = new DataPointVO();
                dp.setName("Point configuration lost. Please recreate.");
                dp.defaultTextRenderer();
            }
            dp.setId(rs.getInt(1));
            dp.setXid(rs.getString(2));
            dp.setDataSourceId(rs.getInt(3));

            // Data source information.
            dp.setDataSourceName(rs.getString(5));
            dp.setDataSourceXid(rs.getString(6));
            dp.setDataSourceTypeId(rs.getInt(7));

            // The spinwave changes were not correctly implemented, so we need to handle potential errors here.
            if (dp.getPointLocator() == null) {
                // Use the data source tpe id to determine what type of locator is needed.
                dp.setPointLocator(new DataSourceDao().getDataSource(dp.getDataSourceId()).createPointLocator());
            }

            return dp;
        }
    }

    private void setRelationalData(List<DataPointVO> dps) {
        for (DataPointVO dp : dps)
            setRelationalData(dp);
    }

    private void setRelationalData(DataPointVO dp) {
        if (dp == null)
            return;
        setEventDetectors(dp);
        setPointComments(dp);
    }

    public void saveDataPoint(final DataPointVO dp) {
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                // Decide whether to insert or update.
                if (dp.getId() == Common.NEW_ID) {
                    insertDataPoint(dp);
                    // Reset the point hierarchy so that the new point gets included.
                    cachedPointHierarchy = null;
                }
                else
                    updateDataPoint(dp);
            }
        });
    }

    void insertDataPoint(final DataPointVO dp) {
        // Create a default text renderer
        if (dp.getTextRenderer() == null)
            dp.defaultTextRenderer();
        String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
        // Insert the main data point record.
        PointCode pc=new PointCode();
        int pointid=Integer.valueOf(mangocode+pc.get_code("point"));
        ejt.update("insert into dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime (id,xid, dataSourceId, data,mangoCode,pointname) values (?,?,?,?,?,?)", new Object[] {pointid,
                dp.getXid(), dp.getDataSourceId(), SerializationHelper.writeObject(dp), mangocode,dp.getName()}, new int[] { Types.INTEGER,Types.VARCHAR,
                Types.INTEGER, Types.BLOB,Types.VARCHAR,Types.VARCHAR });
        dp.setId(pointid);
        
        // Save the relational information.
        saveEventDetectors(dp);
        pc.refresh_code("point");
        AuditEventType.raiseAddedEvent(AuditEventType.TYPE_DATA_POINT, dp);
        
    }

    void updateDataPoint(final DataPointVO dp) {
        DataPointVO old = getDataPoint(dp.getId());

        if (old.getPointLocator().getDataTypeId() != dp.getPointLocator().getDataTypeId())
            // Delete any point values where data type doesn't match the vo, just in case the data type was changed.
            // Only do this if the data type has actually changed because it is just really slow if the database is
            // big or busy.
            new PointValueDao().deletePointValuesWithMismatchedType(dp.getId(), dp.getPointLocator().getDataTypeId());

        // Save the VO information.
        updateDataPointShallow(dp);

        AuditEventType.raiseChangedEvent(AuditEventType.TYPE_DATA_POINT, old, dp);

        // Save the relational information.
        saveEventDetectors(dp);
    }

    public void updateDataPointShallow(final DataPointVO dp) {
    	String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
        ejt.update("update dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime set xid=?, data=?, mangocode=?,pointname=? where id=?",
                new Object[] { dp.getXid(), SerializationHelper.writeObject(dp), mangocode,dp.getName(),dp.getId() }, new int[] {
                        Types.VARCHAR, Types.BLOB,Types.VARCHAR,Types.VARCHAR, Types.INTEGER });
    }

    public void deleteDataPoints(final int dataSourceId) {
        List<DataPointVO> old = getDataPoints(dataSourceId, null);
        for (DataPointVO dp : old)
            beforePointDelete(dp.getId());

        for (DataPointVO dp : old)
            deletePointHistory(dp.getId());

        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @SuppressWarnings("synthetic-access")
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<Integer> pointIds = queryForList("select id from dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime where dataSourceId=? and mangocode='"+Common.getEnvironmentProfile().getString("db.mangocode")+"'",
                        new Object[] { dataSourceId }, Integer.class);
                if (pointIds.size() > 0)
                    deleteDataPointImpl(createDelimitedList(new HashSet<Integer>(pointIds), ",", null));
            }
        });

        for (DataPointVO dp : old)
            AuditEventType.raiseDeletedEvent(AuditEventType.TYPE_DATA_POINT, dp);
    }

    public void deleteDataPoint(final int dataPointId) {
        DataPointVO dp = getDataPoint(dataPointId);
        if (dp != null) {
            beforePointDelete(dataPointId);
            deletePointHistory(dataPointId);
            getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    deleteDataPointImpl(Integer.toString(dataPointId));
                }
            });

            AuditEventType.raiseDeletedEvent(AuditEventType.TYPE_DATA_POINT, dp);
        }
    }

    private void beforePointDelete(int dataPointId) {
        for (PointLinkVO link : new PointLinkDao().getPointLinksForPoint(dataPointId))
            Common.ctx.getRuntimeManager().deletePointLink(link.getId());
    }

    void deletePointHistory(int dataPointId) {
        Object[] p = new Object[] { dataPointId };
        
        long min = ejt.queryForLong("select min(ts) from pointvalues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime where dataPointId=?", p);
        long max = ejt.queryForLong("select max(ts) from pointvalues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime where dataPointId=?", p);
        deletePointHistory(dataPointId, min, max);
    }

    void deletePointHistory(int dataPointId, long min, long max) {
        while (true) {
            try {
                ejt.update("delete from pointvalues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_realtime where dataPointId=? and ts <= ?", new Object[] { dataPointId, max });
                break;
            }
            catch (UncategorizedSQLException e) {
                if ("The total number of locks exceeds the lock table size".equals(e.getSQLException().getMessage())) {
                    long mid = (min + max) >> 1;
                    deletePointHistory(dataPointId, min, mid);
                    min = mid;
                }
                else
                    throw e;
            }
        }
    }

    void deleteDataPointImpl(String dataPointIdList) {
        dataPointIdList = "(" + dataPointIdList + ")";
        ejt.update("delete from eventHandlers where mangocode='"+Common.getEnvironmentProfile().getString("db.mangocode")+"' and eventTypeId=" + EventType.EventSources.DATA_POINT
                + " and eventTypeRef1 in " + dataPointIdList);
        ejt.update("delete from userComments where commentType=2 and typeKey in " + dataPointIdList);
        ejt.update("delete from pointEventDetectors where mangocode='"+Common.getEnvironmentProfile().getString("db.mangocode")+"' and dataPointId in " + dataPointIdList);
        ejt.update("delete from dataPointUsers where dataPointId in " + dataPointIdList);
        ejt.update("delete from watchListPoints where dataPointId in " + dataPointIdList);
        ejt.update("delete from dataPoints_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime where id in " + dataPointIdList);

        cachedPointHierarchy = null;
    }

    //
    //
    // Event detectors
    //
    public int getDataPointIdFromDetectorId(int pedId) {
        return ejt.queryForInt("select dataPointId from pointEventDetectors where id=?", new Object[] { pedId });
    }

    public String getDetectorXid(int pedId) {
        return queryForObject("select xid from pointEventDetectors where id=?", new Object[] { pedId }, String.class,
                null);
    }

    public int getDetectorId(String pedXid, int dataPointId) {
        return ejt.queryForInt("select id from pointEventDetectors where xid=? and dataPointId=? and mangocode=?", new Object[] {
                pedXid, dataPointId,Common.getEnvironmentProfile().getString("db.mangocode") }, -1);
    }

    public String generateEventDetectorUniqueXid(int dataPointId) {
        String xid = Common.generateXid(PointEventDetectorVO.XID_PREFIX);
        while (!isEventDetectorXidUnique(dataPointId, xid, -1))
            xid = Common.generateXid(PointEventDetectorVO.XID_PREFIX);
        return xid;
    }

    public boolean isEventDetectorXidUnique(int dataPointId, String xid, int excludeId) {
        return ejt.queryForInt("select count(*) from pointEventDetectors where dataPointId=? and xid=? and id<>? and mangocode=?",
                new Object[] { dataPointId, xid, excludeId,Common.getEnvironmentProfile().getString("db.mangocode") }) == 0;
    }

    private void setEventDetectors(DataPointVO dp) {
        dp.setEventDetectors(getEventDetectors(dp));
    }

    private List<PointEventDetectorVO> getEventDetectors(DataPointVO dp) {
        return query(
                "select id, xid, alias, detectorType, alarmLevel, stateLimit, duration, durationType, binaryState, " //
                        + "  multistateState, changeCount, alphanumericState, weight " //
                        + "from pointEventDetectors " //
                        + "where mangocode='"+Common.getEnvironmentProfile().getString("db.mangocode")+"' and dataPointId=? " // 
                        + "order by id", new Object[] { dp.getId() }, new EventDetectorRowMapper(dp));
    }

    class EventDetectorRowMapper implements GenericRowMapper<PointEventDetectorVO> {
        private final DataPointVO dp;

        public EventDetectorRowMapper(DataPointVO dp) {
            this.dp = dp;
        }

        public PointEventDetectorVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            PointEventDetectorVO detector = new PointEventDetectorVO();
            int i = 0;
            detector.setId(rs.getInt(++i));
            detector.setXid(rs.getString(++i));
            detector.setAlias(rs.getString(++i));
            detector.setDetectorType(rs.getInt(++i));
            detector.setAlarmLevel(rs.getInt(++i));
            detector.setLimit(rs.getDouble(++i));
            detector.setDuration(rs.getInt(++i));
            detector.setDurationType(rs.getInt(++i));
            detector.setBinaryState(charToBool(rs.getString(++i)));
            detector.setMultistateState(rs.getInt(++i));
            detector.setChangeCount(rs.getInt(++i));
            detector.setAlphanumericState(rs.getString(++i));
            detector.setWeight(rs.getDouble(++i));
            detector.njbSetDataPoint(dp);
            return detector;
        }
    }

    private void saveEventDetectors(DataPointVO dp) {
        // Get the ids of the existing detectors for this point.
        final List<PointEventDetectorVO> existingDetectors = getEventDetectors(dp);

        // Insert or update each detector in the point.
        for (PointEventDetectorVO ped : dp.getEventDetectors()) {
            if (ped.getId() < 0) {
                // Insert the record.
            	String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
                ped.setId(doInsert(
                        "insert into pointEventDetectors "
                                + "  (xid, alias, dataPointId, detectorType, alarmLevel, stateLimit, duration, durationType, "
                                + "  binaryState, multistateState, changeCount, alphanumericState, weight,mangocode) "
                                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[] { ped.getXid(), ped.getAlias(), dp.getId(), ped.getDetectorType(),
                                ped.getAlarmLevel(), ped.getLimit(), ped.getDuration(), ped.getDurationType(),
                                boolToChar(ped.isBinaryState()), ped.getMultistateState(), ped.getChangeCount(),
                                ped.getAlphanumericState(), ped.getWeight(),mangocode }, new int[] { Types.VARCHAR,
                                Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.DOUBLE,
                                Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER,
                                Types.VARCHAR, Types.DOUBLE,Types.VARCHAR }));
                AuditEventType.raiseAddedEvent(AuditEventType.TYPE_POINT_EVENT_DETECTOR, ped);
            }
            else {
                PointEventDetectorVO old = removeFromList(existingDetectors, ped.getId());
                String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
                ejt.update(
                        "update pointEventDetectors set xid=?, alias=?, alarmLevel=?, stateLimit=?, duration=?, "
                                + "  durationType=?, binaryState=?, multistateState=?, changeCount=?, alphanumericState=?, "
                                + "  weight=?,mangocode=? " + "where id=?",
                        new Object[] { ped.getXid(), ped.getAlias(), ped.getAlarmLevel(), ped.getLimit(),
                                ped.getDuration(), ped.getDurationType(), boolToChar(ped.isBinaryState()),
                                ped.getMultistateState(), ped.getChangeCount(), ped.getAlphanumericState(),
                                ped.getWeight(),mangocode, ped.getId() }, new int[] { Types.VARCHAR, Types.VARCHAR,
                                Types.INTEGER, Types.DOUBLE, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
                                Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.DOUBLE,Types.VARCHAR,Types.INTEGER });

                AuditEventType.raiseChangedEvent(AuditEventType.TYPE_POINT_EVENT_DETECTOR, old, ped);
            }
        }

        // Delete detectors for any remaining ids in the list of existing detectors.
        for (PointEventDetectorVO ped : existingDetectors) {
            ejt.update("delete from eventHandlers " + "where eventTypeId=" + EventType.EventSources.DATA_POINT
                    + " and eventTypeRef1=? and eventTypeRef2=?", new Object[] { dp.getId(), ped.getId() });
            ejt.update("delete from pointEventDetectors where id=?", new Object[] { ped.getId() });

            AuditEventType.raiseDeletedEvent(AuditEventType.TYPE_POINT_EVENT_DETECTOR, ped);
        }
    }

    private PointEventDetectorVO removeFromList(List<PointEventDetectorVO> list, int id) {
        for (PointEventDetectorVO ped : list) {
            if (ped.getId() == id) {
                list.remove(ped);
                return ped;
            }
        }
        return null;
    }

    public void copyPermissions(final int fromDataPointId, final int toDataPointId) {
        final List<Tuple<Integer, Integer>> ups = query(
                "select userId, permission from dataPointUsers where dataPointId=?", new Object[] { fromDataPointId },
                new GenericRowMapper<Tuple<Integer, Integer>>() {
                    @Override
                    public Tuple<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Tuple<Integer, Integer>(rs.getInt(1), rs.getInt(2));
                    }
                });

        ejt.batchUpdate("insert into dataPointUsers values (?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return ups.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, toDataPointId);
                ps.setInt(2, ups.get(i).getElement1());
                ps.setInt(3, ups.get(i).getElement2());
            }
        });
    }

    //
    //
    // Point comments
    //
    private static final String POINT_COMMENT_SELECT = UserCommentRowMapper.USER_COMMENT_SELECT
            + "where uc.commentType= " + UserComment.TYPE_POINT + " and uc.typeKey=? " + "order by uc.ts";

    private void setPointComments(DataPointVO dp) {
        dp.setComments(query(POINT_COMMENT_SELECT, new Object[] { dp.getId() }, new UserCommentRowMapper()));
    }

    //
    //
    // Point hierarchy
    //
    static PointHierarchy cachedPointHierarchy;

    public PointHierarchy getPointHierarchy() {
      /*  if (cachedPointHierarchy == null) {*/
            final Map<Integer, List<PointFolder>> folders = new HashMap<Integer, List<PointFolder>>();

            // Get the folder list.
            ejt.query("select id, parentId, name from pointHierarchy where mangocode='"+Common.getEnvironmentProfile().getString("db.mangocode")+"'", new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    PointFolder f = new PointFolder(rs.getInt(1), rs.getString(3));
                    int parentId = rs.getInt(2);
                    List<PointFolder> folderList = folders.get(parentId);
                    if (folderList == null) {
                        folderList = new LinkedList<PointFolder>();
                        folders.put(parentId, folderList);
                    }
                    folderList.add(f);
                }
            });

            // Create the folder hierarchy.
            PointHierarchy ph = new PointHierarchy();
            DataSourceDao dataSourceDao=new DataSourceDao();
            addFoldersToHeirarchy(ph, 0, folders);

            // Add data points.
            List<DataPointVO> points = getDataPoints(DataPointExtendedNameComparator.instance, false);
            for (DataPointVO dp : points){
            	DataSourceVO<?> dsvo=null;
            	dsvo=dataSourceDao.getDataSource(dp.getDataSourceId());
                ph.addDataPoint(dp.getId(), dp.getPointFolderId(),dsvo.getName()+" - "+dp.getName());
            }

            cachedPointHierarchy = ph;
        /*}*/

        return ph;
    }

    private void addFoldersToHeirarchy(PointHierarchy ph, int parentId, Map<Integer, List<PointFolder>> folders) {
        List<PointFolder> folderList = folders.remove(parentId);
        if (folderList == null)
            return;

        for (PointFolder f : folderList) {
            ph.addPointFolder(f, parentId);
            addFoldersToHeirarchy(ph, f.getId(), folders);
        }
    }

    public void savePointHierarchy(final PointFolder root) {
        final ExtendedJdbcTemplate ejt2 = ejt;
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                // Dump the hierarchy table.
                ejt2.update("delete from pointHierarchy where mangocode='"+Common.getEnvironmentProfile().getString("db.mangocode")+"'");

                // Save the point folders.
                savePointFolder(root, 0);
            }
        });

        // Save the point folders. This is not done in the transaction because it can cause deadlocks in Derby.
        savePointsInFolder(root);

        cachedPointHierarchy = null;
        cachedPointHierarchy = getPointHierarchy();
        PointHierarchyEventDispatcher.firePointHierarchySaved(root);
    }

    void savePointFolder(PointFolder folder, int parentId) {
        // Save the folder.
    	String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
        if (folder.getId() == Common.NEW_ID)
            folder.setId(doInsert("insert into pointHierarchy (parentId, name,mangocode) values (?,?,?)", new Object[] { parentId,
                    folder.getName(),mangocode }));
        else if (folder.getId() != 0)
            ejt.update("insert into pointHierarchy (id, parentId, name,mangocode) values (?,?,?,?)", new Object[] { folder.getId(),
                    parentId, folder.getName() ,mangocode});

        // Save the subfolders
        for (PointFolder sf : folder.getSubfolders())
            savePointFolder(sf, folder.getId());
    }

    void savePointsInFolder(PointFolder folder) {
        // Save the points in the subfolders
        for (PointFolder sf : folder.getSubfolders())
            savePointsInFolder(sf);

        // Update the folder references in the points.
        DataPointVO dp;
        for (IntValuePair p : folder.getPoints()) {
            dp = getDataPoint(p.getKey());
            // The point may have been deleted while editing the hierarchy.
            if (dp != null) {
                dp.setPointFolderId(folder.getId());
                updateDataPointShallow(dp);
            }
        }
    }

    public List<PointHistoryCount> getTopPointHistoryCounts() {
        List<PointHistoryCount> counts = query(
                "select dataPointId, count(*) from pointValues_"+Common.getEnvironmentProfile().getString("db.mangocode")+"_RealTime group by dataPointId order by 2 desc",
                new GenericRowMapper<PointHistoryCount>() {
                    @Override
                    public PointHistoryCount mapRow(ResultSet rs, int rowNum) throws SQLException {
                        PointHistoryCount c = new PointHistoryCount();
                        c.setPointId(rs.getInt(1));
                        c.setCount(rs.getInt(2));
                        return c;
                    }
                });

        List<DataPointVO> points = getDataPoints(DataPointExtendedNameComparator.instance, false);

        // Collate in the point names.
        for (PointHistoryCount c : counts) {
            for (DataPointVO point : points) {
                if (point.getId() == c.getPointId()) {
                    c.setPointName(point.getExtendedName());
                    break;
                }
            }
        }

        return counts;
    }
}
