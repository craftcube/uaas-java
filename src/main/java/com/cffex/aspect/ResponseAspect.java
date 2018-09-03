package com.cffex.aspect;

import com.cffex.entity.ResponseResult;
import com.cffex.exception.PermissionException;
import com.cffex.exception.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.stream.Stream;

import static com.cffex.utils.GlobalErrorCodeConstants.ERROR_CODE_SUCCESS;
import static com.cffex.utils.GlobalErrorCodeConstants.ERROR_HEADER;

@Aspect
@Component
@Order(1)
public class ResponseAspect {
    private static final Logger logger = LoggerFactory.getLogger(ResponseAspect.class);

    @Autowired(required = true)
    private ApplicationContext context;

    @Pointcut("execution(public * com..controller.*.*(..)) && !within(com..CaptchaController)")
    private void anyMethod() {
    }

    private HttpServletResponse getResponse(ProceedingJoinPoint joinPoint) {
        Object response = Stream.of(joinPoint.getArgs())
                .filter(arg -> arg instanceof HttpServletResponse)
                .findFirst()
                .get();
        return (HttpServletResponse) response;

    }

    @Around("anyMethod()")
    public Object handleResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("===========handleResponse======================");
        HttpServletResponse response = getResponse(joinPoint);
        ResponseResult result = new ResponseResult();

        try {
            Object obj = joinPoint.proceed(joinPoint.getArgs());
            response.setHeader(ERROR_HEADER, ERROR_CODE_SUCCESS);
            result.setData(obj);
        } catch (PermissionException e) {
            response.setHeader("error_code", e.getErrorCode() + "");
            result.setErrorMsg(e.getErrorMsg());
            result = e.getResponseResult();
        } catch (ServiceException e) {
            response.setHeader("error_code", e.getErrorCode() + "");
            result.setErrorMsg(e.getErrorMsg());
            result = e.getResponseResult();
        } catch (Exception e) {
            response.setHeader("error_code", "500");
            result.setErrorMsg(context.getMessage("server.error", new Object[]{}, Locale.SIMPLIFIED_CHINESE));
        }
        return result;
    }

}