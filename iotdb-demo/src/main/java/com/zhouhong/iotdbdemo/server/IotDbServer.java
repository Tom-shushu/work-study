package com.zhouhong.iotdbdemo.server;

import com.zhouhong.iotdbdemo.model.param.IotDbParam;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;

import java.rmi.ServerException;

/**
 * description: iot服务类
 * date: 2022/8/15 21:41
 * author: zhouhong
 */

public interface IotDbServer {
    /**
     * 添加数据
     */
    void insertData(IotDbParam iotDbParam) throws StatementExecutionException, ServerException, IoTDBConnectionException;

    /**
     * 查询数据
     */
    Object queryDataFromIotDb(IotDbParam iotDbParam) throws Exception;
}
