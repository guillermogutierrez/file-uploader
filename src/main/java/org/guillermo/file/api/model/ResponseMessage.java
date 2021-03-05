package org.guillermo.file.api.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseMessage {

	private String id;
	
	private List<FileOutcome> fileOutcomes;

	public ResponseMessage(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FileOutcome> getFileOutcomes() {
		return fileOutcomes;
	}

	public void setFileNames(List<FileOutcome> fileOutcomes) {
		this.fileOutcomes = fileOutcomes;
	}
	
	public void addFileOutcome(FileOutcome fileOutcome) {
		if (this.fileOutcomes == null) {
			this.fileOutcomes = new ArrayList<FileOutcome>();
		}
		
		this.fileOutcomes.add(fileOutcome);
	}
}