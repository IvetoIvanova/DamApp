package bg.softuni.damapp.web.aop;

import bg.softuni.damapp.validation.annotations.WarnIfPerformanceExceeds;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Component
public class MonitoringAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringAspect.class);

    @Around("Pointcuts.onWarnIfExecutionTimeExceeds()")
    Object monitorExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        WarnIfPerformanceExceeds annotation = getAnnotation(pjp);
        long timeInMillis = annotation.timeInMillis();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        var result = pjp.proceed();
        stopWatch.stop();
        long timeForMethodExecution = stopWatch.lastTaskInfo().getTimeMillis();

        if (timeForMethodExecution > timeInMillis) {
            LOGGER.warn("The method {} took {} milliseconds to execute, which exceeds the acceptable threshold of {} milliseconds.",
                    pjp.getSignature(),
                    timeForMethodExecution,
                    timeInMillis);
        }

        return result;
    }

    private static WarnIfPerformanceExceeds getAnnotation(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        return method.getAnnotation(WarnIfPerformanceExceeds.class);
//        return pjp
//                .getTarget()
//                .getClass()
//                .getMethod()
    }
}
