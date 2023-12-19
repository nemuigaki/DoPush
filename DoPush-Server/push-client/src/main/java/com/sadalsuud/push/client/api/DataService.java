package com.sadalsuud.push.client.api;

import com.sadalsuud.push.client.vo.DataParam;
import com.sadalsuud.push.common.domain.SimpleAnchorInfo;
import com.sadalsuud.push.domain.gateway.domain.SmsRecord;

import java.util.List;
import java.util.Map;

/**
 * @Description 数据链路追踪获取接口
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 17/12/2023
 * @Package com.sadalsuud.push.client.api
 */
public interface DataService {

    /**
     * 获取全链路追踪 消息自身维度信息
     *
     * @param messageId 消息
     * @return
     */
    Map<String, List<SimpleAnchorInfo>> getTraceMessageInfo(String messageId);

    /**
     * 获取全链路追踪 用户维度信息
     *
     * @param receiver 接收者
     * @return
     */
    Map<String, List<SimpleAnchorInfo>> getTraceUserInfo(String receiver);


    /**
     * 获取全链路追踪 消息模板维度信息
     *
     * @param businessId 业务ID（如果传入消息模板ID，则生成当天的业务ID）
     * @return
     */
    Map<Object, Object> getTraceMessageTemplateInfo(String businessId);


    /**
     * 获取短信下发记录
     *
     * @param dataParam
     * @return
     */
    Map<String, List<SmsRecord>> getTraceSmsInfo(DataParam dataParam);

}