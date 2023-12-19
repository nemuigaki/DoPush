package com.sadalsuud.push.infrastructure.utils;

import cn.hutool.core.text.CharSequenceUtil;
import com.sadalsuud.push.common.enums.AnchorState;
import com.sadalsuud.push.common.enums.ChannelType;
import com.sadalsuud.push.common.enums.EnumUtil;
import me.chanjar.weixin.common.error.WxCpErrorMsgEnum;
import me.chanjar.weixin.common.error.WxMaErrorMsgEnum;
import me.chanjar.weixin.common.error.WxMpErrorMsgEnum;

/**
 * @Description
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 17/12/2023
 * @Package com.sadalsuud.push.infrastructure.utils
 */
public class AnchorStateUtils {

    private AnchorStateUtils() {

    }

    /**
     * 根据渠道和点位 获取点位的描述
     *
     * @param channel 发送渠道
     * @param state   点位状态码
     * @return 点位描述
     */
    public static String getDescriptionByState(Integer channel, Integer state) {
        String stateDescription = EnumUtil.getDescriptionByCode(state, AnchorState.class);

        // 如果 AnchorState 找不到对应的点位描述，那就是在对应渠道的点位信息
        if (CharSequenceUtil.isBlank(stateDescription)) {
            if (ChannelType.MINI_PROGRAM.getCode().equals(channel)) {
                stateDescription = WxMaErrorMsgEnum.findMsgByCode(state);
            } else if (ChannelType.OFFICIAL_ACCOUNT.getCode().equals(channel)) {
                stateDescription = WxMpErrorMsgEnum.findMsgByCode(state);
            } else if (ChannelType.ENTERPRISE_WE_CHAT.getCode().equals(channel)) {
                stateDescription = WxCpErrorMsgEnum.findMsgByCode(state);
            }
        }
        return stateDescription;

    }
}