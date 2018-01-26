package com.gepardec.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import com.gepardec.model.User;

@Named
@RequestScoped
public class RegisterController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String PATTERN_EMAIL = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	private static final String PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,8}$";

	@PersistenceUnit
	private EntityManagerFactory emf;

	@Resource
	private UserTransaction ut;

	@Inject
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String persist() {
		try {

			ut.begin();
			emf.createEntityManager().persist(user);
			ut.commit();

			/*
			 * FacesMessage m = new FacesMessage("Successfully registered!",
			 * "Your email was saved under id " + user.getId());
			 * FacesContext.getCurrentInstance().addMessage("registerForm", m);
			 */
			return "/search.xhtml";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), e.getCause().getMessage());
			FacesContext.getCurrentInstance().addMessage("registerForm", m);
		}
		return "/register.xhtml";
	}


	public void isEmail(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_EMAIL, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}

	public void isPassword(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_PASSWORD, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}

	public void isAlreadyRegistered(ComponentSystemEvent event) {

		FacesContext fc = FacesContext.getCurrentInstance();

		UIComponent components = event.getComponent();

		UIInput uiInputEmail = (UIInput) components.findComponent("email");
		String email = uiInputEmail.getLocalValue() == null ? "" : uiInputEmail.getLocalValue().toString();

		if (email.isEmpty()) {
			return;
		}
		

		try {
			EntityManager em = emf.createEntityManager();
			TypedQuery<User> query = em.createQuery("SELECT u FROM User u " + "WHERE u.email= :email", User.class);
			query.setParameter("email", email);
			List<User> list = query.getResultList();

			if (list != null) {
				if (!list.isEmpty()) {
					
					Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
					String m = ResourceBundle.getBundle("com.gepardec.properties.messages", locale)
							.getString("error.alreadyRegistered");
					String emailId = uiInputEmail.getClientId();

					FacesMessage msg = new FacesMessage(m);
					fc.addMessage(emailId, msg);
					fc.renderResponse();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), e.getCause().getMessage());
			FacesContext.getCurrentInstance().addMessage("signinForm", fm);
		}

	}

	public void confirmPassword(ComponentSystemEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();

		UIComponent components = event.getComponent();

		UIInput uiInputPassword = (UIInput) components.findComponent("password");
		String password = uiInputPassword.getLocalValue() == null ? "" : uiInputPassword.getLocalValue().toString();

		UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
		String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
				: uiInputConfirmPassword.getLocalValue().toString();

		if (password.isEmpty()) {
			return;
		}

		if (!password.equals(confirmPassword)) {

			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			String m = ResourceBundle.getBundle("com.gepardec.properties.messages", locale)
					.getString("error.confirmPassword");
			String confirmPasswordId = uiInputConfirmPassword.getClientId();

			FacesMessage msg = new FacesMessage(m);
			fc.addMessage(confirmPasswordId, msg);
			fc.renderResponse();

		}
	}

}
