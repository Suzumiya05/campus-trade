package com.suzumiya.campustrade.exception;

/**
 * 自定义业务异常
 * 用于在业务逻辑中抛出异常，例如："用户名已存在"、"余额不足"等
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    /**
     * 只传消息的构造方法（最常用）
     * 默认 code 为 500
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 传 code 和消息的构造方法（如果想细分错误码，比如 400, 401）
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}