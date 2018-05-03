package com.comviva04.autowire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mobile {
	private Screen scr;
	private Speaker speaker;
	private Camera camera;

	public Mobile() {
		System.out.println("Creating Mobile " + this);
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	@Autowired
	public void setSpeaker(Speaker speaker) {
		System.out.println("Setting Speaker " + speaker);
		this.speaker = speaker;
	}

	public Camera getCamera() {
		return camera;
	}

	@Autowired
	public void setCamera(Camera camera) {
		System.out.println("Setting Camera " + camera);
		this.camera = camera;
	}

	public Screen getScr() {
		return scr;
	}

	@Autowired(required=false)
	public void setScr(Screen scr) {
		System.out.println("Setting Screen " + scr);
		this.scr = scr;
	}

	@Override
	public String toString() {
		return "Mobile [screen=" + scr + ", speaker=" + speaker + ", camera=" + camera + "]";
	}

}