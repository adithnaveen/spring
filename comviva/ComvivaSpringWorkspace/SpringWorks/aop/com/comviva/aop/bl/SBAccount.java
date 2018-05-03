package com.comviva.aop.bl;

import com.comviva.aop.model.Account;

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
