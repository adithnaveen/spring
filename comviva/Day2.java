----------------------------------------------------------------------------------------------------------------------------
AOP
----------------------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
    xmlns:aop="http://www.springframework.org/schema/aop"   
       xsi:schemaLocation="http://www.springframework.org/schema/beans   
       http://www.springframework.org/schema/beans/spring-beans.xsd   
       http://www.springframework.org/schema/aop   
       http://www.springframework.org/schema/aop/spring-aop.xsd">  
  
  		<!--  to define aop, this is how spring container will come to know -->
		<aop:aspectj-autoproxy/>
	
		<!--  and have to given the aspet bean information  
		<bean id="loggingAspect" class="com.naveen.aop.LoggingAspect"/>
		-->
		
		<!--  for after annotation 
		<bean id="loggingAspect" class="com.naveen.aop.LoggingAspect1"/>
		-->
		
		<!-- for around advise -->
		<bean id="loggingAspect" class="com.naveen.aop.LoggingAspect2"/>
		
		
		
		
		<bean id="account1" class="com.naveen.model.Account">
			<property name="accNo" value="101"/>
			<property name="accName" value="Naveen"/>
			<property name="balance" value="10000"/>	
		</bean>
		
		<bean id="account2" class="com.naveen.model.Account">
			<property name="accNo" value="201"/>
			<property name="accName" value="Kanchan"/>
			<property name="balance" value="20000"/>	
		</bean>
			
		<bean id="caAccount" class="com.naveen.bl.CAAccount">
			<property name="acc" ref="account1"/>
		</bean>
		
		<bean id="sbAccount" class="com.naveen.bl.SBAccount">
			<property name="acc" ref="account2"/>
		</bean>
		
		<bean id="banking" class="com.naveen.service.BankingService" 
			autowire="byName"/>
</beans>


---------------------------------------------------------------------------------------------------------------------

package com.naveen.model;

public class Account {
	private int accNo;
	private String accName;
	private double balance;
	
	public int getAccNo() {
		return accNo;
	}
	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}
	public String getAccName() {
		System.out.println(":::::::::: in get Acc Name ::::::");
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Account [accNo=" + accNo + ", accName=" + accName + ", balance=" + balance + "]";
	}
	
	
}

--------------------------------------------------------------------------------------------------------------------------

package com.naveen.bl;

import com.naveen.model.Account;

public class CAAccount {
	private Account acc;
	
	public Account getAcc() {
		return acc;
	}

	public void setAcc(Account acc) {
		this.acc = acc;
	}

	public double showBalance(){
		return acc.getBalance();
	}
}

------------------------------------------------------------------------------------------------------------------------

package com.naveen.bl;

import com.naveen.model.Account;

public class SBAccount {
	@Override
	public String toString() {
		return "SBAccount [acc=" + acc + "]";
	}

	private Account acc;

	public Account getAcc() {
	/*	if("hi".equals("hi"))
				throw new RuntimeException("my exception");
	*/	
		return acc;
	}

	public void setAcc(Account acc) {
		this.acc = acc;
	}

	public double showBalance() {
		System.out.println("Show Balance called " + acc.getBalance());
		return acc.getBalance();
	}
	
	public String takeNGiveSomething(String name){
		return name +" something";
	}
}

---------------------------------------------------------------------------------------------------------------------------



package com.naveen.service;

import com.naveen.bl.CAAccount;
import com.naveen.bl.SBAccount;

public class BankingService {
	private SBAccount sbAccount;
	private CAAccount caAccount;
	
	public SBAccount getSbAccount() {
//		System.out.println("in Getter SB Account");
//		if("hi".equals("hi"))
//			throw new RuntimeException("exception thrown");
		return sbAccount;
	}
	public void setSbAccount(SBAccount sbAccount) {
		this.sbAccount = sbAccount;
	}
	public CAAccount getCaAccount() {
		return caAccount;
	}
	public void setCaAccount(CAAccount caAccount) {
		this.caAccount = caAccount;
	}
	@Override
	public String toString() {
		return "BankingService [sbAccount=" + sbAccount + ", caAccount=" + caAccount + "]";
	}
	
	
}

