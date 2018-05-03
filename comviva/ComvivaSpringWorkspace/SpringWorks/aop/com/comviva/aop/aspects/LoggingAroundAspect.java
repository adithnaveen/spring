package com.comviva.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingAroundAspect {

	@Around("execution(public * showBalance())")
	public Object sampleAroundAdvise(ProceedingJoinPoint procedingJoinPoint){
		Object object = null;
		
		try {
			System.out.println("Before the method is called ");
			object = procedingJoinPoint.proceed();
			System.out.println("After the method is called.. ");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Exiting the code.. " + object);
		return object; 
	}
}
