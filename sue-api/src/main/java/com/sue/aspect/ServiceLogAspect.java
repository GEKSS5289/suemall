package com.sue.aspect;

import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author sue
 * @date 2020/8/1 12:46
 */

@Component
@Aspect
public class ServiceLogAspect {

    public static final Logger log = LoggerFactory.getLogger(ServiceLogAspect.class);
    /**
     * 1.前置通知 在方法调用之前
     * 2.后置通知 在方法调用之后
     * 3.环绕通知 在方法调用前后 都可以执行通知
     * 4.异常通知 发生异常 则通知
     * 5.最终通知 在方法调用之后的通知
     */

    /**
     * 且秒表达式
     * execution 代表所有执行的表达式主体
     * 第一处 * 代表方法放回类型 *代表所有返回类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 ..代表该包以及其子包下的所有方法
     * 第四处 * 代表类名 *代表所有类
     * 第五处 *(..) *代表所有方法名,(..)表示方法中任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.sue.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("======开始执行{}.{}======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName()
        );

        //记录开始时间
        long begin = System.currentTimeMillis();

        //执行目标service
        Object proceed = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long takeTime = end - begin;


        if(takeTime > 3000){
            log.error("====执行结束,耗时:{}毫秒====",takeTime);
        }else if(takeTime > 2000){
            log.warn("====执行结束,耗时:{}毫秒====",takeTime);
        }else{
            log.info("====执行结束,耗时:{}毫秒====",takeTime);
        }

        return proceed;
    }

}
