package com.comviva05.collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyCollection {
	List  questionBank;
	Set addressSet;
	Map accountMap;
	
	
	public List getQuestionBank() {
		return questionBank;
	}
	public void setQuestionBank(List questionBank) {
		this.questionBank = questionBank;
	}
	public Set getAddressSet() {
		return addressSet;
	}
	public void setAddressSet(Set addressSet) {
		this.addressSet = addressSet;
	}
	public Map getAccountMap() {
		return accountMap;
	}
	public void setAccountMap(Map accountMap) {
		this.accountMap = accountMap;
	}
	@Override
	public String toString() {
		return "MyCollection [questionBank=" + questionBank + ", addressSet=" + addressSet + ", accountMap="
				+ accountMap + "]";
	}
	
	
}