package org.guillermo.file.service.impl;

import org.guillermo.file.service.FileCheckService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class BasicFileCheckServiceImpl implements FileCheckService {

	@Override
	public Boolean isValidFile(MultipartFile file) {
		
		return Boolean.TRUE;
	}

}
