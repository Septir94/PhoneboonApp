package com.demo.Phonebook.ExceptionHandler;

public class NotFoundException extends RuntimeException{
	public NotFoundException(String message) {
		super(message);
	}
}
