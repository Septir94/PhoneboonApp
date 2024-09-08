package com.demo.Phonebook.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.demo.Phonebook.enums.ContactTypeEnum;

@Entity
@Table(name = "contact")
public class Contact extends AuditTable<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id",length = 11)
	private int id;
	
	@Column(name = "type",length = 100,nullable = true)
	@Enumerated(EnumType.STRING)
    private ContactTypeEnum type;
	
	@Column(name = "value",length = 100,nullable = true)
    private String value;
	
	@ManyToOne
    @JoinColumn(name="user_info_id", nullable=false)   
	private UserInfo userInfo;
	
	
	@Column(name = "isDeleted",length = 100,nullable = false,columnDefinition = "boolean default false" )
	private boolean isDeleted;
	
		
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public ContactTypeEnum getType() {
		return type;
	}


	public void setType(ContactTypeEnum type) {
		this.type = type;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public UserInfo getUserInfo() {
		return userInfo;
	}


	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}


	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
	
}
