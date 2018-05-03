package com.comviva.aop.service;

import com.comviva.aop.bl.CAAccount;
import com.comviva.aop.bl.SBAccount;

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