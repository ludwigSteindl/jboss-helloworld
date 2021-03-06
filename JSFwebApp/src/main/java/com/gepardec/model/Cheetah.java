package com.gepardec.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Entity
@Table(schema="PUBLIC", name="CHEETAH")
@NamedQuery(name="Cheetah.findAll", query="SELECT c FROM Cheetah c")
public class Cheetah implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(
			schema=			"PUBLIC",
			name=			"CHEETAH_ID_GENERATOR",
			sequenceName=	"SEQ_CHEETAH",
			allocationSize=	1,
			initialValue=	1
			)
	@GeneratedValue(
			strategy=		GenerationType.SEQUENCE,
			generator=		"CHEETAH_ID_GENERATOR"
			)
	private Long id;
	
	
	private String name;
	private String phone;
	private String email;
	private String address;
	
	@NotNull
	@Past
	private String birthday;
	
	@Basic(fetch=FetchType.LAZY)
	@Lob
	private byte[] photo;
	
	public Cheetah() {
	}
	
	
	public Long getId() {
		return this.id;	
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public byte[] getPhoto() {
		return this.photo;
	}
	
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	
	public String toString() {
		return 
				"[" + getId() + "," +
				getName() + "," +
				getPhone() + "," +
				getEmail() + "," + 
				getAddress() + "," + 
				getBirthday() + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cheetah)) {
			return false;
		}
		Cheetah other = (Cheetah) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
}
