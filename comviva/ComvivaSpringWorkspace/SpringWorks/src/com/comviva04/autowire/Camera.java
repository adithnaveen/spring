package com.comviva04.autowire;
import org.springframework.stereotype.Component;

@Component
public class Camera {
	private String type;

	public Camera() {
		System.out.println("Creating Camera " + this );
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Camera [type=" + type + "]";
	}

}