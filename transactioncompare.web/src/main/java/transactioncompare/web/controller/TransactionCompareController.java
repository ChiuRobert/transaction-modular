package transactioncompare.web.controller;

import java.io.IOException;
import java.util.List;

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
import transactioncompare.core.service.DTOConverter;
import transactioncompare.dto.FileInfoDTO;
import transactioncompare.web.exception.FileCompareException;

@Controller
public class TransactionCompareController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCompareController.class);

	private final FileCompareService fileCompareService;

	@Autowired
	public TransactionCompareController(FileCompareService fileCompareService) {
		this.fileCompareService = fileCompareService;
	}

	@GetMapping("/home")
	public String getHome() {
		LOGGER.info("Returning home page");

		return "home";
	}

	@PostMapping("/home")
	public String handleFileComparing(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2, Model model) {

		try {
			LOGGER.info("Starting comparison between " + file1.getOriginalFilename() + " and "
					+ file2.getOriginalFilename());
			List<FileInfoDTO> infoFiles = fileCompareService.compare(file1, file2).stream()
					.map(file -> DTOConverter.convertFileInfoToFileInfoDTO(file)).toList();

			model.addAttribute("fileUploded", true);
			model.addAttribute("fileInfo1", infoFiles.get(0));
			model.addAttribute("fileInfo2", infoFiles.get(1));

		} catch (IOException e) {
			throw new FileCompareException(e);
		}

		return "home";
	}
}
