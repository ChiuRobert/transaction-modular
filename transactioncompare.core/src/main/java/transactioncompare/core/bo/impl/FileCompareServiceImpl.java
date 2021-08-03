package transactioncompare.core.bo.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import transactioncompare.core.bo.FileCompareService;
import transactioncompare.core.model.FileInfo;
import transactioncompare.core.service.DTOConverter;
import transactioncompare.core.wrapper.TripletMap;
import transactioncompare.dto.FileInfoDTO;

@Service
public class FileCompareServiceImpl implements FileCompareService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileCompareServiceImpl.class);

	private static final int TRANSACTION_NARRATIVE_INDEX = 3;

	private TripletMap firstFileMap;
	private TripletMap secondFileMap;

	@Override
	public List<FileInfoDTO> compareFiles(MultipartFile file1, MultipartFile file2) throws IOException {
		LOGGER.info("Comparing the files.");

		checkFilesState(file1, file2);

		firstFileMap = new TripletMap();
		secondFileMap = new TripletMap();

		BufferedReader bufferedReaderFile1 = new BufferedReader(new InputStreamReader(file1.getInputStream(), StandardCharsets.UTF_8));
		BufferedReader bufferedReaderFile2 = new BufferedReader(new InputStreamReader(file2.getInputStream(), StandardCharsets.UTF_8));

		String currentLine;
		int totalLinesFirstFile = 0;
		int totalLinesSecondFile = 0;

		try (bufferedReaderFile1; bufferedReaderFile2) {
			// Parse the first file and add the elements in a map wrapper
			while ((currentLine = bufferedReaderFile1.readLine()) != null) {
				totalLinesFirstFile++;
				firstFileMap.addEntry(currentLine, extractTransactionNarrative(currentLine));
			}

			// While parsing the second file, the 2 files are being compared
			// and the lines that are unique are being saved
			while ((currentLine = bufferedReaderFile2.readLine()) != null) {
				totalLinesSecondFile++;
				if (firstFileMap.getEntryMap().containsKey(currentLine)) {
					// In case an entry exists in both files it can be excluded
					int expectedCount = firstFileMap.getCountMap().get(currentLine) - 1;
					if (expectedCount == 0) {
						firstFileMap.remove(currentLine);
					} else {
						firstFileMap.getCountMap().put(currentLine, expectedCount);
					}
				} else {
					secondFileMap.addEntry(currentLine, extractTransactionNarrative(currentLine));
				}
			}

			totalLinesFirstFile--;
			totalLinesSecondFile--;
		}

		return createFileInfoDTOs(file1, file2, totalLinesFirstFile, totalLinesSecondFile);
	}

	@Override
	public Map<String, List<String>> generateSuggestions() {
		LOGGER.info("Starting to search for suggestions.");

		checkMapsState();

		// The key is the file entry while the List is for duplicated suggestions
		// existing in the second file
		Map<String, List<String>> suggestions = new HashMap<>();

		for (String key : firstFileMap.getEntryMap().keySet()) {
			for (String secondKey : secondFileMap.getEntryMap().keySet()) {
				suggestions.putIfAbsent(key, new ArrayList<String>());

				// If the same Transaction Narrative exists in both maps then it's a suggestion
				if (firstFileMap.getEntryMap().get(key).equals(secondFileMap.getEntryMap().get(secondKey))) {
					for (int i = 0; i < secondFileMap.getCountMap().get(secondKey); i++) {
						// Add all the suggestions to the key
						suggestions.get(key).add(secondKey);
					}
					secondFileMap.remove(secondKey);
				}
			}
		}

		return suggestions;
	}

	@Override
	public List<String> getUnmatchedEntries() {
		return secondFileMap.getEntryMap().keySet().stream().collect(Collectors.toList());
	}

	private List<FileInfoDTO> createFileInfoDTOs(MultipartFile file1, MultipartFile file2, int totalLinesFirstFile,
			int totalLinesSecondFile) {
		// The sum takes into consideration the duplicated entries as well
		int unmatchedRecordsFirstFile = firstFileMap.getCountMap().values().stream().mapToInt(Integer::valueOf).sum();
		int unmatchedRecordsSecondFile = secondFileMap.getCountMap().values().stream().mapToInt(Integer::valueOf).sum();

		FileInfo firstFileInfo = new FileInfo();
		firstFileInfo.setName(file1.getOriginalFilename());
		firstFileInfo.setTotalRecords(totalLinesFirstFile);
		firstFileInfo.setUnmatchedRecords(unmatchedRecordsFirstFile);
		firstFileInfo.setMatchingRecords(totalLinesFirstFile - unmatchedRecordsFirstFile);

		FileInfo secondFileInfo = new FileInfo();
		secondFileInfo.setName(file2.getOriginalFilename());
		secondFileInfo.setTotalRecords(totalLinesSecondFile);
		secondFileInfo.setUnmatchedRecords(unmatchedRecordsSecondFile);
		secondFileInfo.setMatchingRecords(totalLinesSecondFile - unmatchedRecordsSecondFile);

		return List.of(firstFileInfo, secondFileInfo).stream()
				.map(file -> DTOConverter.convertFileInfoToFileInfoDTO(file)).collect(Collectors.toList());
	}

	private String extractTransactionNarrative(String entry) {
		return entry.split(",")[TRANSACTION_NARRATIVE_INDEX];
	}

	private void checkFilesState(MultipartFile file1, MultipartFile file2) throws IOException {
		if (file1.isEmpty() || file2.isEmpty()) {
			LOGGER.error("The files were not uploaded.");
			throw new FileNotFoundException("The files were not uploaded.");
		}
	}

	private void checkMapsState() {
		if (firstFileMap == null) {
			LOGGER.error("The first map is null.");
			throw new IllegalStateException("The first map is null");
		}
		if (secondFileMap == null) {
			LOGGER.error("The second map is null.");
			throw new IllegalStateException("The second map is null");
		}
	}

}
