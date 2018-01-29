package com.gepardec.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class LocaleController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lang;

	@PostConstruct
	public void init() {
		lang = FacesContext.getCurrentInstance().getApplication().getDefaultLocale().getLanguage();
		System.out.println("Language: " + lang);
	}

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
