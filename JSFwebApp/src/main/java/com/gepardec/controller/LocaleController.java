package com.gepardec.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class LocaleController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String lang = "en";
	
	public String getLang() {
		return this.lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String change(String lang) {
		this.lang = lang;
		
		return "/index.xhtml";
	}

}
