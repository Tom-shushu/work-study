package com.zhouhong.iotdbdemo.model.param;

import lombok.Data;
/**
 * description: 入参
 * date: 2022/8/15 21:53
 * author: zhouhong
 */
@Data
public class IotDbParam {
    /***
     * 产品PK
     */
    private  String  pk;
    /***
     * 设备号
     */
    private  String  sn;
    /***
     * 时间
     */
    private Long time;
    /***
     * 实时呼吸
     */
    private String breath;
    /***
     * 实时心率
     */
    private String heart;
    /***
     * 查询开始时间
     */
    private String startTime;
    /***
     * 查询结束时间
     */
    private String endTime;

}
