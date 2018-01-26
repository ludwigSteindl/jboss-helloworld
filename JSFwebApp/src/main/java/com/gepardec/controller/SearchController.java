package com.gepardec.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import com.gepardec.model.Cheetah;

@Named
@RequestScoped
public class SearchController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String PATTERN_SEARCH = "^[A-Za-z0-9 .,/'-]+$";

	@PersistenceUnit
	private EntityManagerFactory emf;

	@Resource
	private UserTransaction ut;

	private List<Cheetah> cheetahs;

	private String search;

	@PostConstruct
	public void init() {
		cheetahs = findAll();
	}

	public List<Cheetah> getCheetahs() {
		return cheetahs;
	}

	public void setCheetahs(List<Cheetah> cheetahs) {
		this.cheetahs = cheetahs;
	}

	public String getSearch() {
		return this.search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<Cheetah> findAll() {
		try {
			TypedQuery<Cheetah> query = emf.createEntityManager().createNamedQuery("Cheetah.findAll", Cheetah.class);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Cheetah>();
	}
	

	public void filter() {
		search = search.replaceAll("[+]", "");
		
		if(search.startsWith("0")) {
			search = search.substring(1);
		}

		try {
			EntityManager em = emf.createEntityManager();
			TypedQuery<Cheetah> query = em.createQuery(
					"SELECT c FROM Cheetah c " +
					"WHERE c.name LIKE :search " + 
					"OR c.phone LIKE :search " + 
					"OR c.email LIKE :search " + 
					"OR c.address LIKE :search " + 
					"OR c.birthday LIKE :search ",
					Cheetah.class);
			query.setParameter("search", "%" + search + "%");
			List<Cheetah> list = query.getResultList();

			if (list != null) {
				cheetahs = list;
			} else {
				cheetahs.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), e.getCause().getMessage());
			FacesContext.getCurrentInstance().addMessage("searchForm", fm);
		}

	}

	public void isSearch(FacesContext fc, UIComponent uic, Object obj) throws ValidatorException {
		String value = (String) obj;

		if (!Pattern.matches(PATTERN_SEARCH, value)) {
			throw new ValidatorException(new FacesMessage());
		}
	}

}
