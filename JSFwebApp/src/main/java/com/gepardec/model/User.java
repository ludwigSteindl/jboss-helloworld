package com.gepardec.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.NamedQuery;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;

@Entity
//@Table(schema="PUBLIC")
//@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String email;
	private String password;

	
	
	public Long getId() {
		return this.id;	
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String toString() {
		return 
				"[" + getId() + "," + 
				getEmail() + "," + 
				getPassword() + "]";
	
	}
	

}
