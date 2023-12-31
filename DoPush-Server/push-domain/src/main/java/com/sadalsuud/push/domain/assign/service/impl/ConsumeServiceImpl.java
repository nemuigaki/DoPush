package com.sadalsuud.push.domain.assign.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.sadalsuud.push.common.domain.AnchorInfo;
import com.sadalsuud.push.common.domain.LogParam;
import com.sadalsuud.push.common.domain.RecallTaskInfo;
import com.sadalsuud.push.common.domain.TaskInfo;
import com.sadalsuud.push.common.enums.AnchorState;
import com.sadalsuud.push.domain.assign.pending.Task;
import com.sadalsuud.push.domain.assign.pending.TaskPendingHolder;
import com.sadalsuud.push.domain.assign.service.ConsumeService;
import com.sadalsuud.push.domain.support.GroupIdMappingUtils;
import com.sadalsuud.push.domain.support.LogUtils;
import com.sadalsuud.push.domain.assign.handler.HandlerHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @Description
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 11/12/2023
 * @Package com.sadalsuud.push.domain.pipeline.task.service.impl
 */
@Service
@RequiredArgsConstructor
public class ConsumeServiceImpl implements ConsumeService {
    private static final String LOG_BIZ_TYPE = "Receiver#consumer";
    private static final String LOG_BIZ_RECALL_TYPE = "Receiver#recall";

    private final ApplicationContext context;

    private final TaskPendingHolder taskPendingHolder;

    private final LogUtils logUtils;

    private final HandlerHolder handlerHolder;

    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
        for (TaskInfo taskInfo : taskInfoLists) {
            logUtils.print(
                    LogParam.builder()
                            .bizType(LOG_BIZ_TYPE)
                            .object(taskInfo).build(),
                    AnchorInfo.builder()
                            .bizId(taskInfo.getBizId())
                            .messageId(taskInfo.getMessageId())
                            .ids(taskInfo.getReceiver())
                            .businessId(taskInfo.getBusinessId())
                            .state(AnchorState.RECEIVE.getCode()).build());
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            ExecutorService route = taskPendingHolder.route(topicGroupId);
            route.execute(task);
        }
    }

    @Override
    public void consume2recall(RecallTaskInfo recallTaskInfo) {
        logUtils.print(LogParam.builder().bizType(LOG_BIZ_RECALL_TYPE).object(recallTaskInfo).build());
        handlerHolder.route(recallTaskInfo.getSendChannel()).recall(recallTaskInfo);
    }
}
