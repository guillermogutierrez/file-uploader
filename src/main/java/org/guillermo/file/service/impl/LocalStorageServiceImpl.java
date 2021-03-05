package org.guillermo.file.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.guillermo.file.api.model.FileOutcome;
import org.guillermo.file.service.FileCheckService;
import org.guillermo.file.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalStorageServiceImpl implements StorageService {

	private static String ROOT_PATH = "./home/spring/files/";
	
	private static String ZIP_NAME = "folder_content.zip";
	
	Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);


	@Autowired
	private FileCheckService fileCheckService;

	@Override
	public FileOutcome saveFile(MultipartFile file, String folderName) {
		
		logger.debug("Saving file {0} in folder{1}".formatted(file.getOriginalFilename(), folderName) );
		
		var outcome = new FileOutcome(file.getOriginalFilename());

		if (fileCheckService.isValidFile(file)) {

			try {
				
				file.transferTo(getPathToStore(file, folderName));
				
				outcome.setResultCode(FileOutcome.ResultCode.FILE_STORED);
				
			} catch (Exception e) {
				
				outcome.setResultCode(FileOutcome.ResultCode.ERROR);
				
				System.err.println(e.getMessage());
			}
			
		}
		else {
			outcome.setResultCode(FileOutcome.ResultCode.FILE_NOT_SUPPORTED);
		}
		
		return outcome;
		
	}
	
	@Override
	public void zipFolder(String folderName) throws IOException {
		
		FileOutputStream fos = new FileOutputStream(getPathToZip(folderName));
		
		ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
		
	    Files.walkFileTree(Paths.get(ROOT_PATH + folderName), new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	          throws IOException {
	        	
	            if (!Files.isDirectory(file) && ! file.getFileName().toString().contains(ZIP_NAME)) {

	    	        var fileInputStream = new FileInputStream(file.toString());
	            	zipOutputStream.putNextEntry(new ZipEntry(file.getFileName().toString()));

	    	        IOUtils.copy(fileInputStream, zipOutputStream);

	    	        fileInputStream.close();
	            }
	            
	            return FileVisitResult.CONTINUE;
	        }
	    });

	    zipOutputStream.close();
		
	}

	@Override
	public InputStreamResource getFile(String fileName) throws FileNotFoundException {
		
		File file = new File(getPathToZip(fileName));
		
		return new InputStreamResource(new FileInputStream(file));
	}
	
	private String getPathToZip(String folderName) {
		return ROOT_PATH + folderName + "/" + ZIP_NAME;
	}
	
	private Path getPathToStore(MultipartFile file, String folderName) {

		Path path = Paths.get(ROOT_PATH + folderName);

		File directory = new File(path.toString());

		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		return path.resolve(file.getOriginalFilename());
	}

}
