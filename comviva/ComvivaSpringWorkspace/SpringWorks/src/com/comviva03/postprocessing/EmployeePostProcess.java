package com.comviva03.postprocessing;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class EmployeePostProcess implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object arg0, String arg1) throws BeansException {

		if(arg0 instanceof Employee){
			Employee temp = (Employee) arg0; 
			
			temp.getName().setFirstName(temp.getName().getFirstName().toUpperCase());
			temp.getName().setLastName(temp.getName().getLastName().toUpperCase());
			
			return temp; 
		}
		
		return arg0; 
	}

	@Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
		// TODO Auto-generated method stub
		return arg0;
	}

}
