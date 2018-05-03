package com.comviva03.postprocessing;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
	public static void main(String[] args) {
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("applicationContext4.xml", 
						"applicationContext3.xml", "applicationContext2.xml");

		
		Employee employee = context.getBean("emp", Employee.class);
		
		System.out.println("Emp Id " + employee.getEmpId());
		System.out.println("Emp First Name "+ employee.getName().getFirstName());
		System.out.println("Emp Last Name " + employee.getName().getLastName());
		
	}
}
