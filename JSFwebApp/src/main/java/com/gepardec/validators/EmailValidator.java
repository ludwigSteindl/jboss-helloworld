package com.gepardec.validators;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "emailValidator")
public class EmailValidator implements Validator {
	
	private static final String PATTERN_EMAIL = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	private static final String MSG_BUNDLE = "com.gepardec.properties.messages";

	@Override
	public void validate(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_EMAIL, value)) {
			//String label = ( (HtmlInputText)uic).getLabel();
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			String msg = ResourceBundle.getBundle(MSG_BUNDLE, locale).getString("error.email");
			
			FacesMessage fm = new FacesMessage(msg + "!");
			throw new ValidatorException(fm);
		}
	}
	
}
