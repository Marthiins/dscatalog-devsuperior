package com.devsuperior.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Instant timestemp;
	private Integer Status;
	private String error;
	private String message;
	private String path;
	
	public StandardError() {
		
	}

	public StandardError(Instant timestemp, Integer status, String error, String message, String path) {
		super();
		this.timestemp = timestemp;
		Status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public Instant getTimestemp() {
		return timestemp;
	}

	public void setTimestemp(Instant timestemp) {
		this.timestemp = timestemp;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}
