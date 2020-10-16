package com.redis.limit;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;

//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * <p>
 * 防重提交切面
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/12 10:54
 * @Version 1.0
 */

@Component
@Aspect
@Slf4j
public class LimitSubmitAspect {



    //@Pointcut("@annotation(com.we.mall.common.annotation.LimitSubmit)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object handleSubmit(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取注解信息
        LimitSubmit limitSubmit = method.getAnnotation(LimitSubmit.class);
        int submitTimeLimiter = limitSubmit.limit();
        String redisKey = limitSubmit.key();
        boolean needAllWait = limitSubmit.needAllWait();

        String v = "key";
        String key = getRedisKey(v, joinPoint, redisKey);
        /*Object result = redisService.get(key);
        if (result != null) {
            throw new ApiException("请勿重复访问！");
        }
        redisService.set(key, v, submitTimeLimiter);*/
        try {
            Object proceed = joinPoint.proceed();
            return proceed;
        } catch (Throwable e) {
            log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
            throw e;
        } finally {
            if (!needAllWait) {
                //redisService.del(redisKey);
            }
        }
    }

    /**
     * 支持多参数，从请求参数进行处理
     */
    private String getRedisKey(String uid, ProceedingJoinPoint joinPoint, String key) {
        if (key.contains("%s")) {
            key = String.format(key, uid);
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                /*String item = parameterNames[i];
                if (key.contains("#" + item)) {
                    key = key.replace("#" + item, joinPoint.getArgs()[i].toString());
                }*/
                String args  = StringUtils.isEmpty(joinPoint.getArgs()[i]) ? null : joinPoint.getArgs()[i].toString();
                key += ":" + args;
            }
        }
        return key;
    }
}
