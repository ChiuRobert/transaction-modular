package transactioncompare.api.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import transactioncompare.core.bo.FileCompareService;
import transactioncompare.dto.FileInfoDTO;

@RestController
@RequestMapping("/api")
public class TransactionCompareAPIController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCompareAPIController.class);

	private final FileCompareService fileCompareService;

	@Autowired
	public TransactionCompareAPIController(FileCompareService fileCompareService) {
		this.fileCompareService = fileCompareService;
	}
	
	@PostMapping("/files/info")
	public ResponseEntity<?> handleFileComparing(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2) {
		
		try {
			LOGGER.info("Uploading files and returning info");
			
			List<FileInfoDTO> infoFiles = fileCompareService.compareFiles(file1, file2);
			
			if (file1.isEmpty() || file2.isEmpty()) {
				LOGGER.warn("Need to upload the files");
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			if (infoFiles.isEmpty()) {
				LOGGER.warn("There is no info about the files");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(infoFiles, HttpStatus.OK);
		} catch(Exception e) {
			LOGGER.error("Error at uploading files", e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/files/1/suggestions")
	public ResponseEntity<?> handleSuggestion(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2) {
		
		try {
			LOGGER.info("Uploading files and returning suggestions");
			
			Map<String, List<String>> suggestions = fileCompareService.generateSuggestions();
			suggestions.values().stream().filter(list -> list.isEmpty())
					.forEach(list -> list.add("No matching element found."));
			
			if (file1.isEmpty() || file2.isEmpty()) {
				LOGGER.warn("Need to upload the files");
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			if (suggestions.isEmpty()) {
				LOGGER.warn("No suggestions available");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(suggestions, HttpStatus.OK);
		} catch(Exception e) {
			LOGGER.error("Error at uploading files", e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/files/2/missings")
	public ResponseEntity<?> handleMissings(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2) {
		
		try {
			LOGGER.info("Uploading files and returning the missings");
			
			List<String> unmatched = fileCompareService.getUnmatchedEntries();;
			
			if (file1.isEmpty() || file2.isEmpty()) {
				LOGGER.warn("Need to upload the files");
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			if (unmatched.isEmpty()) {
				LOGGER.warn("No missings available");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(unmatched, HttpStatus.OK);
		} catch(Exception e) {
			LOGGER.error("Error at uploading files", e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
