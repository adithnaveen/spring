package com.comviva.aop.aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAfterAspect {
	@After(value="execution(* showBalance(..))")
	public void logAfter(){
		// any Business Logic can go here 
		System.out.println("You are logged After invocation... ");
	}
	
	@AfterReturning(value="execution(* showBalance(..))")
	public void logAfterReturning(){
		System.out.println("Log after successful returning ");
	}
	
	@AfterThrowing(value="execution(* showBalance(..))")
	public void logAfterException(){
		System.out.println("When exception was thrown... ");
	}
}
