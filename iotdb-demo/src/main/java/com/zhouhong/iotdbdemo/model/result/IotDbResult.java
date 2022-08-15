package com.zhouhong.iotdbdemo.model.result;

import lombok.Data;

/**
 * description: 返回结果
 * date: 2022/8/15 21:56
 * author: zhouhong
 */
@Data
public class IotDbResult {
    /***
     * 时间
     */
    private String time;
    /***
     * 产品PK
     */
    private  String  pk;
    /***
     * 设备号
     */
    private  String  sn;
    /***
     * 实时呼吸
     */
    private String breath;
    /***
     * 实时心率
     */
    private String heart;

}
