package com.comviva.aop.bl;

import com.comviva.aop.model.Account;

public class CAAccount {
	private Account acc;
	
	public Account getAcc() {
		return acc;
	}

	public void setAcc(Account acc) {
		this.acc = acc;
	}

	public double showBalance(){
		int x = 11; 
		
		if(x == 12 ){
			throw new RuntimeException("User thrown an exception");
		}
		System.out.println("I'm i show Balance... ");
		return acc.getBalance();
	}
}
