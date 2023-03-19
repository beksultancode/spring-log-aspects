package springlogaspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SpringBootApplication
public class SpringLogAspectsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLogAspectsApplication.class, args);
    }

}

@Aspect
@Component
class LoggingAspect {

    @Pointcut("@annotation(springlogaspects.Loggable)")
    public void loggableMethods() {}
    @Before("loggableMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before " + joinPoint.getSignature().getName() + "method execution");
    }

    @After("loggableMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After " + joinPoint.getSignature().getName() + "method execution");
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Loggable {}


@RestController
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    @Loggable
    public MyUser getUser() {
        return userService.getUser();
    }

}

@Service
class UserService {

    @Loggable
    public MyUser getUser() {
        return new MyUser("Beksultan", 19);
    }
}

record MyUser(String name, int age) {}