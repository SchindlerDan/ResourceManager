
public class Pair {
	Process key;
	Resource value;
	
	public Pair(Process pKey, Resource pValue) {
		key = pKey;
		value = pValue;
	}
	
	public Process getKey() {
		return key;
	}
	
	public Resource getValue() {
		return value;
	}
}
