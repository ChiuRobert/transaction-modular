package transactioncompare.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import transactioncompare.core.model.FileInfo;
import transactioncompare.dto.FileInfoDTO;

public abstract class DTOConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DTOConverter.class);
	
	public static FileInfoDTO convertFileInfoToFileInfoDTO(FileInfo fileInfo) {
		LOGGER.info("Converting FileInfo to FileInfoDTO");
		
		FileInfoDTO fileInfoDTO = new FileInfoDTO();
		
		fileInfoDTO.setName(fileInfo.getName());
		fileInfoDTO.setTotalRecords(fileInfo.getTotalRecords());
		fileInfoDTO.setUnmatchedRecords(fileInfo.getUnmatchedRecords());
		fileInfoDTO.setMatchingRecords(fileInfo.getMatchingRecords());
		
		return fileInfoDTO;
	}
}