package com.demo.Phonebook.ExceptionHandler;

import java.util.Date;

public class ErrorDetails {
	private Date timestamp;
	private String status;
	private String message;
	private String path;
	

	

	public ErrorDetails(Date timestamp, String status, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.path = path;
	}




	public Date getTimestamp() {
		return timestamp;
	}



	public String getStatus() {
		return status;
	}




	public String getMessage() {
		return message;
	}



	public String getPath() {
		return path;
	}

}
