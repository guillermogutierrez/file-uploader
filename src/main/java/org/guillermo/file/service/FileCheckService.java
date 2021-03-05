package org.guillermo.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileCheckService {

	public Boolean isValidFile(MultipartFile file);
}
