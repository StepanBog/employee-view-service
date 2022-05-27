package ru.bogdanov.diplom.backend.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.bogdanov.diplom.backend.data.containers.Auth;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class LogOnLogInLogOutAspect {
    @AfterReturning(
            pointcut = "execution(* ru.bogdanov.diplom.backend.service.impl.AuthService.auth(..))",
            returning = "auth")
    public void logAuthMethod(JoinPoint pj, Auth auth){
        log.info("+++ Logged in: At " + LocalDateTime.now()
                + " A user: " + auth.getUser().getUsername()
                + " and id: " + auth.getUser().getId()
                + " has logged in through: " + pj.getSignature().toLongString());
    }
}
