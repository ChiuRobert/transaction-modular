package transactioncompare.web.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import transactioncompare.core.model.FileInfo;
import transactioncompare.core.service.FileCompare;
import transactioncompare.web.exception.FileCompareException;

@Controller
public class FileUploadController {

	private final FileCompare fileCompare;

	@Autowired
	public FileUploadController(FileCompare fileCompare) {
		this.fileCompare = fileCompare;
	}

	@GetMapping("/")
	public String getMainPage() {
		return "index";
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2, Model model) {

		try {
			List<FileInfo> infoFiles = fileCompare.compare(file1, file2);
			
			model.addAttribute("fileUploded", true);
			model.addAttribute("fileInfo1", infoFiles.get(0));
			model.addAttribute("fileInfo2", infoFiles.get(1));
			
		} catch (IOException e) {
			throw new FileCompareException(e);
		}

		return "index";
	}
}
