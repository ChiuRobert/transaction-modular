package transactioncompare.dto;

public class FileInfoDTO {

	private String name;
	
	private int totalRecords;
	
	private int matchingRecords;
	
	private int unmatchedRecords;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getMatchingRecords() {
		return matchingRecords;
	}

	public void setMatchingRecords(int matchingRecords) {
		this.matchingRecords = matchingRecords;
	}

	public int getUnmatchedRecords() {
		return unmatchedRecords;
	}

	public void setUnmatchedRecords(int unmatchedRecords) {
		this.unmatchedRecords = unmatchedRecords;
	}
}
