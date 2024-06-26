package com.sadalsuud.push.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Description
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 10/12/2023
 */
@Getter
@ToString
@AllArgsConstructor
public enum AuditStatus implements PowerfulEnum {


    /**
     * 00.未提交
     */
    WAIT_COMMIT(0, "待提交审核"),
    /**
     * 10.待审核
     */
    WAIT_AUDIT(10, "待审核"),
    /**
     * 20.审核成功
     */
    AUDIT_SUCCESS(20, "审核成功"),
    /**
     * 30.被拒绝'
     */
    AUDIT_REJECT(30, "被拒绝");

    private final Integer code;
    private final String description;


    public static PowerfulEnum findEnumByCode(Integer code) {
        for (AuditStatus auditStatus : AuditStatus.values()) {
            if (auditStatus.getCode().equals(code)) {
                return auditStatus;
            }
        }
        throw new IllegalArgumentException("auditStatus is invalid");
    }


}
