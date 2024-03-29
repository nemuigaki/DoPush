package com.sadalsuud.push.application.service;

import cn.hutool.core.util.ObjectUtil;
import cn.monitor4all.logRecord.annotation.OperationLog;
import com.sadalsuud.push.client.api.SendService;
import com.sadalsuud.push.common.domain.SimpleTaskInfo;
import com.sadalsuud.push.common.enums.MessageStatus;
import com.sadalsuud.push.common.enums.RespStatusEnum;
import com.sadalsuud.push.common.pipeline.ProcessContext;
import com.sadalsuud.push.common.pipeline.ProcessController;
import com.sadalsuud.push.common.vo.BasicResultVO;
import com.sadalsuud.push.domain.receive.BatchSendRequest;
import com.sadalsuud.push.domain.receive.SendRequest;
import com.sadalsuud.push.domain.receive.SendResponse;
import com.sadalsuud.push.domain.receive.SendTaskModel;
import com.sadalsuud.push.domain.template.MessageTemplate;
import com.sadalsuud.push.domain.template.repository.IMessageTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Description
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 10/12/2023
 * @Package com.sadalsuud.push.domain.facade
 */
@Service
public class SendServiceImpl implements SendService {

    @Resource(name = "apiProcessController")
    private ProcessController processController;

    //TODO 发送逻辑和模板仓储逻辑解耦
    @Resource
    private IMessageTemplateRepository messageTemplateRepository;

    @Override
    @OperationLog(bizType = "SendService#send", bizId = "#sendRequest.messageTemplateId", msg = "#sendRequest")
    @SuppressWarnings("unchecked")
    public SendResponse send(SendRequest sendRequest) {
        if (ObjectUtils.isEmpty(sendRequest)) {
            System.out.println("sendRequest is empty");
            return new SendResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg(), null);
        }

        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam()))
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();

        ProcessContext process = processController.process(context);

        //根据相应状态更新模板状态
        Optional<MessageTemplate> messageTemplate = messageTemplateRepository.findById(sendTaskModel.getMessageTemplateId());
        MessageTemplate clone;
        if (messageTemplate.isPresent()) {
            clone = ObjectUtil.clone(messageTemplate.get());
            clone.setMsgStatus(
                    "200".equals(process.getResponse().getStatus()) ?
                    MessageStatus.PENDING.getCode() :
                    MessageStatus.SEND_FAIL.getCode());
        }

        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg(), (List<SimpleTaskInfo>) process.getResponse().getData());
    }

    @Override
    @OperationLog(bizType = "SendService#batchSend", bizId = "#batchSendRequest.messageTemplateId", msg = "#batchSendRequest")
    @SuppressWarnings("unchecked")
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        if (ObjectUtils.isEmpty(batchSendRequest)) {
            return new SendResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg(), null);
        }

        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(batchSendRequest.getMessageTemplateId())
                .messageParamList(batchSendRequest.getMessageParamList())
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(batchSendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();

        ProcessContext process = processController.process(context);

        //根据相应状态更新模板状态
        Optional<MessageTemplate> messageTemplate = messageTemplateRepository.findById(sendTaskModel.getMessageTemplateId());
        MessageTemplate clone;
        if (messageTemplate.isPresent()) {
            clone = ObjectUtil.clone(messageTemplate.get());
            clone.setMsgStatus(
                    "200".equals(process.getResponse().getStatus()) ?
                            MessageStatus.PENDING.getCode() :
                            MessageStatus.SEND_FAIL.getCode());
        }

        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg(), (List<SimpleTaskInfo>) process.getResponse().getData());
    }


}
