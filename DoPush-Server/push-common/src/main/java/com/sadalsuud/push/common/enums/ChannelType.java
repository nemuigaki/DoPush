package com.sadalsuud.push.common.enums;


import com.sadalsuud.push.common.dto.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description 发送渠道类型枚举
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 10/12/2023
 */
@Getter
@ToString
@AllArgsConstructor
public enum ChannelType implements PowerfulEnum {


    /**
     * IM(站内信)  -- 未实现该渠道
     */
    IM(10, "IM", ImContentModel.class, "im", null, null),
    /**
     * push(通知栏) --安卓 已接入 个推
     */
    PUSH(20, "push", PushContentModel.class, "push", "ge_tui_access_token_", 3600 * 24L),
    /**
     * sms(短信)  -- 腾讯云、云片
     */
    SMS(30, "sms", SmsContentModel.class, "sms", null, null),
    /**
     * email(邮件) -- QQ、163邮箱
     */
    EMAIL(40, "email", EmailContentModel.class, "email", null, null),
    /**
     * dingDingRobot(钉钉机器人)
     */
    DING_DING_ROBOT(80, "dingDingRobot", DingDingRobotContentModel.class, "ding_ding_robot", null, null),
    /**
     * dingDingWorkNotice(钉钉工作通知)
     */
    DING_DING_WORK_NOTICE(90, "dingDingWorkNotice", DingDingWorkContentModel.class, "ding_ding_work_notice", "ding_ding_access_token_", 3600 * 2L),
    /**
     * feiShuRoot(飞书机器人)
     */
    FEI_SHU_ROBOT(110, "feiShuRoot", FeiShuRobotContentModel.class, "fei_shu_robot", null, null),
    /**
     * alipayMiniProgram(支付宝小程序)
     */
    ALIPAY_MINI_PROGRAM(120, "alipayMiniProgram", AlipayMiniProgramContentModel.class, "alipay_mini_program", null, null),
    ;

    /**
     * 编码值
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;

    /**
     * 内容模型Class
     */
    private final Class<? extends ContentModel> contentModelClass;

    /**
     * 英文标识
     */
    private final String codeEn;

    /**
     * accessToken prefix
     */
    private final String accessTokenPrefix;

    /**
     * accessToken expire
     * 单位秒
     */
    private final Long accessTokenExpire;

    /**
     * 通过code获取class
     *
     * @param code
     * @return
     */
    public static Class<? extends ContentModel> getChanelModelClassByCode(Integer code) {
        return Arrays.stream(values()).filter(channelType -> Objects.equals(code, channelType.getCode()))
                .map(ChannelType::getContentModelClass)
                .findFirst().orElse(null);
    }
}
