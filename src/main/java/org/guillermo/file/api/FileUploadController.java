package org.guillermo.file.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.guillermo.file.api.model.ResponseMessage;
import org.guillermo.file.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@RestController
@JsonAutoDetect(getterVisibility=Visibility.NONE)
public class FileUploadController {

	@Autowired
	private StorageService storageService;

	@PostMapping(value = "/multiple-upload", 
			  consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
			  produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<ResponseMessage> multipleUpload(@RequestParam("files") MultipartFile[] files) {

		UUID uuid = UUID.randomUUID();

		var folderName = uuid.toString();

		ResponseMessage responseMessage = new ResponseMessage(folderName);

		try {

			Arrays.asList(files).stream().forEach(file -> {
				var saveOutcome = storageService.saveFile(file, folderName);
				responseMessage.addFileOutcome(saveOutcome);
			});

			storageService.zipFolder(folderName);

			return ResponseEntity.status(HttpStatus.OK).body(responseMessage);

		} catch (Exception e) {

			System.out.print(e.getMessage());

			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
		}
	}

	@RequestMapping(path = "/download/{fileKey}", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadZipFile(@PathVariable String fileKey) throws IOException {
 
		try {
			InputStreamResource resource = storageService.getFile(fileKey);

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (FileNotFoundException fileNotFoundException) { 
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, fileKey + " Not Found", fileNotFoundException);
		}
	}

}
