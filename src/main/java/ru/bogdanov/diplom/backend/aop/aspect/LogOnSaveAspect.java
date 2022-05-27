package ru.bogdanov.diplom.backend.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.bogdanov.diplom.backend.aop.enums.TypesToLogOnSave;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
@Aspect
@Component
public class LogOnSaveAspect {
    @Around("execution(* tech.inno.odp.backend.service.impl.*.save(..))")
    public Object logSaveMethods(ProceedingJoinPoint pj) throws Throwable {
        MethodSignature signature = (MethodSignature) pj.getSignature();
        log.info("+++ The save method is starting, its contract is: " + signature.toLongString());
        Arrays.stream(pj.getArgs())
                .forEach(el -> log.info(TypesToLogOnSave.valueOf(el.getClass().getSimpleName().toUpperCase(Locale.ROOT)).logSaveMethod(el)));
        Object obj = pj.proceed();
        log.info("+++ The save method worked off, its contract is: " + signature.toLongString());
        return obj;
    }
}
