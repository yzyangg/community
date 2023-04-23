package com.yzy.community.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yzy
 */
@Getter
@AllArgsConstructor
public enum PermissionEnum {
    /**
     * 用户管理权限
     */
    USER(0, "普通用户"),
    /**
     * 管理员权限
     */
    ADMIN(1, "超级管理员"),

    /**
     * 无需校验,
     */
    NO(-99999, "无需权限"),
    ;
    /**
     * 权限编码
     */
    private Integer code;
    /**
     * 权限名称
     */
    private String msg;
}
