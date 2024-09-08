package com.demo.Phonebook.DTO;

public class ResponseBody<T> {
	private String message;
	private String code;
	private  T data;
	public ResponseBody() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResponseBody(String message, T body) {
		super();
		this.message = message;
		this.data = body;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getBody() {
		return data;
	}
	public void setBody(T body) {
		this.data = body;
	}
	
}
