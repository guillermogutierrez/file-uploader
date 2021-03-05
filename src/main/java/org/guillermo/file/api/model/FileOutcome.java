package org.guillermo.file.api.model;

public class FileOutcome {

	public enum ResultCode {FILE_STORED, FILE_NOT_SUPPORTED, ERROR}
	
	private String fileName;
	
	private ResultCode resultCode;
	
	public FileOutcome(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	public FileOutcome(String fileName, ResultCode resultCode) {
		super();
		this.fileName = fileName;
		this.resultCode = resultCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}
	
}
