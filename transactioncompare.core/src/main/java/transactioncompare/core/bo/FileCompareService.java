package transactioncompare.core.bo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import transactioncompare.dto.FileInfoDTO;

public interface FileCompareService {

	/**
	 * Method called to compare the 2 files and gather the necessary information
	 * 
	 * @param file1 first file to be compared
	 * @param file2 second file to be compared
	 * @return list of FileInfoDTO that contains information regarding both files
	 * @throws IOException
	 */
	public List<FileInfoDTO> compareFiles(MultipartFile file1, MultipartFile file2) throws IOException;
	
	/**
	 * Generate suggestions based on the files saved in the compare stage
	 * 
	 * @return a map where the keys are are the entries from the first list
	 * and the values are the suggestions
	 */
	public Map<String, List<String>> generateSuggestions();
	
	/**
	 * @return a list of entries from the secondary file that haven't been matched
	 */
	public List<String> getUnmatchedEntries();
}
