package com.sadalsuud.push.infrastructure.gatewayImpl.mq.sender.kakfa;

import cn.hutool.core.text.CharSequenceUtil;
import com.sadalsuud.push.domain.support.mq.SendMqService;
import com.sadalsuud.push.infrastructure.gatewayImpl.mq.MessageQueuePipeline;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 13/12/2023
 * @Package com.sadalsuud.push.infrastructure.mq.sender
 */
@Service
@ConditionalOnProperty(value = "dopush.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
@RequiredArgsConstructor
public class KafkaSendMqServiceImpl implements SendMqService {
    private final KafkaTemplate kafkaTemplate;
    @Value("${dopush.business.tagId.key}")
    private String tagIdKey;

    /**
     * 发送消息
     *
     * @param topic
     * @param jsonValue
     * @param tagId
     */
    @Override
    @SuppressWarnings("unchecked")
    public void send(String topic, String jsonValue, String tagId) {
        // 是否需要过滤
        if (CharSequenceUtil.isNotBlank(tagId)) {
            List<Header> headers = Collections.singletonList(
                    new RecordHeader(tagIdKey, tagId.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(new ProducerRecord(topic, null, null, null, jsonValue, headers));
            return;
        }
        kafkaTemplate.send(topic, jsonValue);
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param jsonValue
     */
    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}
