package transactioncompare.core.model;

import java.util.Objects;

public class FileInfo {
	
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

	@Override
	public int hashCode() {
		return Objects.hash(matchingRecords, name, totalRecords, unmatchedRecords);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileInfo other = (FileInfo) obj;
		return matchingRecords == other.matchingRecords && Objects.equals(name, other.name)
				&& totalRecords == other.totalRecords && unmatchedRecords == other.unmatchedRecords;
	}

	@Override
	public String toString() {
		return "FileInfo [name=" + name + ", totalRecords=" + totalRecords + ", matchingRecords=" + matchingRecords
				+ ", unmatchedRecords=" + unmatchedRecords + "]";
	}
}