package ee.icefire.vault.security.rls;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * Created by Felipe Carvalho on 2019-03-28.
 */

@Aspect
@Component
@EnableAspectJAutoProxy
public class RowLevelSecurityAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RowLevelSecurityAspect.class);

    private RowLevelSecurityHandler handler;

    @Autowired
    public RowLevelSecurityAspect(RowLevelSecurityHandler rowLevelSecurityAspect) {
        this.handler = rowLevelSecurityAspect;
    }

    @Before("@within(org.springframework.stereotype.Service) && !(@annotation(ee.icefire.vault.security.rls.DisableRowLevelSecurity)|| @within(ee.icefire.vault.security.rls.DisableRowLevelSecurity))")
    public void enableRowLevelSecurityFilters(JoinPoint joinPoint) {
        LOGGER.debug("Enabling Row-Level Security for {}.{}", joinPoint.getSignature().getDeclaringTypeName()
                , joinPoint.getSignature().getName());
        this.handler.applyRowLevelSecurity();
    }
}