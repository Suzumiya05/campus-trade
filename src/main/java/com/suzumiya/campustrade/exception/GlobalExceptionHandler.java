package com.suzumiya.campustrade.exception;

import com.suzumiya.campustrade.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;   // Spring Boot 3.x
import org.springframework.validation.BindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
//相当于 @ControllerAdvice + @ResponseBody，能直接返回 JSON
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 参数校验异常：@Valid 校验失败时触发
     * 提取第一个字段错误信息返回给前端
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("参数校验失败: {}", e.getMessage());

        // 安全地取出第一个字段错误
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = (fieldError != null && fieldError.getDefaultMessage() != null)
                ? fieldError.getDefaultMessage()
                : "参数校验失败";

        return R.error(400, message);
    }

    /**
     * 处理 @Validated 标注在普通参数（如 @RequestParam / @PathVariable）上的校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("参数约束校验失败: {}", e.getMessage());
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getMessage())
                .orElse("参数校验失败");
        return R.error(400, message);
    }

    /**
     * 处理请求体未传或类型不匹配等 Bind 校验失败
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        log.warn("参数绑定校验失败: {}", e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = (fieldError != null && fieldError.getDefaultMessage() != null)
                ? fieldError.getDefaultMessage()
                : "参数校验失败";
        return R.error(400, message);
    }

    /**
     * 捕获自定义业务异常
     * 在业务逻辑中主动 throw new BusinessException("xxx") 时触发
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());

        // 直接把异常里的提示信息返回给前端
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 1. HTTP 媒体类型不匹配（比如后端要 JSON，前端没传 Content-Type: application/json）
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R<Void> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.warn("媒体类型不匹配: {}", e.getMessage());
        return R.error(415, "不支持的 Content-Type");
    }

    /**
     * 2. 请求参数缺失（比如接口要求传 id，但前端没传）
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<Void> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数: {}", e.getMessage());
        return R.error(400, "缺少必要参数: " + e.getParameterName());
    }

    /**
     * 3. 请求路径不存在（404）
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("接口不存在: {}", e.getRequestURL());
        return R.error(404, "接口不存在");
    }

    /**
     * 兜底：处理所有未被具体捕获的 Exception
     * 保证返回给前端的是统一 JSON 格式，而不是 Whitelabel 错误页
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统未知异常", e);

        // 统一返回友好的用户提示（不暴露内部错误细节）
        return R.error("服务器内部错误，请稍后重试");
    }

    /**
     * 目前若前端发送的 JSON 格式错误或
     * Content-Type 正确但请求体不匹配（如 int 字段收到字符串）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体格式错误: {}", e.getMessage());
        return R.error(400, "请求体格式错误，请检查JSON");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<Void> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {}", e.getMessage());
        return R.error(400, "参数类型错误：" + e.getName());
    }
}
