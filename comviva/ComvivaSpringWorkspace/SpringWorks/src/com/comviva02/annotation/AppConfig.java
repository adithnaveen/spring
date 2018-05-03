package com.comviva02.annotation;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Required
	@Bean(name="english")
	public Greeting english(){
		return new GreetEnglish();
	}
	
	@Bean(name="french")
	public Greeting french(){
		return new GreetFrench();
	}
}	
