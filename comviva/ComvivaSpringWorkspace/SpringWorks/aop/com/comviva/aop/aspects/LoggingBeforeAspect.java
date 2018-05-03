package com.comviva.aop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingBeforeAspect {

	// @Before(value="execution(public double getBalance())")
//	@Before(value="execution(public double com.comviva.aop.bl.CAAccount.showBalance())")
	
	// @Before(value="execution(public * get*())")
	// @Before(value="execution(* get*())")
	// invoke this aspect if the method takes atleaset 1 arg 
//	@Before(value="execution(* get*(*))")
	
	// invoke either the method takes 0 or more parameters 
//	@Before(value="execution(* get*(..))")
//	public void logBefore1(){
//		// any Business Logic can go here 
//		System.out.println("1st - You are logged Before invocation... ");
//	}
	
	@Before("allGetters()")
	public void firstAdvise(){
		System.out.println("From First Advise");
	}
	
	@Before("allGetters()")
	public void secondAdvice(){
		System.out.println("From Second Advise ");
	}


	@Pointcut("execution(* getBalance())")
	public void allGetters(){
		// this method will act as only place holder 
		// and any executable statement in this method will not work 
		System.out.println("*****************************************");
	}

}
