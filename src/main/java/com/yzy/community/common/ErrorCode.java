package com.yzy.community.common;

/**
 * 错误码
 *
 * @author yzy
 */
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),

    SIMPLE_ERROR(50001, "非正常操作", ""),

    ACTION_ERROR(50002, "操作失败", ""),

    NOT_LOGIN_ERROR(40100, "未登录", ""),

    NO_AUTH_ERROR(40101, "无权限", ""),
    NOT_FOUND_ERROR(40400, "请求数据不存在", ""),

    FORBIDDEN_ERROR(40300, "禁止访问", ""),

    OPERATION_ERROR(50001, "操作失败", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
