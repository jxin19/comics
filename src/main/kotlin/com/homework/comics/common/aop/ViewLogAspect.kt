package com.homework.comics.common.aop

import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.viewLog.application.ViewLogCommandService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class ViewLogAspect(
    private val viewLogCommandService: ViewLogCommandService,
    private val authenticationFacade: AuthenticationFacade
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Pointcut("@annotation(com.homework.comics.common.aop.ViewLog)")
    fun viewLogAnnotation() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun controllerPointcut() {
    }

    @AfterReturning(
        "viewLogAnnotation() && controllerPointcut() && args(workId, ..)",
        returning = "result"
    )
    fun logging(joinPoint: JoinPoint, workId: Long, result: Any?) {
        try {
            val memberId = authenticationFacade.getMemberId()
            if (memberId != null) {
                viewLogCommandService.create(workId, memberId)
            }
        } catch (e: Exception) {
            logger.error("Failed to log view history: ${e.message}", e)
        }
    }

}
