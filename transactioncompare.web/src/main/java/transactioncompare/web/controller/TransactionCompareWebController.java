package transactioncompare.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import transactioncompare.core.bo.FileCompareService;
import transactioncompare.dto.FileInfoDTO;
import transactioncompare.web.exception.FileCompareException;
import transactioncompare.web.exception.ReconciliationException;

@Controller
public class TransactionCompareWebController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCompareWebController.class);

	private final FileCompareService fileCompareService;
	
	private List<FileInfoDTO> infoFiles;
	private Map<String, List<String>> suggestions;
	private List<String> unmatched;

	@Autowired
	public TransactionCompareWebController(FileCompareService fileCompareService) {
		this.fileCompareService = fileCompareService;
	}

	@GetMapping("/home")
	public String getHome(Model model) {
		LOGGER.info("Returning home page");

		if (infoFiles != null) {
			model.addAttribute("fileUploaded", true);
			model.addAttribute("fileInfo1", infoFiles.get(0));
			model.addAttribute("fileInfo2", infoFiles.get(1));
		}
		if (suggestions != null) {
			model.addAttribute("suggestionEnabled", true);
			model.addAttribute("suggestions", suggestions);
			model.addAttribute("unmatched", unmatched);
		}
		
		return "home";
	}

	@PostMapping("/compare")
	public String handleFileComparing(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2, Model model) {

		try {
			LOGGER.info("Starting comparison between " + file1.getOriginalFilename() + " and "
					+ file2.getOriginalFilename());
			infoFiles = fileCompareService.compareFiles(file1, file2);

		} catch (IOException e) {
			throw new FileCompareException(e);
		}

		return "redirect:/home";
	}
	
	@PostMapping("/suggest")
	public String handleSuggestions(Model model) {
		LOGGER.info("Handling possible suggestions");
		try {
			suggestions = fileCompareService.generateSuggestions();
			suggestions.values().stream().filter(list -> list.isEmpty())
					.forEach(list -> list.add("No matching element found."));
			
			unmatched = fileCompareService.getUnmatchedEntries();
			
		} catch (Exception e) {
			throw new ReconciliationException(e);
		}
		
		return "redirect:/home";
	}
}
