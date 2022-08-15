package com.zhouhong.iotdbdemo.controller;

import com.zhouhong.iotdbdemo.config.IotDBSessionConfig;
import com.zhouhong.iotdbdemo.model.param.IotDbParam;
import com.zhouhong.iotdbdemo.response.ResponseData;
import com.zhouhong.iotdbdemo.server.IotDbServer;
import lombok.extern.log4j.Log4j2;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.rmi.ServerException;

/**
 * description: iotdb 控制层
 * date: 2022/8/15 21:50
 * author: zhouhong
 */
@Log4j2
@RestController
public class IotDbController {

    @Resource
    private IotDbServer iotDbServer;
    @Resource
    private IotDBSessionConfig iotDBSessionConfig;

    /**
     * 插入数据
     * @param iotDbParam
     */
    @PostMapping("/api/device/insert")
    public ResponseData insert(@RequestBody IotDbParam iotDbParam) throws StatementExecutionException, ServerException, IoTDBConnectionException {
        iotDbServer.insertData(iotDbParam);
        return ResponseData.success();
    }

    /**
     * 插入数据
     * @param iotDbParam
     */
    @PostMapping("/api/device/queryData")
    public ResponseData queryDataFromIotDb(@RequestBody IotDbParam iotDbParam) throws Exception {
        return ResponseData.success(iotDbServer.queryDataFromIotDb(iotDbParam));
    }

    /**
     * 删除分组
     * @return
     */
    @PostMapping("/api/device/deleteGroup")
    public ResponseData deleteGroup() throws StatementExecutionException, IoTDBConnectionException {
        iotDBSessionConfig.deleteStorageGroup("root.a1eaKSRpRty");
        iotDBSessionConfig.deleteStorageGroup("root.smartretirement");
        return ResponseData.success();
    }

}
