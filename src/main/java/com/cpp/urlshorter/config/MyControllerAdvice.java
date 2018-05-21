package com.cpp.urlshorter.config;

import com.cpp.urlshorter.ApiResonse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * controller 增强器
 *
 * @author sam
 * @since 2017/7/17
 */
@ControllerAdvice
public class MyControllerAdvice {

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiResonse errorHandler(Exception ex) {
        return new ApiResonse(ApiResonse.FAIL_CODE_INTERNAL, ex.getMessage());
    }

}