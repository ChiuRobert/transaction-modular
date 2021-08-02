package transactioncompare.core.bo;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import transactioncompare.core.model.FileInfo;

public interface FileCompareService {

	public List<FileInfo> compare(MultipartFile file1, MultipartFile file2) throws IOException;
}
