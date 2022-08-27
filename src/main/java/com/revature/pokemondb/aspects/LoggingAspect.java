package com.revature.pokemondb.aspects;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("allPokeApp()")
    public Object logAdvice(ProceedingJoinPoint joinpoint) throws Throwable {
		// getting the class that the joinpoint is part of so that the logger matches
		Class<?> joinPointClass = joinpoint.getTarget().getClass();

		// this creates a logger for that class
		Logger logger = LoggerFactory.getLogger(joinPointClass);

		String methodName = joinpoint.getSignature().toShortString();
		String arguments = Arrays.toString(joinpoint.getArgs());

		logger.info("Method called: {}", methodName);
		logger.info("with arguments: {}", arguments);

		Object returnVal;

		try {
			// allowing the method to actually execute
			returnVal = joinpoint.proceed();
		} catch (Throwable e) {
			throw new Throwable (e.getClass() + ": " + e.getMessage());
		}

		logger.info("Method returned: {}", returnVal);
		// don't forget to return the return value
		return returnVal;
	}

    // in order to make a reusable pointcut expression,
	// we can use a hook method (method with an empty body used to hold annotations)
	@Pointcut("execution(* com.revature.pokemondb..*(..) )")
	public void allPokeApp() {/* */}
}
