package com.cffex.aspect;

import com.cffex.entity.Role;
import com.cffex.entity.User;
import com.cffex.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
import java.util.Date;
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
    @Pointcut("execution(public * com..controller..*.*(..)) && !within(com..LoginController)")
    private void controllerMethod() {
    }

    @Before("controllerMethod()")
    public void controllerAround(JoinPoint joinPoint) throws Throwable {
        logger.info("=========AuthenticationAspect=============");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String token = request.getHeader("AUTH-TOKEN");
        if (token == null) {
            throw new ServletException("Missing or invalid Authorization header.");
        }
        logger.info(token);
        try{
            Jws<Claims> claims= jwtUtils.getAllClaimsFromToken(token);
            Date expireDate=claims.getBody().getExpiration();
            String username=claims.getBody().getSubject();
            List<Role> roles=(List<Role>)claims.getBody().get("roles");
            User user =User.builder().username(username).roles(roles).build();
            request.setAttribute("user", user);
        }catch(JwtException e){
            throw new ServletException("You are not authorized!");
        }


//        try {
//            Map<String, Object> decodedPayload = jwtVerifier.verify(token);
//            if(jwtUtils.validateToken(token)){
//                String username=jwtUtils.getUsernameFromToken(token);
//                Claims claims=jwtUtils.getAllClaimsFromToken(token);
//                User user =User.builder().username(username).roles((List<Role>)claims.get("roles")).build();
//                request.setAttribute("user", user);
//            }


//            logger.debug(decodedPayload.toString());
//            User user = new User();
//            user.setUserId(Long.parseLong(decodedPayload.get("id").toString()));
//            user.setMail(decodedPayload.get("email").toString());
//            user.setMobile(decodedPayload.get("cellphone") != null ? Long.parseLong(decodedPayload.get("cellphone").toString()) : null);
//            decodedPayload.get("roles").
//            Object obj = joinPoint.proceed(joinPoint.getArgs());
//            return obj;
//        } catch (final Exception e) {
//            logger.error(e.getMessage(), e);
//            throw new ServletException("Invalid token.");
//        }

    }
}
