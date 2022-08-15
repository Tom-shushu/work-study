package com.zhouhong.iotdbdemo.config;

import lombok.extern.log4j.Log4j2;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.Session;
import org.apache.iotdb.session.SessionDataSet;
import org.apache.iotdb.session.util.Version;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.tsfile.write.record.Tablet;
import org.apache.iotdb.tsfile.write.schema.MeasurementSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

/**
 * description: iotdb 配置工具类（常用部分，如需要可以自行扩展）
 * 注意：可以不需要创建分组，插入时默认前两个节点名称为分组名称 比如： root.a1eaKSRpRty.CA3013A303A25467 或者
 * root.a1eaKSRpRty.CA3013A303A25467.heart  他们的分组都为 root.a1eaKSRpRty
 * author: zhouhong
 */
@Log4j2
@Component
@Configuration
public class IotDBSessionConfig {

    private static Session session;
    private static final String LOCAL_HOST = "XXX.XX.XXX.XX";
    @Bean
    public Session getSession() throws IoTDBConnectionException, StatementExecutionException {
        if (session == null) {
            log.info("正在连接iotdb.......");
            session = new Session.Builder().host(LOCAL_HOST).port(6667).username("root").password("root").version(Version.V_0_13).build();
            session.open(false);
            session.setFetchSize(100);
            log.info("iotdb连接成功~");
            // 设置时区
            session.setTimeZone("+08:00");
        }
        return session;
    }

    /**
     * description: 带有数据类型的添加操作 - insertRecord没有指定类型
     * author: zhouhong
     * @param  * @param deviceId:节点路径如：root.a1eaKSRpRty.CA3013A303A25467
     *                  time:时间戳
     *                  measurementsList：物理量 即：属性
     *                  type：数据类型： BOOLEAN((byte)0), INT32((byte)1),INT64((byte)2),FLOAT((byte)3),DOUBLE((byte)4),TEXT((byte)5),VECTOR((byte)6);
     *                  valuesList：属性值 --- 属性必须与属性值一一对应
     * @return
     */
    public void insertRecordType(String deviceId, Long time,List<String>  measurementsList, TSDataType type,List<Object> valuesList) throws StatementExecutionException, IoTDBConnectionException, ServerException {
        if (measurementsList.size() != valuesList.size()) {
            throw new ServerException("measurementsList 与 valuesList 值不对应");
        }
        List<TSDataType> types = new ArrayList<>();
        measurementsList.forEach(item -> {
            types.add(type);
        });
        session.insertRecord(deviceId, time, measurementsList, types, valuesList);
    }
    /**
     * description: 带有数据类型的添加操作 - insertRecord没有指定类型
     * author: zhouhong
     * @param  deviceId:节点路径如：root.a1eaKSRpRty.CA3013A303A25467
     * @param  time:时间戳
     * @param  measurementsList：物理量 即：属性
     * @param  valuesList：属性值 --- 属性必须与属性值一一对应
     * @return
     */
    public void insertRecord(String deviceId, Long time,List<String>  measurementsList, List<String> valuesList) throws StatementExecutionException, IoTDBConnectionException, ServerException {
        if (measurementsList.size() == valuesList.size()) {
            session.insertRecord(deviceId, time, measurementsList, valuesList);
        } else {
            log.error("measurementsList 与 valuesList 值不对应");
        }
    }
    /**
     * description: 批量插入
     * author: zhouhong
     */
    public void insertRecords(List<String> deviceIdList, List<Long> timeList, List<List<String>> measurementsList, List<List<String>> valuesList) throws StatementExecutionException, IoTDBConnectionException, ServerException {
        if (measurementsList.size() == valuesList.size()) {
            session.insertRecords(deviceIdList, timeList, measurementsList, valuesList);
        } else {
            log.error("measurementsList 与 valuesList 值不对应");
        }
    }

    /**
     * description: 插入操作
     * author: zhouhong
     * @param  deviceId:节点路径如：root.a1eaKSRpRty.CA3013A303A25467
     *  @param  time:时间戳
     *  @param  schemaList: 属性值 + 数据类型 例子： List<MeasurementSchema> schemaList = new ArrayList<>();  schemaList.add(new MeasurementSchema("breath", TSDataType.INT64));
     *  @param  maxRowNumber：
     * @return
     */
    public void insertTablet(String deviceId,  Long time,List<MeasurementSchema> schemaList, List<Object> valueList,int maxRowNumber) throws StatementExecutionException, IoTDBConnectionException {

        Tablet tablet = new Tablet(deviceId, schemaList, maxRowNumber);
        // 向iotdb里面添加数据
        int rowIndex = tablet.rowSize++;
        tablet.addTimestamp(rowIndex, time);
        for (int i = 0; i < valueList.size(); i++) {
            tablet.addValue(schemaList.get(i).getMeasurementId(), rowIndex, valueList.get(i));
        }
        if (tablet.rowSize == tablet.getMaxRowNumber()) {
            session.insertTablet(tablet, true);
            tablet.reset();
        }
        if (tablet.rowSize != 0) {
            session.insertTablet(tablet);
            tablet.reset();
        }
    }

    /**
     * description: 根据SQL查询
     * author: zhouhong
     */
    public SessionDataSet query(String sql) throws StatementExecutionException, IoTDBConnectionException {
        return session.executeQueryStatement(sql);
    }

    /**
     * description: 删除分组 如 root.a1eaKSRpRty
     * author: zhouhong
     * @param  groupName：分组名称
     * @return
     */
    public void deleteStorageGroup(String groupName) throws StatementExecutionException, IoTDBConnectionException {
        session.deleteStorageGroup(groupName);
    }

    /**
     * description: 根据Timeseries删除  如：root.a1eaKSRpRty.CA3013A303A25467.breath  （个人理解：为具体的物理量）
     * author: zhouhong
     */
    public void deleteTimeseries(String timeseries) throws StatementExecutionException, IoTDBConnectionException {
        session.deleteTimeseries(timeseries);
    }
    /**
     * description: 根据Timeseries批量删除
     * author: zhouhong
     */
    public void deleteTimeserieList(List<String> timeseriesList) throws StatementExecutionException, IoTDBConnectionException {
        session.deleteTimeseries(timeseriesList);
    }

    /**
     * description: 根据分组批量删除
     * author: zhouhong
     */
    public void deleteStorageGroupList(List<String> storageGroupList) throws StatementExecutionException, IoTDBConnectionException {
        session.deleteStorageGroups(storageGroupList);
    }

    /**
     * description: 根据路径和结束时间删除 结束时间之前的所有数据
     * author: zhouhong
     */
    public void deleteDataByPathAndEndTime(String path, Long endTime) throws StatementExecutionException, IoTDBConnectionException {
        session.deleteData(path, endTime);
    }
    /**
     * description: 根据路径集合和结束时间批量删除 结束时间之前的所有数据
     * author: zhouhong
     */
    public void deleteDataByPathListAndEndTime(List<String> pathList, Long endTime) throws StatementExecutionException, IoTDBConnectionException {
        session.deleteData(pathList, endTime);
    }
    /**
     * description: 根据路径集合和时间段批量删除
     * author: zhouhong
     */
    public void deleteDataByPathListAndTime(List<String> pathList, Long startTime,Long endTime) throws StatementExecutionException, IoTDBConnectionException {
        session.deleteData(pathList, startTime, endTime);
    }

}