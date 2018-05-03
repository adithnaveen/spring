package com.comviva.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorld {

	@RequestMapping("/hello.html")
	public ModelAndView helloWorld(){
		String message = "Welcome from Naveen in Comviva, Bengaluru";
		// return new ModelAndView("welcome", "message", message);
		
		// first param : view name 
		// Second param : Model Name 
		// Third Param :Data Object 
		return new ModelAndView("hello", "message", message);
	}
	
	

	@RequestMapping("/")
	public String indexFile(){
		return "index";
	}
	
}