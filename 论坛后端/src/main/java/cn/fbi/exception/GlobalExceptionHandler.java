package cn.fbi.exception;


import cn.fbi.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 全局异常处理器
 *
 * 捕获和处理控制器层抛出的异常，并返回统一格式的错误响应。
 */
@ControllerAdvice(basePackages = "com.example.se_practice.controller")
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有类型的异常
     *
     * @param request HTTP请求对象
     * @param e 异常对象
     * @return 统一格式的错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(HttpServletRequest request, Exception e) {
        logger.error("异常信息", e);
        return Result.Error("系统异常");
    }

    /**
     * 处理自定义异常 CustomException
     *
     * @param request HTTP请求对象
     * @param e 自定义异常对象
     * @return 统一格式的错误响应
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result customError(HttpServletRequest request, CustomException e) {
        return Result.Error(e.getMsg());
    }
}
