package cn.fbi.common;

/**
 * 封装统一的数据返回类型。
 * code为响应码
 * msg为消息描述。
 * data为任意类型的数据。
 */
public class Result {
    /**响应码**/
    private int code;
    /**消息**/
    private String msg;
    /**数据**/
    private Object data;

    /**
     * 返回成功的Result对象，仅包含消息描述。
     *
     * @param msg 消息描述
     * @return 成功的Result对象
     */
    public static Result Success(String msg) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(Code.SUCCESS);
        return result;
    }

    /**
     * 返回成功的Result对象，包含消息描述和数据。
     *
     * @param msg  消息描述
     * @param data 返回的数据
     * @return 成功的Result对象
     */
    public static Result Success(String msg, Object data) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(Code.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     * 返回失败的Result对象，包含错误消息描述。
     *
     * @param msg 错误消息描述
     * @return 失败的Result对象
     */
    public static Result Error(String msg) {
        Result result = new Result();
        result.setCode(Code.ERROR);
        result.setMsg(msg);
        return result;
    }

    /**
     * 获取响应码。
     *
     * @return 响应码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置响应码。
     *
     * @param code 要设置的响应码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取消息。
     *
     * @return 消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息。
     *
     * @param msg 要设置的消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取数据。
     *
     * @return 数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置数据。
     *
     * @param data 要设置的数据
     */
    public void setData(Object data) {
        this.data = data;
    }
}
