package com.ssgss.common.constant;

public class SraException extends RuntimeException{
    // 无参构造方法
    public SraException() {
        super();
    }

    // 带有异常信息的构造方法
    public SraException(String message) {
        super(message);
    }

    // 带有异常信息和异常原因的构造方法
    public SraException(String message, Throwable cause) {
        super(message, cause);
    }

    // 带有异常原因的构造方法
    public SraException(Throwable cause) {
        super(cause);
    }
}
