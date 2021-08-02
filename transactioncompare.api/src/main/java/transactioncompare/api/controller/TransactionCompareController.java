package transactioncompare.api.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import transactioncompare.core.bo.FileCompareService;
import transactioncompare.core.service.DTOConverter;
import transactioncompare.dto.FileInfoDTO;

@RestController
@RequestMapping("/api")
public class TransactionCompareController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCompareController.class);

	private Set<FileInfoDTO> infoFiles;
	
	private final FileCompareService fileCompareService;

	@Autowired
	public TransactionCompareController(FileCompareService fileCompareService) {
		this.fileCompareService = fileCompareService;
	}
	
	@PostMapping("/files/upload")
	public ResponseEntity<?> handleFileComparing(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2) {
		
		try {
			LOGGER.info("Uploading files");
			
			infoFiles = fileCompareService.compare(file1, file2).stream()
					.map(file -> DTOConverter.convertFileInfoToFileInfoDTO(file)).collect(Collectors.toSet());
			
			return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
		} catch(Exception e) {
			LOGGER.error("Error at uploading files", e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/files/info")
	public ResponseEntity<Set<FileInfoDTO>> getFileInfos() {
		if (infoFiles == null) {
			LOGGER.warn("No file has been uploaded");
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		if (infoFiles.isEmpty()) {
			LOGGER.warn("There is no info about the files");
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		
		LOGGER.info("Returning info about the files");
		return new ResponseEntity<>(infoFiles, HttpStatus.OK);
 	}
}
