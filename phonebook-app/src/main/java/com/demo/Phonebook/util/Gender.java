package com.demo.Phonebook.util;

public enum Gender {
	Unknown(0),Pria(1),Wanita(2);
	private final int id;
	private Gender(int id) {
		// TODO Auto-generated constructor stub
		this.id=id;
	}
	
	public int getValue() {
		return id;
	}
}
