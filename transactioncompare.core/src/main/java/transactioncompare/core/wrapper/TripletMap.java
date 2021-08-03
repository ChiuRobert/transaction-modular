package transactioncompare.core.wrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Wrapper class mapping 3 values between themselves
 * entryMap: file line entry - TransactionNarrative 
 * countMap: file line entry - number of appearances
 */
public final class TripletMap {

	private final Map<String, String> entryMap;
	
	private final Map<String, Integer> countMap;
	
	public TripletMap() {
		entryMap = new ConcurrentHashMap<>();
		countMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * Adds an element in both wrapped maps
	 * @param entryKey
	 * @param entryValue
	 */
	public void addEntry(String entryKey, String entryValue) {
        if (entryMap.containsKey(entryKey)) {
        	countMap.put(entryKey, countMap.get(entryKey) + 1);
        } else {
        	entryMap.put(entryKey, entryValue);
        	countMap.put(entryKey, 1);
        }
	}
	
	/**
	 * Removes a key from both maps
	 * @param key
	 */
	public void remove(String key) {
		entryMap.remove(key);
		countMap.remove(key);
	}
	
	/**
	 * Returns the size of the maps, both of them have the same size
	 * @return
	 */
	public int getSize() {
		return entryMap.size();
	}
	
	public Map<String, String> getEntryMap() {
		return entryMap;
	}

	public Map<String, Integer> getCountMap() {
		return countMap;
	}
	
}
