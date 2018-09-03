package com.cffex.aspect;

import com.cffex.entity.Role;
import com.cffex.entity.User;
import com.cffex.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Ming on 2016/5/4.
 */

@Aspect
@Component
@Order(10)
public class AuthenticationAspect {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);

    @Autowired
    private Environment env;


    @Autowired
    private JWTUtils jwtUtils;

//    JWTVerifier jwtVerifier = new JWTVerifier(env.getProperty("jwt.security.key"));

    // controller pointcut without logonController and logoutController
    @Pointcut("execution(public * com..controller..*.*(..)) && !within(com..LoginController) && !within(com..CaptchaController)")
    private void controllerMethod() {
    }

    @Around("controllerMethod()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("=========AuthenticationAspect=============");


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        final String token = request.getHeader("X-AUTH-TOKEN");
        if (token == null) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        try {
//            Map<String, Object> decodedPayload = jwtVerifier.verify(token);
            if(jwtUtils.validateToken(token)){
                String username=jwtUtils.getUsernameFromToken(token);
                Claims claims=jwtUtils.getAllClaimsFromToken(token);
                User user =User.builder().username(username).roles((List<Role>)claims.get("roles")).build();
                request.setAttribute("user", user);
            }

//            logger.debug(decodedPayload.toString());

//            User user = new User();
//            user.setUserId(Long.parseLong(decodedPayload.get("id").toString()));
//            user.setMail(decodedPayload.get("email").toString());
//            user.setMobile(decodedPayload.get("cellphone") != null ? Long.parseLong(decodedPayload.get("cellphone").toString()) : null);
//            decodedPayload.get("roles").

            Object obj = joinPoint.proceed(joinPoint.getArgs());
            return obj;
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException("Invalid token.");
        }

    }
}
