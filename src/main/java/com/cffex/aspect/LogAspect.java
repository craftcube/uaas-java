package com.cffex.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
@Order(20)
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PerformanceMonitor monitor=null;

	@Pointcut("execution(public * com..controller..*.*(..))")
	private void controllerMethod() {}

	//service public method
	@Pointcut("execution(public * com.cffex.*.service..*.*(..))")
	private void serviceMethod() {}

	@Around("serviceMethod()")
	public Object serviceLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("entering : " + className + ":" + methodName);

		monitor.start(className+'.'+methodName);
		Object obj = joinPoint.proceed();
		monitor.stop(className+'.'+methodName);
		logger.info("leaving : " + className + ":" + methodName);
		logger.info("returned result : " + obj);
		return obj;
	}

	@Around("controllerMethod()")
	public Object controlLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.info("============logAspect==============");

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
//		String params=getParams(joinPoint);
		logger.info("entering : " + className + ":" + methodName);
		monitor.start(className+'.'+methodName);
		Object obj = joinPoint.proceed();
		monitor.stop(className+'.'+methodName);

		logger.info("leaving : " + className + ":" + methodName);
		logger.info("returned result : " + obj);
		return obj;
	}

	private String getParams(ProceedingJoinPoint joinPoint) throws Exception{
		StringBuilder sb = new StringBuilder();
		Object[] arguments = joinPoint.getArgs();
		for(Object argument : arguments) {
			sb.append(objectMapper.writeValueAsString(argument));
		}
		logger.info("���÷���Ĳ��� : " + sb.toString());
		return sb.toString();
	}

}
