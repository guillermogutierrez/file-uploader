package org.guillermo.file.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.guillermo.file.api.model.FileOutcome;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	FileOutcome saveFile(MultipartFile file, String path);
	
	void zipFolder(String pathName) throws IOException;
	
	InputStreamResource getFile(String fileName) throws FileNotFoundException;
}
