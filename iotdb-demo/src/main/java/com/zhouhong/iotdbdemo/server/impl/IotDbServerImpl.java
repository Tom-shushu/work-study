package com.zhouhong.iotdbdemo.server.impl;

import com.zhouhong.iotdbdemo.config.IotDBSessionConfig;
import com.zhouhong.iotdbdemo.model.param.IotDbParam;
import com.zhouhong.iotdbdemo.model.result.IotDbResult;
import com.zhouhong.iotdbdemo.server.IotDbServer;
import lombok.extern.log4j.Log4j2;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.SessionDataSet;
import org.apache.iotdb.tsfile.read.common.Field;
import org.apache.iotdb.tsfile.read.common.RowRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: iot服务实现类
 * date: 2022/8/15 9:43
 * author: zhouhong
 */

@Log4j2
@Service
public class IotDbServerImpl implements IotDbServer {

    @Resource
    private IotDBSessionConfig iotDBSessionConfig;

    @Override
    public void insertData(IotDbParam iotDbParam) throws StatementExecutionException, ServerException, IoTDBConnectionException {
        // iotDbParam: 模拟设备上报消息
        // bizkey: 业务唯一key  PK :产品唯一编码   SN:设备唯一编码
        String deviceId = "root.bizkey."+ iotDbParam.getPk() + "." + iotDbParam.getSn();
        // 将设备上报的数据存入数据库(时序数据库)
        List<String> measurementsList = new ArrayList<>();
        measurementsList.add("heart");
        measurementsList.add("breath");
        List<String> valuesList = new ArrayList<>();
        valuesList.add(String.valueOf(iotDbParam.getHeart()));
        valuesList.add(String.valueOf(iotDbParam.getBreath()));
        iotDBSessionConfig.insertRecord(deviceId, iotDbParam.getTime(), measurementsList, valuesList);
    }

    @Override
    public List<IotDbResult> queryDataFromIotDb(IotDbParam iotDbParam) throws Exception {
        List<IotDbResult> iotDbResultList = new ArrayList<>();

        if (null != iotDbParam.getPk() && null != iotDbParam.getSn()) {
            String sql = "select * from root.bizkey."+ iotDbParam.getPk() +"." + iotDbParam.getSn() + " where time >= "
                    + iotDbParam.getStartTime() + " and time < " + iotDbParam.getEndTime();
            SessionDataSet sessionDataSet = iotDBSessionConfig.query(sql);
            List<String> columnNames = sessionDataSet.getColumnNames();
            List<String> titleList = new ArrayList<>();
            // 排除Time字段 -- 方便后面后面拼装数据
            for (int i = 1; i < columnNames.size(); i++) {
                String[] temp = columnNames.get(i).split("\\.");
                titleList.add(temp[temp.length - 1]);
            }
            // 封装处理数据
            packagingData(iotDbParam, iotDbResultList, sessionDataSet, titleList);
        } else {
            log.info("PK或者SN不能为空！！");
        }
        return iotDbResultList;
    }
    /**
     * 封装处理数据
     * @param iotDbParam
     * @param iotDbResultList
     * @param sessionDataSet
     * @param titleList
     * @throws StatementExecutionException
     * @throws IoTDBConnectionException
     */
    private void packagingData(IotDbParam iotDbParam, List<IotDbResult> iotDbResultList, SessionDataSet sessionDataSet, List<String> titleList)
            throws StatementExecutionException, IoTDBConnectionException {
        int fetchSize = sessionDataSet.getFetchSize();
        if (fetchSize > 0) {
            while (sessionDataSet.hasNext()) {
                IotDbResult iotDbResult = new IotDbResult();
                RowRecord next = sessionDataSet.next();
                List<Field> fields = next.getFields();
                String timeString = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(next.getTimestamp());
                iotDbResult.setTime(timeString);
                Map<String, String> map = new HashMap<>();

                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    // 这里的需要按照类型获取
                    map.put(titleList.get(i), field.getObjectValue(field.getDataType()).toString());
                }
                iotDbResult.setTime(timeString);
                iotDbResult.setPk(iotDbParam.getPk());
                iotDbResult.setSn(iotDbParam.getSn());
                iotDbResult.setHeart(map.get("heart"));
                iotDbResult.setBreath(map.get("breath"));
                iotDbResultList.add(iotDbResult);
            }
        }
    }
}
