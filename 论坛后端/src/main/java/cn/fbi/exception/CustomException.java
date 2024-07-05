package cn.fbi.exception;

/**
 * 自定义异常信息捕获类
 * 继承自 RuntimeException，用于表示业务逻辑中的异常情况。
 */
public class CustomException extends RuntimeException {
    private String msg;

    /**
     * 构造方法，传入异常信息
     *
     * @param msg 异常信息
     */
    public CustomException(String msg) {
        this.msg = msg;
    }

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置异常信息
     *
     * @param msg 异常信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
