package bg.softuni.damapp.web.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("@annotation(bg.softuni.damapp.validation.annotations.WarnIfPerformanceExceeds)")
    void onWarnIfExecutionTimeExceeds() {
    }
}
