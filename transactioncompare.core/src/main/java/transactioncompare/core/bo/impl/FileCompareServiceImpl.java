package transactioncompare.core.bo.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import transactioncompare.core.bo.FileCompareService;
import transactioncompare.core.model.FileInfo;

@Service
public class FileCompareServiceImpl implements FileCompareService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCompareServiceImpl.class);

	@Override
	public List<FileInfo> compare(MultipartFile file1, MultipartFile file2) throws IOException {
	    Set<String> firstFile = new HashSet<>();
	    Set<String> secondFile = new HashSet<>();

	    BufferedReader bufferedReaderFile1 = new BufferedReader(new InputStreamReader(file1.getInputStream(), StandardCharsets.UTF_8));
	    BufferedReader bufferedReaderFile2 = new BufferedReader(new InputStreamReader(file2.getInputStream(), StandardCharsets.UTF_8));
	    BufferedWriter bufferedWriterCompare = new BufferedWriter(new FileWriter(new File("upload-dir/comparison.txt")));

	    String currentLine;
	    int linesFirstFile = 0;
	    int linesSecondFile = 0;

	    try (bufferedReaderFile1; bufferedReaderFile2) {
		    while ((currentLine = bufferedReaderFile1. readLine()) != null) {
		    	linesFirstFile++;
		        firstFile.add(currentLine);
		    }
		    
		    while ((currentLine = bufferedReaderFile2.readLine()) != null) {
		    	linesSecondFile++;
		    	secondFile.add(currentLine);
		    }
	    }
	    
	    Set<String> one = new HashSet<>(firstFile);
	    Set<String> two = new HashSet<>(secondFile);
	    one.removeAll(secondFile);
	    two.removeAll(firstFile);
	    
	    linesFirstFile--;
	    linesSecondFile--;
	    
	    bufferedWriterCompare.write("Total elements in first: " + linesFirstFile + "\n");
	    bufferedWriterCompare.write("Nr of elements in first: " + one.size() + "\n");
	    bufferedWriterCompare.write("Records which are not present in second file\n");
	    for (String key : one) {
            bufferedWriterCompare.write(key);
            bufferedWriterCompare.newLine();
	    }
	    
	    FileInfo firstFileInfo = new FileInfo();
	    firstFileInfo.setName(file1.getOriginalFilename());
	    firstFileInfo.setTotalRecords(linesFirstFile);
	    firstFileInfo.setUnmatchedRecords(one.size());
	    firstFileInfo.setMatchingRecords(linesFirstFile - one.size());
	    
	    bufferedWriterCompare.write("Total elements in first: " + linesSecondFile + "\n");
	    bufferedWriterCompare.write("Nr of elements in second: " + two.size() + "\n");
	    bufferedWriterCompare.write("Records which are in actual but not present in first file\n");
	    for (String key : two) {
            bufferedWriterCompare.write(key);
            bufferedWriterCompare.newLine();
	    }
	    
	    FileInfo secondFileInfo = new FileInfo();
	    secondFileInfo.setName(file2.getOriginalFilename());
	    secondFileInfo.setTotalRecords(linesSecondFile);
	    secondFileInfo.setUnmatchedRecords(two.size());
	    secondFileInfo.setMatchingRecords(linesSecondFile - two.size());
	    
	    bufferedWriterCompare.flush();
	    bufferedWriterCompare.close();
	    
	    return List.of(firstFileInfo, secondFileInfo);
	}
}
